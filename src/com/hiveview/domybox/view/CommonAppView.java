package com.hiveview.domybox.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.common.listenter.ITabPage;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.utils.AnimationUtil;
import com.hiveview.domybox.utils.FileUtils;
import com.hiveview.domybox.utils.Logger;
import com.hiveview.domybox.utils.ToastUtil;

public class CommonAppView extends FrameLayout implements ITabPage{

	
	private static final String TAG = "CommonAppView";
	protected ArrayList<AppMarketEntity> arrAppInfo;
	protected Context mContext;
	protected View view;
	protected int page;
	protected ArrayList<AppItemView> mViewList;
	protected RelativeLayout mContainer;
	protected AppItemView app_item_1_line_1;
	protected AppItemView app_item_1_line_2;
	protected AppItemView app_item_1_line_3;
	protected AppItemView app_item_1_line_4;
	protected AppItemView app_item_1_line_5;
	protected AppItemView app_item_1_line_6;
	protected AppItemView app_item_2_line_1;
	protected AppItemView app_item_2_line_2;
	protected AppItemView app_item_2_line_3;
	protected AppItemView app_item_2_line_4;
	protected AppItemView app_item_2_line_5;
	protected AppItemView app_item_2_line_6;
	public int position;	//焦点的位置
	
	protected Boolean isDelecteModel=false;
	public PackageInfo info;
	private Handler mHandler;
	public static AppMarketEntity currentEntity;
	
