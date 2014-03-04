package com.hiveview.domybox.view;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hiveview.domybox.R;
import com.hiveview.domybox.activity.MainActivity;
import com.hiveview.domybox.activity.MyAppMarketActivity;
import com.hiveview.domybox.common.listenter.AppUninstllListener;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.utils.ToastUtil;

public class MainPageAppView extends CommonMainAppView {

	private LinearLayout app_item_mark;
	private LinearLayout app_item_unload;
	private LinearLayout app_item_multi_media;
	private ImageView iv_app_unload;
	public PackageInfo info;
	private AppUninstllListener delectedInterface;
	
	/**
	 * @param index 当前页
	 * @param isMain 是否为首页
	 * */
	public MainPageAppView(Context context, ArrayList<AppInfoEntity> arrAppInfo, int index, boolean isDelecteModel,Handler mHandler) {
		super(context, arrAppInfo, index, isDelecteModel,mHandler);
//		this.arrAppInfo=arrAppInfo;
//		isMainPage=isMain;
//		page=index;
//		initView();
//		initChildrenFocusID();
//		initData(arrAppInfo,index);
		setDefaultFocuse();
	}
	
	@Override
	protected void initView() {
		
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view = (View) mLayoutInflater.inflate(R.layout.layout_app_main_list,null);
		
		addView(view);
		
		mContainer		=	(RelativeLayout)findViewById(R.id.common_container);
		
		app_item_mark	=	(LinearLayout)findViewById(R.id.app_item_mark);
		
		app_item_mark.setOnFocusChangeListener(mFixedFocusListener);
		
		app_item_unload	=	(LinearLayout)findViewById(R.id.app_item_unload);
		iv_app_unload	=	(ImageView)findViewById(R.id.iv_app_unload);
		
		if(isDeletedMode){
			iv_app_unload.setBackgroundResource(R.drawable.iv_app_cancel);
		}else {
			iv_app_unload.setBackgroundResource(R.drawable.iv_app_unload);
		}
		
		app_item_unload.setOnFocusChangeListener(mFixedFocusListener);
		
		
		app_item_multi_media	=	(LinearLayout)findViewById(R.id.app_item_multi_media);
		app_item_multi_media.setOnFocusChangeListener(mFixedFocusListener);
		
		
		app_item_1_line_3 	= (AppItemView) view.findViewById(R.id.app_item_1_line_3);
		app_item_1_line_3.setOnFocusChangeListener(mFocusListener);
		
		app_item_1_line_4 	= (AppItemView) view.findViewById(R.id.app_item_1_line_4);
		app_item_1_line_4.setOnFocusChangeListener(mFocusListener);
		
		
		app_item_1_line_5 	= (AppItemView) view.findViewById(R.id.app_item_1_line_5);
		app_item_1_line_5.setOnFocusChangeListener(mFocusListener);
		
		app_item_1_line_6 	= (AppItemView) view.findViewById(R.id.app_item_1_line_6);
		app_item_1_line_6.setOnFocusChangeListener(mFocusListener);
		
		//----------2-----------//
		
		app_item_2_line_3 	= (AppItemView) view.findViewById(R.id.app_item_2_line_3);
		app_item_2_line_3.setOnFocusChangeListener(mFocusListener);
		
		app_item_2_line_4 	= (AppItemView) view.findViewById(R.id.app_item_2_line_4);
		app_item_2_line_4.setOnFocusChangeListener(mFocusListener);
		
		
		app_item_2_line_5 	= (AppItemView) view.findViewById(R.id.app_item_2_line_5);
		app_item_2_line_5.setOnFocusChangeListener(mFocusListener);
		
		app_item_2_line_6 	= (AppItemView) view.findViewById(R.id.app_item_2_line_6);
		app_item_2_line_6.setOnFocusChangeListener(mFocusListener);
		
		mViewList = new ArrayList<AppItemView>();
		mViewList.add(app_item_1_line_3);
		mViewList.add(app_item_1_line_4);
		mViewList.add(app_item_1_line_5);
		mViewList.add(app_item_1_line_6);
		
		mViewList.add(app_item_2_line_3);
		mViewList.add(app_item_2_line_4);
		mViewList.add(app_item_2_line_5);
		mViewList.add(app_item_2_line_6);
		
		setOnclick();
		
	}
	

