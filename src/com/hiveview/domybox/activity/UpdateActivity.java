package com.hiveview.domybox.activity;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.FileUtils;
import com.hiveview.domybox.utils.Logger;
import com.hiveview.domybox.utils.MD5Utils;
import com.hiveview.domybox.utils.PackageManagerUtils;
import com.hiveview.domybox.utils.ToastUtil;
import com.hiveview.domybox.utils.TypefaceUtils;

public class UpdateActivity extends BaseActivity{
	private static final int DOWN_FINISH = 4489;
	private static final String TAG = "UpdateActivity";
	private String 		url;
	private TextView 	tv_update_progress_num;
	private String 		locationpath;
	private NumberFormat format = NumberFormat.getInstance();
	protected File 		apk;
	private ProgressBar progressBar1;
	private TextView 	version;
	private String 		md5;
	private String 		md5System;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		private int size;

		public void handleMessage(android.os.Message msg) {
			int progress = 0;
			switch (msg.what) {
			
			case AppConstant.MSG_DIALOG_GETSIZE:
				size = msg.arg1;
				apk=(File) msg.obj;
				progressBar1.setMax(size);
				break;
			
			case AppConstant.MSG_DIALOG_PROGRESS:
				progress = progressBar1.getProgress();
				int add = msg.arg1;
				progress += add;
				progressBar1.setProgress(progress);
				tv_update_progress_num.setText("已完成"+progress*100/size+"%");
				
				if(progress==progressBar1.getMax()){
			    	String md5File;
					try {
						md5File = MD5Utils.getFileMD5String(apk);
						if(md5.equals(md5File)){
							//提示安装中
							version.setVisibility(View.GONE);
							progressBar1.setVisibility(View.GONE);
							tv_update_progress_num.setText("即将重启");
							installLanucher();
						}else {
							ToastUtil.showMessageView(UpdateActivity.this, "MD5校验失败", 1);
							apk.delete();
						}
					} catch (IOException e1) {
						apk.delete();
						ToastUtil.showMessageView(UpdateActivity.this, "安装失败", 1);
						e1.printStackTrace();
					} catch (InterruptedException e) {
						apk.delete();
						ToastUtil.showMessageView(UpdateActivity.this, "安装失败", 1);
						e.printStackTrace();
					}
				}
				break;
				
			default:
				break;
			}
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		format.setMinimumFractionDigits(2);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateprogress_activity);
		init();
	}
	
	protected void installLanucher() throws IOException, InterruptedException {
		if(apk!=null){
			ApplicationInfo  appInfo = PackageManagerUtils.getApplicationInfo(UpdateActivity.this, AppConstant.PACKAGE_NAME_LANUCHER);
	    	
			FileUtils.copyFileToSystemApp(apk);
			md5System = MD5Utils.getFileMD5String(new File("/system/app",apk.getName()));
			if(md5.equals(md5System)){
				//删除源
		    	apk.delete();
		    	
		    	//删除
		    	String path  = appInfo.sourceDir;
		    	Logger.e(TAG, "delete"+path);
		    	new File(path).delete();
		    	AppUtils.reBoot();
			}
			
		}
	}
	private void init() {
		url = getIntent().getStringExtra("url");
		md5 = getIntent().getStringExtra("md5");
		if(null == url || "".equals(url)) {
			Toast.makeText(this, "地址错误，升级失败", 1).show();
			return;
		}
		
		
		version 		= 	(TextView) findViewById(R.id.tv_update_progress);
		progressBar1	= 	(ProgressBar) findViewById(R.id.progressBar1);
		tv_update_progress_num = (TextView) findViewById(R.id.tv_update_progress_num);
		
		version.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_update_progress_num.setTypeface(TypefaceUtils.getStandardfFontFace());
		version.setText("正在 升级" + getIntent().getStringExtra("version") +"版本，请勿关闭电视或盒子");
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//			SD可用
			locationpath=AppConstant.LOCATION_PATH;
		}else {
//			SD卡不可用
			locationpath=this.getCacheDir().getAbsolutePath()+"/"+"DomyBoxLanucher";
		}
		threadDown(url,handler);
	}
	
	private void threadDown(final String url, final Handler mHandler) {
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				int size;
				try {
					size = FileUtils.fileInit(url, locationpath,mHandler);
					FileUtils.downLoad(url,locationpath,size,mHandler);
				} catch (Exception e) {
					ToastUtil.showMessageView(UpdateActivity.this, "下载失败", 1);
					UpdateActivity.this.finish();
					e.printStackTrace();
				}
			}
		});
		
	}
	

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		 if (KeyEvent.ACTION_DOWN == event.getAction()) {
			if(event.getKeyCode() ==  KeyEvent.KEYCODE_BACK) return false;
		}
		return super.dispatchKeyEvent(event);
	}
	
}
