package com.hiveview.domybox.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.activity.adapter.ViewPageAdapter;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.common.listenter.AppUninstllListener;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.Logger;
import com.hiveview.domybox.utils.TypefaceUtils;
import com.hiveview.domybox.view.CommonMainAppView;
import com.hiveview.domybox.view.DialogApp;
import com.hiveview.domybox.view.MainPageAppView;

@SuppressLint("HandlerLeak")
public class ApplicationActivity extends BaseActivity{

	protected static final int MSG_GET_LOCATIONG_APP 	= 1001;
	protected static final int MSG_UNINSTALL_UPDATA 	= 1002;
	protected static final int PAGE_COUNT = 12;			//每页的个数
	protected static final String TAG = "ApplicationActivity";
	private LinearLayout ll_progressBar_application;
	private TextView tv_app_page;
	private TextView tv_app_installed;
	private ViewPager mViewPager;
	private ArrayList<CommonMainAppView> mViewList=new ArrayList<CommonMainAppView>();
	private ViewPageAdapter mAdapter;
	private View progressDialogView;
	protected int mSelectedPageIndex;	
	protected ArrayList<AppInfoEntity> appList;	//已安装程序集合（非系统）
	protected int count;	//页数
	protected MainPageAppView mainPageAppView;
	private Boolean isDeletedModel	=	false;
	private TextView tv_app_title;
	/**选择的行*/
	private int SELECTED_LINE = 1;
	
	protected boolean isNext;
	protected CommonMainAppView mCurrentView;

