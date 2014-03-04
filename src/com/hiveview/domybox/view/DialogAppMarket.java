package com.hiveview.domybox.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.common.image.ImageLoadView;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.service.net.HttpTaskManager;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.FileUtils;
import com.hiveview.domybox.utils.Logger;
import com.hiveview.domybox.utils.StringUtils;
import com.hiveview.domybox.utils.TypefaceUtils;

public class DialogAppMarket {
	
	protected static final String TAG = "DialogAppMarket";

	private static DialogAppMarket dialogUtils;
	
	public TextView tv_title_name;
	public ImageLoadView  iv_dialog_icon;
	public TextView tv_dialog_cancel;
	public TextView tv_dialog_confirm;
	public LinearLayout ll_dialog_cancel;
	public LinearLayout ll_dialog_confirm;
	
	private String locationpath;

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

	public LinearLayout ll_dialog_complete_bg;

	private TextView tv_dialog_complete;

	public static Dialog dialog;
	
	public static  DialogAppMarket getInstance(Context context){
		
		if(context!=null&&mContext!=context){
			dialogUtils=new DialogAppMarket(context);
		}
		return dialogUtils;
		
	}

	private DialogAppMarket(Context mContext) {
		DialogAppMarket.mContext=mContext;
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
	 * 显示  打开 取消
	 * @param appInfoEntity 
	 * @param mHandler 
	 */
	public void showOpenDialog(final AppMarketEntity appMarketEntity, final Handler mHandler){
		ll_dialog_two_button.setVisibility(View.VISIBLE);
		ll_dialog_complete.setVisibility(View.GONE);
		ll_progressbar.setVisibility(View.GONE);
		progressBar1.setProgress(0);
		
		dialogUtils.tv_dialog_confirm.setText(R.string.open_app);
		dialogUtils.tv_dialog_cancel.setText(R.string.cancel);
		
		//打开应用
		dialogUtils.ll_dialog_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.openApp(appMarketEntity.getBundlerId(),mContext);
				dialogUtils.dialog.dismiss();
			}
		});
		
		//取消
		dialogUtils.ll_dialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogUtils.dialog.dismiss();
			}
		});
		
		if (!((Activity)mContext).isFinishing()) {  
			dialogUtils.dialog.show();
	    } 
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
	

	/**
	 *  取消.卸载
	 * @param appInfoEntity 
	 * @param mHandler
	 */
	public void showDialogCancelUninstall(final AppInfoEntity appInfoEntity, final Handler mHandler){
		ll_dialog_two_button.setVisibility(View.VISIBLE);
		ll_dialog_complete.setVisibility(View.GONE);
		ll_progressbar.setVisibility(View.GONE);
		dialogUtils.tv_dialog_cancel.setText(R.string.cancel);
		dialogUtils.tv_dialog_confirm.setText(R.string.uninstall);
		
		//取消
		dialogUtils.ll_dialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogUtils.dialog.dismiss();
			}
		});
		
		//卸载
		dialogUtils.ll_dialog_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.unInstallApp(appInfoEntity.getPackageName(), mHandler);
				ll_progressbar.setVisibility(View.VISIBLE);
				ll_dialog_two_button.setVisibility(View.GONE);
				progressBar1.setMax(100);
				tv_progressBar1_title.setText(R.string.uninstalling);
				startUnInstall(mHandler);
			}
		});
		if (!((Activity)mContext).isFinishing()) {  
			dialogUtils.dialog.show();
	    } 
	}
	
	/**卸载 完成
	 * @return */
	public void unInstallComplete(){
		progressBar1.setProgress(100);
		ll_progressbar.setVisibility(View.GONE);
		Logger.e("卸载", "卸载完成");
		ll_dialog_complete.setVisibility(View.VISIBLE);
		ll_dialog_complete_bg.requestFocus();
		
	}

	
	/** 升级dialog 
	 * 立即升级、稍后升级------------有最新版本
	 * @param mHandler 
	 * */
	public void showUpDataDialog(final AppMarketEntity appMarketEntity, final Handler mHandler) {
		final DialogAppMarket dialogUtils =DialogAppMarket.getInstance(mContext);
		dialogUtils.tv_dialog_cancel.setText(R.string.updata);
		dialogUtils.tv_dialog_confirm.setText(R.string.later_updata);
		progressBar1.setProgress(0);
//		dialog.setCancelable(false);
		
		//升级
		dialogUtils.ll_dialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogUtils.ll_dialog_two_button.setVisibility(View.GONE);
				dialogUtils.ll_progressbar.setVisibility(View.VISIBLE);
				
				dialogUtils.tv_progressBar1_title.setText(R.string.down_loading);
				
				DownLoadTaskManager.getInstance().submit(new Runnable() {
					public void run() {
						try {
							downLoad(appMarketEntity,mHandler);
						} catch (Exception e) {
							mHandler.sendEmptyMessage(AppConstant.MSG_DOWNLOAD_DEFEATED);
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		//稍后
		dialogUtils.ll_dialog_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogUtils.dialog.dismiss();
			}
		});
		
		
		
	 if (!((Activity)mContext).isFinishing()) {  
			dialogUtils.dialog.show();
	    } 

	}
	
	
	/**下载更新
	 * @param url 
	 * @param mHandler 
	 * @throws Exception */
	protected void downLoad( AppMarketEntity appMarketEntity, Handler mHandler) throws Exception {
		String url=appMarketEntity.getVersionUrl();
		if(url == null || url.equals("")){
			Toast.makeText(mContext, "无效地址", 1).show();
			dialog.dismiss();
		}else {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				//SD可用
				locationpath=AppConstant.LOCATION_PATH;
			}
//			else {
////				SD卡不可用
//				locationpath=mContext.getCacheDir().getAbsolutePath()+"/"+"DomyBoxLanucher";
//			}
			
			
			try {
				//创建文件---获取文件大小
				int size = FileUtils.fileInit(url, locationpath,mHandler);
				Logger.e(TAG, size+"");
				//下载文件
				FileUtils.downLoad(url,locationpath,size,mHandler);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException(e.getLocalizedMessage(), e);
			}
		}
	}
	
	// 安装完成
	public void complete() {
		progressBar1.setProgress(progressBar1.getMax());
		ll_progressbar.setVisibility(View.GONE);
		progressBar1.setProgress(0);
		tv_dialog_complete.setText(R.string.complete);
		ll_dialog_complete_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		Logger.e("安装", "完成");
		ll_dialog_complete.setVisibility(View.VISIBLE);
		ll_dialog_complete_bg.requestFocus();
	}
	
	
	/**
	 * 显示---安装
	 * @param appInfoEntity 
	 * @param mHandler 
	 */
	public void installDialog(final AppMarketEntity appMarketEntity, final Handler mHandler){
		ll_dialog_complete.setVisibility(View.GONE);
		ll_dialog_two_button.setVisibility(View.VISIBLE);
		ll_progressbar.setVisibility(View.GONE);
		progressBar1.setProgress(0);
		
		dialogUtils.tv_dialog_cancel.setText(R.string.complete);
		dialogUtils.tv_dialog_confirm.setText(R.string.open_app);
		
		tv_dialog_confirm.setText(R.string.install);
		tv_dialog_cancel.setText(R.string.cancel);
		
		
		//下载
		dialogUtils.ll_dialog_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				dialog.setCancelable(false);
				dialogUtils.ll_dialog_two_button.setVisibility(View.GONE);
				dialogUtils.ll_progressbar.setVisibility(View.VISIBLE);
				dialogUtils.ll_dialog_complete.setVisibility(View.GONE);
				
				dialogUtils.tv_progressBar1_title.setText(R.string.installing);
				
				HttpTaskManager.getInstance().submit(new Runnable() {
					public void run() {
						try {
							downLoad(appMarketEntity,mHandler);
						} catch (Exception e) {

							e.printStackTrace();
							//下载失败
							mHandler.sendEmptyMessage(AppConstant.MSG_DOWNLOAD_DEFEATED);
						}
					}
				});
				
				
				
			}
		});
		
		//取消
		dialogUtils.ll_dialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		
		if (!((Activity)mContext).isFinishing()) {  
	        dialogUtils.dialog.show();
		}else {
//			mContext=null;
			Toast.makeText(mContext, "暂时无法显示", 1).show();
		} 
	}

	/**downloading defeated*/
	public void downloadDefeated() {
		ll_dialog_complete.setVisibility(View.VISIBLE);
		ll_dialog_two_button.setVisibility(View.GONE);
		ll_progressbar.setVisibility(View.GONE);
//		dialog.setCancelable(true);
		ll_dialog_complete_bg.requestFocus();
		dialogUtils.tv_dialog_complete.setText(R.string.down_load_defeated);
		
		//失败
		ll_dialog_complete_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogUtils.dialog.dismiss();
			}
		});
	}
	
	/**install defeated*/
	public void installDefeated() {
		ll_dialog_complete.setVisibility(View.VISIBLE);
		ll_dialog_two_button.setVisibility(View.GONE);
		ll_progressbar.setVisibility(View.GONE);
//		dialog.setCancelable(true);
		ll_dialog_complete_bg.requestFocus();
		dialogUtils.tv_dialog_complete.setText(R.string.install_defeated);
		
		//失败
		ll_dialog_complete_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogUtils.dialog.dismiss();
			}
		});
	}

	public void setInfo(AppMarketEntity appMarketEntity,Handler mHandler) {
		tv_app_dialog_title_name.setText(appMarketEntity.getAppName());
		tv_app_dialog_type.setText(appMarketEntity.getTagName());
		//TODO tagName
		
		tv_app_dialog_content.setText(appMarketEntity.getVersionDesc());
		tv_app_dialog_developer.setText(appMarketEntity.getDeveloper());
		tv_app_dialog_size.setText(appMarketEntity.getAppSize());
		
		tv_app_dialog_version.setText(appMarketEntity.getVersionNo());
		tv_app_dialog_updata.setText(StringUtils.getDateTime(appMarketEntity.getCtime()));
		iv_dialog_icon.setImageUrl(appMarketEntity.getAppIcon(), mHandler);
	}

	
}
