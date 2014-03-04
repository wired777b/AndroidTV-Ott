package com.hiveview.domybox.activity;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.service.exception.NetworkConnectException;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.service.net.HttpTaskManager;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.Logger;
import com.hiveview.domybox.utils.MD5Utils;
import com.hiveview.domybox.utils.PackageManagerUtils;

public class InitializationActivity extends BaseActivity {
	protected static final String TAG = "InitializationActivity";
	
	private static final int 	DEVICECHECK_SUCCESS = 10000;
	private static final int 	DEVICECHECK_FAIL 	= DEVICECHECK_SUCCESS + 1;
	private static final int 	NETWORK_UNCONNECT 	= DEVICECHECK_SUCCESS + 2;
	
	private boolean 	isCheckDevSuccess = false;
	private boolean 	isFinish 	= 	false;
	protected String 	dateStr;
	private SharedPreferences spUpdata;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NETWORK_UNCONNECT:	//网络未连接
				networkUnconnect();
				break;
				
			case DEVICECHECK_SUCCESS:
				isCheckDevSuccess = true;
				
				Intent intent = new Intent(InitializationActivity.this,MainActivity.class);
				InitializationActivity.this.startActivity(intent);
				InitializationActivity.this.finish();
				
				break;
				
			case DEVICECHECK_FAIL:
				isCheckDevSuccess = false;
				deviceCheckFail();
				
				break;

				
			default:
				break;
			}
			
		};
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_initializtion);
		
		init();
		
	}
	
	
	


	/**网络未连接*/
	protected void networkUnconnect() {
		AlertDialog.Builder netBuilder = new AlertDialog.Builder(InitializationActivity.this);  
		netBuilder.setMessage("网络未连接，无法鉴权设备，请设置网络")  
		       .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
		           public void onClick(DialogInterface dialog, int id) {  
		        	   
		        	   Intent mIntent = new Intent();
		                ComponentName comp = new ComponentName(
		                        "com.android.settings",
		                        "com.android.settings.Settings");
		                mIntent.setComponent(comp);
		                mIntent.setAction("android.intent.action.VIEW");
		                startActivity(mIntent);
		        	   InitializationActivity.this.finish();
		           }  
		       });
		netBuilder.create();
		netBuilder.show();
	}
	
	/**网络未连接*/
	protected void deviceCheckFail() {
		AlertDialog.Builder netBuilder = new AlertDialog.Builder(InitializationActivity.this);  
		netBuilder.setMessage("鉴权失败，请联系厂商。")  
		       .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
		           public void onClick(DialogInterface dialog, int id) {  
		        	   InitializationActivity.this.finish();
		           }  
		       });
		netBuilder.create();
		netBuilder.show();
	}


	private void authorization() {
		AppUtils.installAppSU("/system/app/a");
	}


	private void init(){
		//  sp
    	spUpdata = getSharedPreferences(AppConstant.SP_UPDATA, Context.MODE_PRIVATE);
		//授权
		authorization();
		
		checkUpdata();
		
		checkNetWork();
		
		
	}
	
	


	private void checkNetWork() {
		HttpTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					InetAddress address = InetAddress.getByName("www.baidu.com");
					if(address!=null){
						deviceCheck();
					}else {
						//网络未通
						mHandler.sendEmptyMessage(NETWORK_UNCONNECT);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
					//网络未通
					mHandler.sendEmptyMessage(NETWORK_UNCONNECT);
				}
				
			}
		});
		
	}





	/**检查是否升级
	 * @throws IOException */
	private Boolean checkUpdata(){
		Boolean isReBoot = false;
		File dir = new File(AppConstant.SYSTEM_APP_PATH);
//		File dir = new File(AppConstant.LOCATION_PATH);
		if(dir.exists()){
			File[] files = dir.listFiles(); 
			try {
				for (File file : files) {  
				    if (file.isFile()) {  
				    	String md5 		= spUpdata.getString(file.getName(), "");
				    	String md5File 	= MD5Utils.getFileMD5String(file);
				    	
				    	//
				    	PackageInfo packageInfo = PackageManagerUtils.getPackageInfo(InitializationActivity.this, file.getAbsolutePath());
				    	
				    	ApplicationInfo  appInfo = PackageManagerUtils.getApplicationInfo(InitializationActivity.this, packageInfo.packageName);
//				    	ApplicationInfo  appInfo = PackageManagerUtils.getApplicationInfo(InitializationActivity.this, "st.com.xiami");
				    	
				    	String path  = appInfo.sourceDir;
				    	Logger.e(TAG, "delete"+path);
				    	new File(path).delete();
				    	
				    	
//				    	AppUtils.push(file);
				    	
				    	
				    	//linux 命令安装
//				    	AppUtils.commondSystemApp(file);
				    	
//				    	 ////--------文件-----------///////////////
//				    	file.renameTo(new File("/system/app/"+file.getName()));
				    	
//				    	Logger.e(TAG, file.getName());
//				    	new File("/system/app/"+file.getName()).delete();
				    	////////////------------------//////////////////////
				    	if(md5File.equals(md5)){
				    		//push system/app
				    		AppUtils.push(file);
				    		
//				    		FileUtils.copyFileToSystemApp(file);
//				    		//copy to system/app
//							FileUtils.copyFile(file.getAbsolutePath(), "/system/app/"+file.getName());
				    		
				    		
//				    		new File("/system/app/"+file.getName()).createNewFile();
//				    		file.renameTo(new File("/system/app/"+file.getName()));
//							isReBoot = true;
//							
//							File sysFile =new File("/system/app/"+file.getName());
//							if(md5.equals(MD5Utils.getFileMD5String(sysFile))){
//								file.delete();
//								Logger.e(TAG, "delete"+file.getName());
//							}
				    	}else {
				    		file.delete();
						}
				    }  
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		isFinish = true;
		return isReBoot ;
	}


	//设备 鉴权
	private void deviceCheck() {
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					
					
					
//					dateStr =	new HiveViewService().deviceCheck(
//							ApiConstant.APP_VERSION, 
//							AppUtils.getLocaldeviceId(getApplicationContext()), 
//							AppUtils.getBoxDeviceMac(), 
//							ApiConstant.UUID);
					mHandler.sendEmptyMessageDelayed(DEVICECHECK_SUCCESS,3000);
				}catch (NetworkConnectException e) {
					mHandler.sendEmptyMessage(DEVICECHECK_FAIL);
					e.printStackTrace();
				} catch (ServiceException e) {
					mHandler.sendEmptyMessage(DEVICECHECK_FAIL);
					e.printStackTrace();
				} catch (Exception e) {
					mHandler.sendEmptyMessage(DEVICECHECK_FAIL);
					e.printStackTrace();
				}
			}
		});
		
	}

}
