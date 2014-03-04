package com.hiveview.domybox.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hiveview.domybox.R;
import com.hiveview.domybox.activity.adapter.PreMarketPageAdapter;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.service.HiveViewService;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.FileUtils;
import com.hiveview.domybox.utils.Logger;
import com.hiveview.domybox.utils.MD5Utils;
import com.hiveview.domybox.utils.ToastUtil;
import com.hiveview.domybox.utils.TypefaceUtils;
import com.hiveview.domybox.view.CommonAppView;
import com.hiveview.domybox.view.DialogAppMarket;
import com.hiveview.domybox.view.PreloadingViewPager;
import com.hiveview.domybox.view.PreloadingViewPager.OnPreloadingListener;
@SuppressLint("HandlerLeak")
public class MyAppMarketActivity extends BaseActivity {
	
	private PreloadingViewPager viewPager;
	private TextView tv_app_title;
	private TextView tv_app_page;
	private TextView tv_app_installed;
	protected PreMarketPageAdapter adapter;
	protected int mSelectedPageIndex;
	protected boolean isNext;
	protected CommonAppView mCurrentView;
	protected int mViewPageScrollState;
	protected CommonAppView lastCommonListView;
	private TextView tv_app_market;
	private TextView tv_app_total;
	
	
	protected ArrayList<AppInfoEntity> appInstalledList;
	
	public ArrayList<AppMarketEntity> appList  ; //用来存储获取的应用信息数据;
	protected int 	count;		//总页数
	protected int 	size;		//下载文件大小
	protected File 	fileApk;	//下载apk
	
	protected static final int MSG_GET_MARKET_APP = 2000;	//获取市场信息
	protected static final int PAGE_COUNT 	= 	12;
	private static final String TAG = "MyAppMarketActivity";
	
	protected static final int PAGE_NO = 1;		//页数
	protected static int PAGE_SIZE = 60;		//每页的个数
	protected static final int MSG_DATA_CHANGE = 234345;
	
	private int SELECTED_LINE	=	1;
	protected boolean isNoData 	= 	false;
	
	private BroadcastReceiver installReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			 //接收卸载广播  
			Logger.e("MyAppMarketActivity",intent.getAction());
			
			HashMap<String, AppInfoEntity> arrApp =new HashMap<String, AppInfoEntity>();
			arrApp = PengDoctorApplication.appinfo;
			
			String str = intent.getDataString(); 
			
			String packageName = str.substring(str.indexOf(":")+1);
		        
	        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {   
	        	
	        	arrApp.put(packageName, null);
	        	PengDoctorApplication.appinfo.put(packageName, new AppInfoEntity());
	        }else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
	        	
	        	arrApp.remove(packageName);
	        	PengDoctorApplication.appinfo.remove(packageName);
			}
	        
	        //删除 文件
	        FileUtils.delecte(fileApk);
	        PengDoctorApplication.appinfo	=	arrApp;
	        Logger.e(TAG, PengDoctorApplication.appinfo.size()+"");
	        
	        dataChange();
		}
	};
	
	
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case MSG_GET_MARKET_APP:
				progressDialogView.setVisibility(View.GONE);
