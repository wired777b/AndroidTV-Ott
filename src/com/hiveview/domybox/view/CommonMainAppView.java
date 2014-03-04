package com.hiveview.domybox.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.common.listenter.ITabPage;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.utils.AnimationUtil;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.Logger;

public class CommonMainAppView extends FrameLayout implements ITabPage{
	private static final String TAG = "CommonMainAppView";
	public static String delectPackageName;
	public static AppInfoEntity delectApp;
	public PackageInfo info;
	protected Handler mHandler;
	protected boolean isMainPage=false;
	protected ArrayList<AppInfoEntity> arrAppInfo;
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
	protected Boolean isDeletedMode=false;
	public static Dialog dialog;
	
	protected OnFocusChangeListener mFocusListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				v.bringToFront();
//				((AppItemView)v).setDelecteModel(isDelecteModel);
				
//				v.startAnimation(AnimationUtil.getScaleAnimation());
				AnimationUtil.getBigAnimation(v);
				((AppItemView)v).setHasFocus(true);
				((AppItemView)v).setDeletedModelFocused(true);
				
			}else {
//				((AppItemView)v).setDelecteModel(false);
				
//				v.startAnimation(AnimationUtil.getZoomOutAnimation());
				AnimationUtil.getLittleAnimation(v);
				((AppItemView)v).setHasFocus(false);
				((AppItemView)v).setDeletedModelFocused(false);
			}
			mContainer.invalidate();
		}
	};
	
	protected OnFocusChangeListener mFixedFocusListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				v.bringToFront();
				v.startAnimation(AnimationUtil.getScaleAnimation());
