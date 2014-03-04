package com.hiveview.domybox.view;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.common.image.ImageLoadView;
import com.hiveview.domybox.service.HiveViewService;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.StringUtils;
import com.hiveview.domybox.utils.ToastUtil;
import com.hiveview.domybox.utils.TypefaceUtils;

public class DialogApp {
	
	private static DialogApp dialogAppUtils;
	
	public TextView 		tv_title_name;
	public ImageLoadView 	iv_dialog_icon;
	public TextView 		tv_dialog_cancel;
	public TextView 		tv_dialog_confirm;
	public LinearLayout ll_dialog_cancel;
	public LinearLayout ll_dialog_confirm;
	
	private TextView tv_app_dialog_title_name;

	private TextView tv_app_dialog_content;

	private TextView tv_app_dialog_size;

	private TextView tv_app_dialog_type;

	private TextView tv_app_dialog_developer;

	private TextView tv_app_dialog_version;

	public TextView tv_app_dialog_updata;

	public RelativeLayout ll_dialog_two_button;

	public LinearLayout ll_progressbar;		//精度条layout

	public LinearLayout ll_dialog_complete;

	public TextView tv_progressBar1_title;

	public ProgressBar progressBar1;

	private static Context mContext;

	private LinearLayout ll_dialog_complete_bg;

	private TextView tv_dialog_complete;

	public static Dialog dialog;
	
	public static  DialogApp getInstance(Context context){
		if(context!=null && mContext!=context && dialogAppUtils == null){
			dialogAppUtils=new DialogApp(context);
		}
		return dialogAppUtils;
	}

	private DialogApp(Context mContext) {
		dialog = new Dialog(mContext, R.style.dialog); 
		initView(mContext);
	}
	
	private void initView(Context mContext) {
		View view = View.inflate(mContext, R.layout.dialog_layout_uninstaill, null);
		tv_app_dialog_title_name = (TextView) view.findViewById(R.id.tv_app_dialog_title_name);
		
		tv_app_dialog_content 	= (TextView) view.findViewById(R.id.tv_app_dialog_content);
		tv_app_dialog_developer = (TextView) view.findViewById(R.id.tv_app_dialog_developer);
		tv_app_dialog_size 		= (TextView) view.findViewById(R.id.tv_app_dialog_size);
		tv_app_dialog_type 		= (TextView) view.findViewById(R.id.tv_app_dialog_type);
		
		tv_app_dialog_updata 	= (TextView) view.findViewById(R.id.tv_app_dialog_updata);
		tv_app_dialog_version 	= (TextView) view.findViewById(R.id.tv_app_dialog_version);
		
		tv_progressBar1_title 	= (TextView) view.findViewById(R.id.tv_progressBar1_title);
		
		iv_dialog_icon = (ImageLoadView) view.findViewById(R.id.iv_dialog_icon);
		
		tv_dialog_cancel 	= (TextView) view.findViewById(R.id.tv_dialog_cancel);
		tv_dialog_confirm 	= (TextView) view.findViewById(R.id.tv_dialog_confirm);
		tv_dialog_complete 	= (TextView) view.findViewById(R.id.tv_dialog_complete);
		
		ll_dialog_cancel 	= (LinearLayout) view.findViewById(R.id.ll_dialog_cancel);
		ll_dialog_confirm 	= (LinearLayout) view.findViewById(R.id.ll_dialog_confirm);
		
		ll_dialog_two_button 	= (RelativeLayout) view.findViewById(R.id.ll_dialog_two_button);
		ll_progressbar 		= (LinearLayout) view.findViewById(R.id.ll_progressbar);
		ll_dialog_complete 	= (LinearLayout) view.findViewById(R.id.ll_dialog_complete);
		
		ll_dialog_complete_bg 	= (LinearLayout) view.findViewById(R.id.ll_dialog_complete_bg);
		
		progressBar1 	= (ProgressBar) view.findViewById(R.id.progressBar1);
		
		ll_dialog_complete_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		setcharacters();
	}
	