	protected OnFocusChangeListener mFocusListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				v.bringToFront();
				((AppItemView)v).setDeletedModel(isDelecteModel);
				AnimationUtil.getBigAnimation(v);
				((AppItemView)v).setHasFocus(true);
				
			}else {
				((AppItemView)v).setDeletedModel(false);
				AnimationUtil.getLittleAnimation(v);
				((AppItemView)v).setHasFocus(false);
			}
			mContainer.invalidate();
		}
	};
	
	protected OnFocusChangeListener mFixedFocusListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				v.bringToFront();
				AnimationUtil.getBigAnimation(v);
				v.setBackgroundResource(R.drawable.bg_b);
			}else {
				AnimationUtil.getLittleAnimation(v);
				v.setBackgroundResource(R.drawable.bg_a);
			}
			mContainer.invalidate();
		}
	};
	
	protected OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			if(hasFocus){
				v.bringToFront();
				((AppItemView)v).setDeletedModel(isDelecteModel);
				AnimationUtil.getBigAnimation(v);
				((AppItemView)v).setHasFocus(true);
			}else {
				((AppItemView)v).setDeletedModel(false);
				AnimationUtil.getLittleAnimation(v);
				((AppItemView)v).setHasFocus(false);
			}
			mContainer.invalidate();
		}
	};
	
	
	
	public CommonAppView(Context context,ArrayList<AppMarketEntity> arrAppInfo,int index, Handler mHandler) {
		super(context);
		this.arrAppInfo = arrAppInfo;
		mContext = context; 
		page=index;
		this.mHandler=mHandler;
		PengDoctorApplication.getPackageNameList();
		initView();
		initChildrenFocusID();
		initData(arrAppInfo,index);
	}



	protected void initView() {
		
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view = (View) mLayoutInflater.inflate(R.layout.layout_app_list,null);
		
		addView(view);
		
		mContainer = (RelativeLayout) view.findViewById(R.id.common_container);
		
		
		app_item_1_line_1 	= (AppItemView) view.findViewById(R.id.app_item_1_line_1);
		app_item_1_line_1.setOnFocusChangeListener(new MyFocusListener(1));
		
		app_item_1_line_2 	= (AppItemView) view.findViewById(R.id.app_item_1_line_2);
		app_item_1_line_2.setOnFocusChangeListener(new MyFocusListener(2));
		
		
		
		app_item_1_line_3 	= (AppItemView) view.findViewById(R.id.app_item_1_line_3);
		app_item_1_line_3.setOnFocusChangeListener(new MyFocusListener(3));
		
		app_item_1_line_4 	= (AppItemView) view.findViewById(R.id.app_item_1_line_4);
		app_item_1_line_4.setOnFocusChangeListener(new MyFocusListener(4));
		
		
		app_item_1_line_5 	= (AppItemView) view.findViewById(R.id.app_item_1_line_5);
		app_item_1_line_5.setOnFocusChangeListener(new MyFocusListener(5));
		
		app_item_1_line_6 	= (AppItemView) view.findViewById(R.id.app_item_1_line_6);
		app_item_1_line_6.setOnFocusChangeListener(new MyFocusListener(6));
		
		//----------2-----------//
		
		app_item_2_line_1 	= (AppItemView) view.findViewById(R.id.app_item_2_line_1);
		app_item_2_line_1.setOnFocusChangeListener(new MyFocusListener(7));
		
		app_item_2_line_2 	= (AppItemView) view.findViewById(R.id.app_item_2_line_2);
		app_item_2_line_2.setOnFocusChangeListener(new MyFocusListener(8));
		
		
		app_item_2_line_3 	= (AppItemView) view.findViewById(R.id.app_item_2_line_3);
		app_item_2_line_3.setOnFocusChangeListener(new MyFocusListener(9));
		
		app_item_2_line_4 	= (AppItemView) view.findViewById(R.id.app_item_2_line_4);
		app_item_2_line_4.setOnFocusChangeListener(new MyFocusListener(10));
		
		
		app_item_2_line_5 	= (AppItemView) view.findViewById(R.id.app_item_2_line_5);
		app_item_2_line_5.setOnFocusChangeListener(new MyFocusListener(11));
		
		app_item_2_line_6 	= (AppItemView) view.findViewById(R.id.app_item_2_line_6);
		app_item_2_line_6.setOnFocusChangeListener(new MyFocusListener(12));
		
		mViewList = new ArrayList<AppItemView>();
		mViewList.add(app_item_1_line_1);
		mViewList.add(app_item_1_line_2);
		mViewList.add(app_item_1_line_3);
		mViewList.add(app_item_1_line_4);
		mViewList.add(app_item_1_line_5);
		mViewList.add(app_item_1_line_6);
		
		mViewList.add(app_item_2_line_1);
		mViewList.add(app_item_2_line_2);
		mViewList.add(app_item_2_line_3);
		mViewList.add(app_item_2_line_4);
		mViewList.add(app_item_2_line_5);
		mViewList.add(app_item_2_line_6);
	}

	protected void initChildrenFocusID() {
		app_item_1_line_1.setNextFocusLeftId(View.NO_ID);
		app_item_1_line_1.setNextFocusUpId(View.NO_ID);
		app_item_1_line_1.setNextFocusRightId(R.id.app_item_1_line_2);
		app_item_1_line_1.setNextFocusDownId(R.id.app_item_2_line_1);
		
		app_item_1_line_2.setNextFocusLeftId(R.id.app_item_1_line_1);
		app_item_1_line_2.setNextFocusUpId(View.NO_ID);
		app_item_1_line_2.setNextFocusRightId(R.id.app_item_1_line_3);
		app_item_1_line_2.setNextFocusDownId(R.id.app_item_2_line_2);
		
		app_item_1_line_3.setNextFocusLeftId(R.id.app_item_1_line_2);
		app_item_1_line_3.setNextFocusUpId(View.NO_ID);
		app_item_1_line_3.setNextFocusRightId(R.id.app_item_1_line_4);
		app_item_1_line_3.setNextFocusDownId(R.id.app_item_2_line_3);
		
		app_item_1_line_4.setNextFocusLeftId(R.id.app_item_1_line_3);
		app_item_1_line_4.setNextFocusUpId(View.NO_ID);
		app_item_1_line_4.setNextFocusRightId(R.id.app_item_1_line_5);
		app_item_1_line_4.setNextFocusDownId(R.id.app_item_2_line_4);
		
		app_item_1_line_5.setNextFocusLeftId(R.id.app_item_1_line_4);
		app_item_1_line_5.setNextFocusUpId(View.NO_ID);
		app_item_1_line_5.setNextFocusRightId(R.id.app_item_1_line_6);
		app_item_1_line_5.setNextFocusDownId(R.id.app_item_2_line_5);
		
		app_item_1_line_6.setNextFocusLeftId(R.id.app_item_1_line_5);
		app_item_1_line_6.setNextFocusUpId(View.NO_ID);
		app_item_1_line_6.setNextFocusRightId(View.NO_ID);
		app_item_1_line_6.setNextFocusDownId(R.id.app_item_2_line_6);
		
		/**********------2-----*********/
		app_item_2_line_1.setNextFocusLeftId(View.NO_ID);
		app_item_2_line_1.setNextFocusUpId(R.id.app_item_1_line_1);
		app_item_2_line_1.setNextFocusRightId(R.id.app_item_2_line_2);
		app_item_2_line_1.setNextFocusDownId(View.NO_ID);
		
		app_item_2_line_2.setNextFocusLeftId(R.id.app_item_2_line_1);
		app_item_2_line_2.setNextFocusUpId(R.id.app_item_1_line_2);
		app_item_2_line_2.setNextFocusRightId(R.id.app_item_2_line_3);
		app_item_2_line_2.setNextFocusDownId(View.NO_ID);
		
		app_item_2_line_3.setNextFocusLeftId(R.id.app_item_2_line_2);
		app_item_2_line_3.setNextFocusUpId(R.id.app_item_1_line_3);
		app_item_2_line_3.setNextFocusRightId(R.id.app_item_2_line_4);
		app_item_2_line_3.setNextFocusDownId(View.NO_ID);
		
		app_item_2_line_4.setNextFocusLeftId(R.id.app_item_2_line_3);
		app_item_2_line_4.setNextFocusUpId(R.id.app_item_1_line_4);
		app_item_2_line_4.setNextFocusRightId(R.id.app_item_2_line_5);
		app_item_2_line_4.setNextFocusDownId(View.NO_ID);
		
		app_item_2_line_5.setNextFocusLeftId(R.id.app_item_2_line_4);
		app_item_2_line_5.setNextFocusUpId(R.id.app_item_1_line_5);
		app_item_2_line_5.setNextFocusRightId(R.id.app_item_2_line_6);
		app_item_2_line_5.setNextFocusDownId(View.NO_ID);
		
		app_item_2_line_6.setNextFocusLeftId(R.id.app_item_2_line_5);
		app_item_2_line_6.setNextFocusUpId(R.id.app_item_1_line_6);
		app_item_2_line_6.setNextFocusRightId(View.NO_ID);
		app_item_2_line_6.setNextFocusDownId(View.NO_ID);
		
	}

	//index 页数
	protected void initData(ArrayList<AppMarketEntity> arrAppInfo, int index) {
		int size=arrAppInfo.size();
		for (int i = 0; i < 12; i++) {
			if(size >12*index+i){
				handView(i,arrAppInfo.get(12*index+i));
			}else {
				hideItem(i);
			}
		}
	}

	protected void hideItem(int i) {
		mViewList.get(i).setVisibility(View.GONE);
		mViewList.get(i).setFocusable(false);
	}


	protected void handView(int i, AppMarketEntity appMarketEntity) {
		AppItemView view =mViewList.get(i);
		view.setVisibility(View.VISIBLE);
		view.setFocusable(true);
		view.setImageUrl(appMarketEntity.getAppIcon(),mHandler);
		view.setAppName(appMarketEntity.getAppName());
		view.setTag(arrAppInfo.get(12*page+i));
		
		Logger.e(TAG, PengDoctorApplication.appinfo.size()+"");
		if(PengDoctorApplication.appinfo.containsKey(appMarketEntity.getBundlerId()))view.setInstalled(true);
		
		view.setOnClickListener(new MyOnClickListener(arrAppInfo.get(12*page+i)));

	}
	
	public void setDefaultFocuse(){
		app_item_1_line_1.requestFocus();
	}


	@Override
	public boolean requestDefaultFocusLeft() {
		return app_item_1_line_1.requestFocus();
	}


	@Override
	public boolean requestDefaultFocusRight() {
		return app_item_2_line_1.requestFocus();
	}


	@Override
	public boolean requestDefaultFocusBottom() {
		return false;
	}


	@Override
	public boolean requestDefaultFocusUp() {
		return false;
	}


	@Override
	public boolean canGoUp() {
		View view = this.findFocus();

		if (null != view && View.NO_ID != view.getNextFocusUpId()) {
			return true;
		}
		return false;
	}


	@Override
	public boolean canGoLeft() {
		View view = this.findFocus();
		if (null != view && View.NO_ID != view.getNextFocusLeftId()) {
			return true;
		}
		return false;
	}


	@Override
	public boolean canGoDown() {
		View view = this.findFocus();

		if (null != view && View.NO_ID != view.getNextFocusDownId()) {
			return true;
		}
		return false;
	}


	@Override
	public boolean canGoRight() {
		View view = this.findFocus();

		if (null != view && View.NO_ID != view.getNextFocusRightId()) {
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (KeyEvent.ACTION_DOWN == event.getAction()) {
			if(event.getKeyCode()==KeyEvent.KEYCODE_DPAD_DOWN){
	//			if(!canGoDown()){
	//				requestLastFocus();
	//			}
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	
	class MyOnClickListener implements OnClickListener{
		AppMarketEntity appMarketEntity;
		
		MyOnClickListener(AppMarketEntity entity){
			appMarketEntity = entity;
			//当前 view
//			currentEntity	=	appMarketEntity;
		}

		@Override
		public void onClick(View v) {
			AppMarketEntity bean =(AppMarketEntity) v.getTag();
			AppInfoEntity entity = PengDoctorApplication.isInstalled(bean.getBundlerId());
			if(entity!=null){
				//已经安装
				if((entity.getVersionCode()+"").equals(bean.getVersionNo())){
					//无新版本
					showOpenDialog(bean);
				}else {
					//有新版本
					showUpDataDialog(bean);
				}
			}else {
				//未安装
				//判断是否需要升级//可用空间大于150M
				 if(FileUtils.isDownload()){
//					 new MyFileObserver(AppConstant.LOCATION_PATH);
					 installApp(bean);
				 }else {
					ToastUtil.showMessageView(mContext, "空间不足", 1);
				}
			}
		}
	}



	/**
	 * 显示卸载Dialog 打开---已经安装--无最新版本
	 * @param AppMarketEntity 
	 * @param mHandler2 
	 */
	public void showOpenDialog(final AppMarketEntity AppMarketEntity ){
		final DialogAppMarket dialogUtils =DialogAppMarket.getInstance(mContext);
		dialogUtils.setInfo(AppMarketEntity, mHandler);
		dialogUtils.showOpenDialog(AppMarketEntity,mHandler);
	}
	

	

	//安装app
	public void installApp(AppMarketEntity appMarketEntity) {
		final DialogAppMarket dialogUtils =DialogAppMarket.getInstance(mContext);
		dialogUtils.setInfo(appMarketEntity,mHandler);
		
		dialogUtils.installDialog(appMarketEntity,mHandler);
	}


	/** 升级dialog 
	 * 立即升级、稍后升级------------有最新版本
	 * */
	public void showUpDataDialog(AppMarketEntity AppMarketEntity ) {
		final DialogAppMarket dialogUtils =DialogAppMarket.getInstance(mContext);
		dialogUtils.setInfo(AppMarketEntity, mHandler);
		dialogUtils.showUpDataDialog(AppMarketEntity,mHandler);
	}


	//	刷新
	public void refresh() {
		for (int i = 0; i < 12; i++) {
			if(page >12*page+i){
				handView(i,arrAppInfo.get(12*page+i));
			}else {
				hideItem(i);
			}
		}
	}
	
	public class MyFocusListener implements OnFocusChangeListener{
		private int pos;

		public MyFocusListener(int position) {
			this.pos=position;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){ 
				position=pos;
				currentEntity	=	arrAppInfo.get(pos-1);
				v.bringToFront();
//				v.startAnimation(AnimationUtil.getScaleAnimation());
				AnimationUtil.getBigAnimation(v);
				((AppItemView)v).setHasFocus(true);
				
			}else {
//				v.startAnimation(AnimationUtil.getZoomOutAnimation());
				AnimationUtil.getLittleAnimation(v);
				((AppItemView)v).setHasFocus(false);
			}
			mContainer.invalidate();
		}
		
	} 
	
	public void setFocus() {
		Logger.e(TAG, "请求默认焦点"+position);
		switch (position) {
		case 1:
			app_item_1_line_1.requestFocus();
			break;
		case 2:
			app_item_1_line_2.requestFocus();
			break;
		case 3:
			app_item_1_line_3.requestFocus();
			break;
		case 4:
			app_item_1_line_4.requestFocus();
			break;
		case 5:
			app_item_1_line_5.requestFocus();
			break;
		case 6:
			app_item_1_line_6.requestFocus();
			break;
			
		case 7:
			app_item_2_line_1.requestFocus();
			break;
		case 8:
			app_item_2_line_2.requestFocus();
			break;
		case 9:
			app_item_2_line_3.requestFocus();
			break;
		case 10:
			app_item_2_line_4.requestFocus();
			break;
		case 11:
			app_item_2_line_5.requestFocus();
			break;
		case 12:
			app_item_2_line_6.requestFocus();
			break;
		default:
			break;
		}
		
	}

	public void setDataChange() {
		PengDoctorApplication.getPackageNameList();
		initData(arrAppInfo,page);
		setFocus();
	}



	public void requestNextViewpager(int sELECTED_LINE) {
		Logger.e(TAG, "下一页");
		switch (sELECTED_LINE) {
		case 2:
			if(app_item_2_line_1.isShown()){
				app_item_2_line_1.requestFocus();
			}else {
				app_item_1_line_1.requestFocus();
//				requestLastFocus();
			}
			break;
		default:
			app_item_1_line_1.requestFocus();
			break;
		}
	}



	public void requestUPViewpager(int sELECTED_LINE) {
		Logger.e(TAG, "上一页");
		switch (sELECTED_LINE) {
		case 2:
			app_item_2_line_6.requestFocus();
			break;
		default:
			app_item_1_line_6.requestFocus();
			break;
		}
		
	}
	
	private void requestLastFocus() {
		int lastPosition=arrAppInfo.size()%12;
		switch (lastPosition) {
		case 2:
			app_item_1_line_2.requestFocus();
			break;
		case 3:
			app_item_1_line_3.requestFocus();
			break;
		case 4:
			app_item_1_line_4.requestFocus();
			break;
		case 5:
			app_item_1_line_5.requestFocus();
			break;
		case 6:
			app_item_1_line_6.requestFocus();
			break;
		case 7:
			app_item_2_line_1.requestFocus();
			break;
		case 8:
			app_item_2_line_2.requestFocus();
			break;
		case 9:
			app_item_2_line_3.requestFocus();
			break;
		case 10:
			app_item_2_line_4.requestFocus();
			break;
		case 11:
			app_item_2_line_5.requestFocus();
			break;
		case 12:
			app_item_2_line_6.requestFocus();
			break;
		default:
			break;
		}
		
	}
	
}