	private BroadcastReceiver unInstallReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			 //接收卸载广播  
			
	        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {   
	            String packageName = intent.getDataString();   
	            
	            appList= new ArrayList<AppInfoEntity>();
	            getAppInstalledInfo();
	            DialogApp.getInstance(ApplicationActivity.this).complete();
	            
	            mHandler.removeMessages(AppConstant.MSG_DIALOG_UNINSTALL);
	        }
		}
	};
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppConstant.MSG_DIALOG_UNINSTALL:
				int progress = DialogApp.getInstance(ApplicationActivity.this).progressBar1.getProgress();
				progress = progress + 5;
				
				DialogApp.getInstance(ApplicationActivity.this).progressBar1.setProgress(progress);
				if(DialogApp.getInstance(ApplicationActivity.this).progressBar1.getProgress()==100){
					mHandler.removeMessages(AppConstant.MSG_DIALOG_UNINSTALL);
					DialogApp.getInstance(ApplicationActivity.this).unInstallDefeated();
				}else{
					mHandler.sendEmptyMessageDelayed(AppConstant.MSG_DIALOG_UNINSTALL, 1000);
				}
				
				break;

			case MSG_GET_LOCATIONG_APP:
				if(appList!=null){
					progressDialogView.setVisibility(View.GONE);
					PengDoctorApplication.appInstalledList=appList;
					ll_progressBar_application.setVisibility(View.GONE);
					mViewPager.setVisibility(View.VISIBLE);
					
					if((appList.size()+4)%PAGE_COUNT==0){
						count = (appList.size()+4)/PAGE_COUNT;
					}else {
						count = (appList.size()+4)/PAGE_COUNT + 1;
					}
					mViewList.clear();
					for(int i=0;i<count;i++){
						if(i==0){
							mainPageAppView =new MainPageAppView(ApplicationActivity.this, appList, i, isDeletedModel,mHandler);
							mainPageAppView.setUninstllListener(new AppUninstllListener() {
								@Override
								public void onComplete(Boolean isDeletedModel) {
									//删除模式改变
									UnInstallModeChanged(isDeletedModel);
								}
							});
							mViewList.add(mainPageAppView);
						}else {
							mViewList.add(new CommonMainAppView(ApplicationActivity.this, appList, i,isDeletedModel,mHandler));
						}
					}
					
					tv_app_installed.setText("共安装"+appList.size()+"个");
					tv_app_page.setText("第"+ "1/" +count+ "页");
					mAdapter.notifyDataSetChanged();
//					mViewPager.requestFocus();
					
				}else{
					ll_progressBar_application.setVisibility(View.GONE);
					mViewPager.setVisibility(View.VISIBLE);
					tv_app_installed.setText("已安装"+0+"个");
					tv_app_page.setText("第"+ "1/" + 1+"页");
				}
				break;
			case AppConstant.MSG_GET_APPINFO:
				//卸载
				@SuppressWarnings("unchecked")
				AppMarketEntity arrEntity = (AppMarketEntity)msg.obj;
				
				DialogApp.getInstance(ApplicationActivity.this).setInfo(mHandler,arrEntity);
				break;
				
			case AppConstant.UNINSTALL_SUCCEE:
				DialogApp.getInstance(ApplicationActivity.this).complete();
				break;
				
			case AppConstant.UNINSTALL_DEFEATE:
				DialogApp.getInstance(ApplicationActivity.this).unInstallDefeated();
				break;
			default:
				break;
			}
		}
		
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_application);
		init();
	}

	private void init() {
		tv_app_title		= (TextView)findViewById(R.id.tv_app_title);
		tv_app_page 		= (TextView)findViewById(R.id.tv_app_page);
		tv_app_installed 	= (TextView)findViewById(R.id.tv_app_installed);
		
		mViewPager 			=  (ViewPager) findViewById(R.id.view_pager_app);
		
		progressDialogView 	= 	findViewById(R.id.progress_dialog);
		
		
		ll_progressBar_application =  (LinearLayout) findViewById(R.id.ll_progressBar_application);
		
		mAdapter = new ViewPageAdapter(mViewList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				
				tv_app_page.setText("第"+ mSelectedPageIndex + "/" +count+ "页");

				/**viewpager当前页*/
				if(mSelectedPageIndex < arg0+1 ){
					isNext = true;
				}else{
					isNext = false;
				}
				mSelectedPageIndex = arg0+1 ;
				tv_app_page.setText("第"+ mSelectedPageIndex + "/" +count+ "页");
				
					ViewPageAdapter adapter = (ViewPageAdapter) mViewPager.getAdapter();
					mCurrentView = (CommonMainAppView) adapter.getPrimaryItem();
					
					if(isNext){
						//下一页
						mCurrentView.requestNextViewpager(SELECTED_LINE);
					}else {
						mCurrentView.requestUPViewpager(SELECTED_LINE);
					}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		getAppInstalledInfo();
		setcharacters();
	}
	
	private void UnInstallModeChanged(Boolean isDeleted) {
		isDeletedModel = isDeleted;
		mViewList.size();
		for (CommonMainAppView view : mViewList) {
			view.delecteModekChange(isDeletedModel);
		}
		
	}

	// 获取已经安装程序信息
	private void getAppInstalledInfo() {
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
			   appList = AppUtils.getAppInfo(ApplicationActivity.this);
		       mHandler.sendEmptyMessage(MSG_GET_LOCATIONG_APP);
			}
		});
		
	}


	/**注册广播*/
	private void setBroadCast() {
		Logger.i("广播", "注册 ---卸载广播");
         //实例化过滤器并设置要过滤的广播 
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED"); 
        intentFilter.addDataScheme("package");
        
         //注册广播 
        registerReceiver(unInstallReceiver, intentFilter); 
	}
	
	
	@Override
	protected void onStart() {
		setBroadCast();
		super.onStart();
	} 
	
	@Override
	protected void onStop() {
		unregisterReceiver(unInstallReceiver);  
		super.onStop();
	}
	
	/** 设置文字样式 */
	private void setcharacters() {
		tv_app_title.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_page.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_installed.setTypeface(TypefaceUtils.getStandardfFontFace());
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		progressDialogView.setVisibility(View.VISIBLE);
		getAppInstalledInfo();
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		 if (KeyEvent.ACTION_DOWN == event.getAction()) {

			if(event.getKeyCode() ==  KeyEvent.KEYCODE_DPAD_UP)			SELECTED_LINE	=1;
			if(event.getKeyCode() ==  KeyEvent.KEYCODE_DPAD_DOWN)		SELECTED_LINE	=2;
			 
			
			return super.dispatchKeyEvent(event);
		}else {
			return super.dispatchKeyEvent(event);
		}
	}
	
	
}