	private void setcharacters() {
		tv_app_dialog_title_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_dialog_content.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_dialog_developer.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_dialog_size.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_dialog_type.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_dialog_updata.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_app_dialog_version.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_progressBar1_title.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_dialog_cancel.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_dialog_confirm.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_dialog_complete.setTypeface(TypefaceUtils.getStandardfFontFace());
		
	}
	
	
	/**
	 * 显示卸载Dialog 取消-卸载
	 * @param appInfoEntity 
	 * @param mHandler 
	 */
	public void showuUninstallDialog(final AppInfoEntity appInfoEntity, final Handler mHandler){
		ll_dialog_two_button.setVisibility(View.VISIBLE);
		ll_dialog_complete.setVisibility(View.GONE);
		ll_progressbar.setVisibility(View.GONE);
		progressBar1.setProgress(0);

		setNOInfo();
		
		dialogAppUtils.tv_dialog_cancel.setText(R.string.cancel);
		dialogAppUtils.tv_dialog_confirm.setText(R.string.uninstall);
		
		//取消
		dialogAppUtils.ll_dialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogApp.dialog.dismiss();
				
			}
		});
		
		//卸载
		dialogAppUtils.ll_dialog_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.unInstallApp(appInfoEntity.getPackageName(),mHandler);
				ll_progressbar.setVisibility(View.VISIBLE);
				ll_dialog_two_button.setVisibility(View.GONE);
				
				progressBar1.setProgress(0);
				progressBar1.setMax(100);
				tv_progressBar1_title.setText("卸载中");
				mHandler.sendEmptyMessageDelayed(AppConstant.MSG_DIALOG_UNINSTALL, 1000);
			}
		});
		DialogApp.dialog.show();
		
	}

	//获取APP的详情信息
	public void getAppInfo(final AppInfoEntity appInfoEntity, final Handler mHandler) {
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			private ArrayList<AppMarketEntity> arrEntityList;

			@Override
			public void run() {
				
				try {
					arrEntityList = new HiveViewService().getAppInfo(AppConstant.APP_VERSION, appInfoEntity.getPackageName());
					Message msg = new Message();
					msg.what=AppConstant.MSG_GET_APPINFO;
					msg.obj=arrEntityList.get(0);
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					
				}
				
			}
		});
		
		
	}


	
	// 完成
	public void complete() {
		ll_progressbar.setVisibility(View.GONE);
		progressBar1.setProgress(0);
		ll_dialog_complete.setVisibility(View.VISIBLE);
		ll_dialog_complete_bg.requestFocus();
		tv_dialog_complete.setText(R.string.complete);
		ll_dialog_complete_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public void setInfo(Handler mHandler, AppMarketEntity appMarketEntity) {
		
			tv_app_dialog_title_name.setText(appMarketEntity.getAppName());
			tv_app_dialog_type.setText(appMarketEntity.getTagName());
			
			tv_app_dialog_content.setText(appMarketEntity.getVersionDesc());
			tv_app_dialog_developer.setText(appMarketEntity.getDeveloper());
			tv_app_dialog_size.setText(appMarketEntity.getAppSize());
			
			tv_app_dialog_version.setText(appMarketEntity.getVersionNo());
			tv_app_dialog_updata.setText(StringUtils.getDateTime(appMarketEntity.getCtime()));
			iv_dialog_icon.setImageUrl(appMarketEntity.getAppIcon(), mHandler);
			
	}

	public void setNOInfo() {
		AppInfoEntity app = CommonMainAppView.delectApp;
		if (app!=null) {
			tv_app_dialog_title_name.setText(app.getAppName());
			tv_app_dialog_content.setText("未知");
			tv_app_dialog_developer.setText("未知");
			tv_app_dialog_size.setText("未知");
//			tv_app_dialog_type.setText(app.getVersionCode());
			
			tv_app_dialog_updata.setText("未知");
			iv_dialog_icon.setImageDrawable(app.getAppIcon());
		}else {
			tv_app_dialog_title_name.setText("未知");
			tv_app_dialog_content.setText("未知");
			tv_app_dialog_developer.setText("未知");
			tv_app_dialog_size.setText("未知");
			tv_app_dialog_type.setText("未知");
			
			tv_app_dialog_updata.setText("未知");
			iv_dialog_icon.setImageResource(R.drawable.img_default);
		}
	}
	
	/**downloading defeated*/
	public void unInstallDefeated() {
		ll_progressbar.setVisibility(View.GONE);
		progressBar1.setProgress(0);
		ll_dialog_complete.setVisibility(View.VISIBLE);
		ll_dialog_complete_bg.requestFocus();
		tv_dialog_complete.setText(R.string.uninstall_defeated);
		ll_dialog_complete_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
	}
}