//				loading.setVisibility(View.GONE);
//				iv_loading.clearAnimation();
				if(appList!=null&&appList.size()!=0){
					if(appList.size()%PAGE_COUNT==0){
						count = appList.size()/PAGE_COUNT;
					}else {
						count = appList.size()/PAGE_COUNT + 1;
					}
					tv_app_installed.setText("共"+appList.size()+"个");
					tv_app_page.setText("第"+ "1/" +count+ "页");
					
					
					adapter 	=	new PreMarketPageAdapter(MyAppMarketActivity.this, appList,mHandler);
					viewPager.setAdapter(adapter);
					
//					adapter.notifyDataSetChanged();
					
				}else{
					ToastUtil.showMessageView(MyAppMarketActivity.this, "  网络不给力啊 ！ ", 3);
//					ll_progressBar_application.setVisibility(View.GONE);
//					mViewPager.setVisibility(View.VISIBLE);
					tv_app_installed.setText("已安装"+0+"个");
					tv_app_page.setText("第"+ "1/" + 1+"页");
				}
				break;
			case AppConstant.MSG_DIALOG_PROGRESS:
				ProgressBar progressbar = DialogAppMarket.getInstance(MyAppMarketActivity.this).progressBar1;
				int add=msg.arg1;
				progressbar.incrementProgressBy(add);
				
				Logger.e(TAG, size+"-------------------"+progressbar.getProgress());
				if(size == progressbar.getProgress()){
					
//					DialogAppMarket.getInstance(MyAppMarketActivity.this).dialog.dismiss();
					try {
						String md5	=	MD5Utils.getFileMD5String(fileApk);
						Logger.e(TAG, md5);
						Logger.e(TAG, CommonAppView.currentEntity.getMd5());
						if(CommonAppView.currentEntity.getMd5().equals(md5)){
							installFile(fileApk.getAbsolutePath());
						}else {
							ToastUtil.showMessageView(MyAppMarketActivity.this, "MD5校验失败", 1);
							DialogAppMarket.getInstance(MyAppMarketActivity.this).dialog.dismiss();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(size < progressbar.getProgress()) {
					mHandler.sendEmptyMessage(AppConstant.INSTALL_DEFEATE);
				}
				break;
			case AppConstant.MSG_DIALOG_GETSIZE:
				size=msg.arg1;
				fileApk=(File) msg.obj;
				DialogAppMarket.getInstance(MyAppMarketActivity.this).progressBar1.setMax(size*2);
				break;
				
			case AppConstant.MSG_DIALOG_UNINSTALL:
				ProgressBar uninstallProgressBar = DialogAppMarket.getInstance(MyAppMarketActivity.this).progressBar1;
				if(uninstallProgressBar.getProgress()<90){
					uninstallProgressBar.incrementProgressBy(5);
				}
				break;
				
			case AppConstant.MSG_DIALOG_INSTALL:
				ProgressBar installProgressBar = DialogAppMarket.getInstance(MyAppMarketActivity.this).progressBar1;
				if(installProgressBar.getProgress()<(size*2)){
					installProgressBar.incrementProgressBy(size/10);
					mHandler.sendEmptyMessageDelayed(AppConstant.MSG_DIALOG_INSTALL, 5000);
				}else {
					DialogAppMarket.getInstance(MyAppMarketActivity.this).installDefeated();
				}
				break;
			case AppConstant.MSG_DIALOG_INSTALL_COMPLETE:
				mHandler.removeMessages(AppConstant.MSG_DIALOG_INSTALL);
				DialogAppMarket  dialogAppMarket= DialogAppMarket.getInstance(MyAppMarketActivity.this);
				ProgressBar installCompleteProgressBar = dialogAppMarket.progressBar1;
				installCompleteProgressBar.setProgress(installCompleteProgressBar.getMax());
				
				dialogAppMarket.ll_progressbar.setVisibility(View.GONE);
				dialogAppMarket.ll_dialog_two_button.setVisibility(View.GONE);
				dialogAppMarket.ll_dialog_complete.setVisibility(View.VISIBLE);
//				dialogAppMarket.ll_dialog_
				break;
				
			case MSG_DATA_CHANGE:
//				PengDoctorApplication.appInstalledList=appInstalledList;
//				PengDoctorApplication.
//				adapter.notifyDataSetChanged();
				
//				DialogAppMarket.getInstance(MyAppMarketActivity.this).installComplete();
				
				PreMarketPageAdapter adapter = (PreMarketPageAdapter) viewPager.getAdapter();
				mCurrentView = (CommonAppView) adapter.getPrimaryItem();
				mCurrentView.setDataChange();
				break;
				
			case AppConstant.MSG_DOWNLOAD_DEFEATED:
				DialogAppMarket.getInstance(MyAppMarketActivity.this).downloadDefeated();
				break;
				
			case AppConstant.INSTALL_SUCCEE:
				mHandler.removeMessages(AppConstant.MSG_DIALOG_INSTALL);
				DialogAppMarket.getInstance(MyAppMarketActivity.this).complete();
				break;
				
			case AppConstant.INSTALL_DEFEATE:
				mHandler.removeMessages(AppConstant.MSG_DIALOG_INSTALL);
				DialogAppMarket.getInstance(MyAppMarketActivity.this).installDefeated();
				break;
			default:
				break;
			}
		}
	};
	private View progressDialogView;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_application_market);
		init();
	}

	protected void dataChange() {

		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				appInstalledList = AppUtils.getAppInfo(MyAppMarketActivity.this);
		        
		        PengDoctorApplication.appInstalledList=appInstalledList;
		        PengDoctorApplication.getPackageNameList();
		        mHandler.sendEmptyMessage(MSG_DATA_CHANGE);
			}
		});
	}

	private void init() {
		viewPager	=	(PreloadingViewPager)findViewById(R.id.view_pager_app);
		
		
		progressDialogView 	= 	findViewById(R.id.progress_dialog);
		
		
//		loading	=	(RelativeLayout)findViewById(R.id.domy_loading);
//		iv_loading	=	(ImageView)findViewById(R.id.iv_image_domy_loading);
//		iv_loading.setImageResource(R.drawable.animation_domy_loading);
//		AnimationDrawable mAnimationDrawable = (AnimationDrawable) iv_loading.getDrawable();
//		mAnimationDrawable.start();
		
		tv_app_title	=	(TextView)findViewById(R.id.tv_app_title);
		tv_app_page		=	(TextView)findViewById(R.id.tv_app_page);
		tv_app_market	=	(TextView)findViewById(R.id.tv_app_market);
		tv_app_installed=	(TextView)findViewById(R.id.tv_app_installed);
		tv_app_total	=	(TextView)findViewById(R.id.tv_app_total);
		tv_app_total.setVisibility(View.VISIBLE);
		tv_app_market.setVisibility(View.VISIBLE);
		
		adapter 	=	new PreMarketPageAdapter(MyAppMarketActivity.this, appList,mHandler);
		viewPager.setAdapter(adapter);
		
		setPreLoadingListener();
		setcharacters();
	}
	
	private void getMarketInfo() {
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
//					appList= new HiveViewService().getAppMarketList(AppConstant.APP_CLIENTKEY, AppConstant.APP_VERSION, PAGE_NO, PAGE_SIZE);
					appList= new HiveViewService().getAppMarketList(PAGE_NO, PAGE_SIZE, AppConstant.APP_VERSION);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(MyAppMarketActivity.this, "网络不给力", 1).show();
				}
				
				if(appList != null){
				
					int count;
					
					if(appList.size()%PAGE_COUNT==0&&appList.size()!=0){
						count = appList.size()/PAGE_COUNT;
					}else {
						count = appList.size()/PAGE_COUNT + 1;
					}
					if(count==5){
						isNoData = false;
						PAGE_SIZE += PAGE_NO;
					}else{
						isNoData = true;
					}
				}
				mHandler.sendEmptyMessage(MSG_GET_MARKET_APP);
			}
		});
	}
	
	/** 设置文字样式 */
	private void setcharacters() {
		tv_app_title.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_page.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_installed.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_market.setTypeface(TypefaceUtils.getStandardfFontFace());
	}
	
	/**设置预加载监听*/
	protected void setPreLoadingListener() {
		viewPager.setPreLoadingListener(new OnPreloadingListener(){
			@Override
			public void getPagerNum(int pagerNum) {
				
				if(mSelectedPageIndex < pagerNum ){
					isNext = true;
				}else{
					isNext = false;
				}
				mSelectedPageIndex = pagerNum ;
				tv_app_page.setText("第"+ mSelectedPageIndex + "/" +count+ "页");
				
				Logger.e(TAG, "第  "+mSelectedPageIndex);
				PreMarketPageAdapter adapter = (PreMarketPageAdapter) viewPager.getAdapter();
				mCurrentView = (CommonAppView) adapter.getPrimaryItem();
				
//				mCurrentView.setDefaultFocuse();
				if(isNext){
					//下一页
					mCurrentView.requestNextViewpager(SELECTED_LINE);
				}else {
					mCurrentView.requestUPViewpager(SELECTED_LINE);
				}
			}

			@Override
			public void preLoading() {
			//todo预加载数据 请求
			if(!isNoData)
				//加载全部数据
				getMarketInfo();
			}

			@Override
			public void notifyDataSetChanged(int state) {
				mViewPageScrollState = state;
				
				//SCROLL_STATE_IDLE 闲置时设置当前imageView
				if(mViewPageScrollState==ViewPager.SCROLL_STATE_IDLE){
					PreMarketPageAdapter adapter = (PreMarketPageAdapter) viewPager.getAdapter();
					mCurrentView = (CommonAppView) adapter.getPrimaryItem();
					
//					mCurrentView.setFocus();
//					mCurrentView.setImageView(mSelectedPageIndex, mVideoList);
				}
			}

			@Override
			public void loading() {
			}

			@Override
			/**
			 * 滑动前调用的方法，
			 * 默示在前一个页面滑动到后一个页面的时，在前一个页面滑动前调用的办法 */
			public void onPageScrolled() {
				PreMarketPageAdapter adapter = (PreMarketPageAdapter) viewPager.getAdapter();
				mCurrentView = (CommonAppView) adapter.getPrimaryItem();
				
				lastCommonListView = mCurrentView;
				
				//滑动前取消当前页加载
//				lastCommonListView.cancelSetImage();
//				ImageTaskManager.getInstance().shutDownNow();
				
			}
    	});
		viewPager.setCurrentItem(0);
		getMarketInfo();
	}
	
	protected void installFile(String absolutePath) {
        ////////////////////////////////////
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setAction(android.content.Intent.ACTION_VIEW);
//
//		intent.setDataAndType(Uri.fromFile(new File(AppConstant.LOCATION_PATH,
//				fileApk.getAbsolutePath().substring(fileApk.getAbsolutePath().lastIndexOf("/")+1))),"application/vnd.android.package-archive");
//		startActivity(intent);
		
		//-------  ---------//
//		AppUtils.installAppSU(fileApk.getAbsolutePath(),mHandler);
		mHandler.sendEmptyMessageDelayed(AppConstant.MSG_DIALOG_INSTALL, 500);
		AppUtils.installApp(fileApk.getAbsolutePath(),mHandler);
//		AppUtils.installSystemApp(fileApk,mHandler);
	}

	/**注册广播*/
	private void setBroadCast() {
		Logger.e(TAG, "注册广播");
         //实例化过滤器并设置要过滤的广播 
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED"); 
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addCategory("android.intent.category.LAUNCHER");
        intentFilter.addDataScheme("package");
        
         //注册广播 
        registerReceiver(installReceiver, intentFilter); 
	}


	@Override
	protected void onStart() {
		setBroadCast();
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(installReceiver);  
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		PreMarketPageAdapter adapter = (PreMarketPageAdapter) viewPager.getAdapter();
		mCurrentView = (CommonAppView) adapter.getPrimaryItem();
		
		if(event.getKeyCode() ==  KeyEvent.KEYCODE_DPAD_UP)			SELECTED_LINE=1;
		if(event.getKeyCode() ==  KeyEvent.KEYCODE_DPAD_DOWN)		SELECTED_LINE=2;
		

		if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
			if(mCurrentView.canGoLeft()==false){//加载前几页
//				return false;
			}
			
		}else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT){  //如果向左移动页面，缓存后5页的图片

//			if(mCurrentView.canGoRight()==false){
//				return true;
//			}
		}
	
		
		return super.dispatchKeyEvent(event);
	}
	
}