	private void setOnclick() {
		//应用市场
		app_item_mark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
				
				mContext.startActivity(new Intent(mContext, MyAppMarketActivity.class));
			}
		});
		
		//卸载
		app_item_unload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isDeletedMode){
					iv_app_unload.setBackgroundResource(R.drawable.iv_app_unload);
					isDeletedMode=false;
				}else {
					iv_app_unload.setBackgroundResource(R.drawable.iv_app_cancel);
					isDeletedMode=true;
				}
				delecteModelChange(isDeletedMode);
			}
		});
		
		//多媒体
		app_item_multi_media.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent mIntent = new Intent();
					ComponentName comp = new ComponentName(
					        "com.amlogic.mediacenter",
					        "com.amlogic.mediacenter.AmlMediaCenter");
					mIntent.setComponent(comp);
					mIntent.setAction("android.intent.action.VIEW");
					mContext.startActivity(mIntent);
				} catch (Exception e) {
					ToastUtil.showMessageView(mContext, "  多媒体 组件未安装！ ", 3);
					e.printStackTrace();
				}
			}
		});
		
	}
	
	
	
	
	protected void delecteModelChange(Boolean isDelecteModel) {
		delectedInterface.onComplete(isDelecteModel);
	}

	@Override
	protected void hideItem(int i) {
		mViewList.get(i).setVisibility(View.GONE);
		mViewList.get(i).setFocusable(false);
	}
	
	@Override
	protected void initChildrenFocusID() {
		app_item_mark.setNextFocusLeftId(View.NO_ID);
		app_item_mark.setNextFocusUpId(View.NO_ID);
		app_item_mark.setNextFocusRightId(R.id.app_item_1_line_3);
		app_item_mark.setNextFocusDownId(R.id.app_item_multi_media);
		
		
		app_item_1_line_3.setNextFocusLeftId(R.id.app_item_mark);
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
		app_item_multi_media.setNextFocusLeftId(View.NO_ID);
		app_item_multi_media.setNextFocusUpId(R.id.app_item_mark);
		app_item_multi_media.setNextFocusRightId(R.id.app_item_unload);
		app_item_multi_media.setNextFocusDownId(View.NO_ID);
		
		app_item_unload.setNextFocusLeftId(R.id.app_item_multi_media);
		app_item_unload.setNextFocusUpId(R.id.app_item_mark);
		app_item_unload.setNextFocusRightId(R.id.app_item_2_line_3);
		app_item_unload.setNextFocusDownId(View.NO_ID);
		
		app_item_2_line_3.setNextFocusLeftId(R.id.app_item_unload);
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
	
	@Override
	protected void initData(ArrayList<AppInfoEntity> arrAppInfo, int index) {
		int size=arrAppInfo.size();
		if(index==0){
			//首页显示
			for (int i = 0; i < 8; i++) {
				if(size >i){
					handView(i,arrAppInfo.get(i));
				}else {
					hideItem(i);
				}
			}
		}
	}
	
	@Override
	protected void handView(int i, AppInfoEntity appInfoEntity) {
		AppItemView view =	mViewList.get(i);
		view.setVisibility(View.VISIBLE);
		view.setFocusable(true);
		view.setBackgroundResource(appInfoEntity.getAppIcon());
		view.setAppName(appInfoEntity.getAppName());
		view.setDeletedModel(isDeletedMode);
		view.setOnClickListener(new MyOnClickListener(arrAppInfo.get(i)));
	}
	
	@Override
	public void setDefaultFocuse() {
		app_item_mark.requestFocus();
	}
	
	
	@Override
	public boolean requestDefaultFocusLeft() {
		return app_item_mark.requestFocus();
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
	
	
	/**获取Key接口，通过接口回调，回去按键信息*/
	public void setUninstllListener(AppUninstllListener delectedInterface) {
		this.delectedInterface = delectedInterface;
	}
	
	@Override
	public void requestUPViewpager(int sELECTED_LINE) {
		switch (sELECTED_LINE) {
		case 2:
			app_item_2_line_6.requestFocus();
			break;
		default:
			app_item_1_line_6.requestFocus();
			break;
		}
	}
	
	
}