//				AnimationUtil.getBigAnimation(v);
				v.setBackgroundResource(R.drawable.bg_b);
			}else {
				v.startAnimation(AnimationUtil.getZoomOutAnimation());
//				AnimationUtil.getLittleAnimation(v);
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

				v.startAnimation(AnimationUtil.getScaleAnimation());
				((AppItemView)v).setHasFocus(true);
			}else {

				
				v.startAnimation(AnimationUtil.getZoomOutAnimation());
				((AppItemView)v).setHasFocus(false);
			}
			mContainer.invalidate();
		}
	};
	
	
	public CommonMainAppView(Context context,ArrayList<AppInfoEntity> appList,int index,boolean isDeleted,Handler mHandler) {
		super(context);
		this.arrAppInfo=appList;
		mContext = context; 
		isDeletedMode=isDeleted;
		page=index;
		this.mHandler=mHandler;
		initView();
		initChildrenFocusID();
		initData(appList,index);
	}


	protected void initView() {
		
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view = (View) mLayoutInflater.inflate(R.layout.layout_app_list,null);
		
		addView(view);
		
		mContainer = (RelativeLayout) view.findViewById(R.id.common_container);
		
		
		app_item_1_line_1 	= (AppItemView) view.findViewById(R.id.app_item_1_line_1);
		app_item_1_line_1.setOnFocusChangeListener(mFocusListener);
		
		app_item_1_line_2 	= (AppItemView) view.findViewById(R.id.app_item_1_line_2);
		app_item_1_line_2.setOnFocusChangeListener(mFocusListener);
		
		
		app_item_1_line_3 	= (AppItemView) view.findViewById(R.id.app_item_1_line_3);
		app_item_1_line_3.setOnFocusChangeListener(mFocusListener);
		
		app_item_1_line_4 	= (AppItemView) view.findViewById(R.id.app_item_1_line_4);
		app_item_1_line_4.setOnFocusChangeListener(mFocusListener);
		
		
		app_item_1_line_5 	= (AppItemView) view.findViewById(R.id.app_item_1_line_5);
		app_item_1_line_5.setOnFocusChangeListener(mFocusListener);
		
		app_item_1_line_6 	= (AppItemView) view.findViewById(R.id.app_item_1_line_6);
		app_item_1_line_6.setOnFocusChangeListener(mFocusListener);
		
		//----------2-----------//
		
		app_item_2_line_1 	= (AppItemView) view.findViewById(R.id.app_item_2_line_1);
		app_item_2_line_1.setOnFocusChangeListener(mFocusListener);
		
		app_item_2_line_2 	= (AppItemView) view.findViewById(R.id.app_item_2_line_2);
		app_item_2_line_2.setOnFocusChangeListener(mFocusListener);
		
		
		app_item_2_line_3 	= (AppItemView) view.findViewById(R.id.app_item_2_line_3);
		app_item_2_line_3.setOnFocusChangeListener(mFocusListener);
		
		app_item_2_line_4 	= (AppItemView) view.findViewById(R.id.app_item_2_line_4);
		app_item_2_line_4.setOnFocusChangeListener(mFocusListener);
		
		
		app_item_2_line_5 	= (AppItemView) view.findViewById(R.id.app_item_2_line_5);
		app_item_2_line_5.setOnFocusChangeListener(mFocusListener);
		
		app_item_2_line_6 	= (AppItemView) view.findViewById(R.id.app_item_2_line_6);
		app_item_2_line_6.setOnFocusChangeListener(mFocusListener);
		
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
	protected void initData(ArrayList<AppInfoEntity> arrAppInfo, int index) {
		int size=arrAppInfo.size();
		for (int i = 0; i < 12; i++) {
			if(size+4 >12*index+i){
				handView(i,arrAppInfo.get(12*index-4+i));
			}else {
				hideItem(i);
			}
		}
		
	}

	protected void hideItem(int i) {
		mViewList.get(i).setVisibility(View.GONE);
		mViewList.get(i).setFocusable(false);
	}


	protected void handView(int i, AppInfoEntity appInfoEntity) {
		AppItemView view =mViewList.get(i);
		view.setVisibility(View.VISIBLE);
		view.setFocusable(true);
		view.setBackgroundResource(appInfoEntity.getAppIcon());
		view.setAppName(appInfoEntity.getAppName());
		view.setDeletedModel(isDeletedMode);
		view.setOnClickListener(new MyOnClickListener(arrAppInfo.get(12*page+i-4)));

	}
	
	@Override
	public void setDefaultFocuse(){
		app_item_1_line_1.requestFocus();
	}


	@Override
	public boolean requestDefaultFocusLeft() {

		return false;
	}


	@Override
	public boolean requestDefaultFocusRight() {

		return false;
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
	
	
	class MyOnClickListener implements OnClickListener{
		
		private AppInfoEntity appInfoEntity;

		MyOnClickListener(AppInfoEntity entity){
			appInfoEntity=entity;
		}

		@Override
		public void onClick(View v) {
			if(isDeletedMode){
				if (!((Activity)mContext).isFinishing()) {  
					cancelUninstall(appInfoEntity);
					CommonMainAppView.delectApp=appInfoEntity;
			    } 
			}else {
				//开启 	app
				AppUtils.openApp(appInfoEntity.getPackageName(),mContext);
			}
		}
	}


	public void delecteModekChange(Boolean model) {
		isDeletedMode =model;
		initData(arrAppInfo, page);
	}
	
	
	//取消 卸载
	public void cancelUninstall(final AppInfoEntity appInfoEntity) {
		delectPackageName	=	appInfoEntity.getPackageName();
		delectApp	=	appInfoEntity;
		
		DialogApp.getInstance(mContext).showuUninstallDialog(appInfoEntity, mHandler);
		DialogApp.getInstance(mContext).getAppInfo(appInfoEntity, mHandler);
		
		
	}

	//开始卸载apk
	protected void startUnInstall(final Handler mHandler) {
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20; i++) {
					mHandler.sendEmptyMessageDelayed(AppConstant.MSG_DIALOG_UNINSTALL, 800);
				}
			}
		});
	}


	public void requestNextViewpager(int sELECTED_LINE) {
		Logger.e(TAG, "下一页");
		switch (sELECTED_LINE) {
		case 2:
			if(app_item_2_line_1.isShown()){
				app_item_2_line_1.requestFocus();
			}else {
				requestLastFocus();
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
		int lastPosition=(arrAppInfo.size()+4)%12;
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
