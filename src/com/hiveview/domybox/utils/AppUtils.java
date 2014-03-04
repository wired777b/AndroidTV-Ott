package com.hiveview.domybox.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.common.image.ImageRequest;
import com.hiveview.domybox.common.image.ImageTaskManager;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.service.net.BaseHttpGetConnector;
import com.hiveview.domybox.service.net.HttpGetConnector;
import com.hiveview.domybox.service.net.HttpTaskManager;
import com.hiveview.domybox.service.request.ApiConstant;
import com.hiveview.domybox.service.request.BaseGetRequest;


public class AppUtils {

	protected static final String TAG = "AppUtils";

	/**获取版本*/
	public static String getVersionName(Context ctx) {
		try {
			PackageInfo packInfo = ctx.getPackageManager().getPackageInfo( ctx.getPackageName(), 0);
			return packInfo.versionName;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	public static int dipToPx(int dipValue) {
		final float scale = PengDoctorApplication.mDisplayScale;
		int pxValue = (int) (dipValue * scale + 0.5f);
		return pxValue;
	}
	
	
	 /* 
	  *    
	 * @param _context  
	 * @return  
	 */  
	public static String getLocaldeviceId(Context _context){  
	    TelephonyManager tm = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);  
	    String deviceId = null;
		try {
			
			deviceId = Secure.getString(_context.getContentResolver(), Secure.ANDROID_ID);
//			deviceId = tm.getDeviceId();
//			String android_id = Secure.getString(_context.getContentResolver(), Secure.ANDROID_ID);
			
		} catch (Exception e) {
		}  
	    
	    System.out.println("deviceId: " + deviceId);
	    
	    if (deviceId == null|| deviceId.trim().length() == 0) { 
//	    	return "";
//	        deviceId = String.valueOf(System.currentTimeMillis());  
	    	
	        deviceId = AppUtils.getDeviceMac();
	        if (deviceId == null|| deviceId.trim().length() == 0) {  
	        	deviceId = String.valueOf(System.currentTimeMillis());  
		    }
	    }
	    return deviceId ;  
	}
	
	

	/**开启应用*/
	public static void openApp(String packageName, Context context){
		
		PackageInfo info = null;
		try {
			// 获取到包的信息
			info = context.getPackageManager().getPackageInfo(
					packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
			
			// 通过包名后去activity信息
			ActivityInfo[] activityinfos = info.activities;

			// 有些节点为空节点无法启动
			if (activityinfos.length > 0) {
				// activity 的的一个节点是可以开启的
				ActivityInfo startActivity = activityinfos[0];
				// 建立意图开启程序
				Intent intent = new Intent();
				intent.setClassName(packageName, startActivity.name);
				context.startActivity(intent);
			} else {
				Toast.makeText(context, "此程序无法启动", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "此程序无法启动", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	/**adb 卸载 apk
	 * @param mHandler */
	public static void unInstallApp(final String pageName, final Handler mHandler) {
		
		HttpTaskManager.getInstance().submit(new Runnable() {
	 	int resault	=	-1; 
		InputStream is;
		private String str;
		
		@Override
		public void run() {
			try { 
				String adb="adb uninstall " + pageName;
	        	Process p = Runtime.getRuntime().exec(adb); 
	        	is	=	p.getInputStream();
	        	if(is!=null){
	        		str = StringUtils.converStreamToString(is);
	        		resault=0;
	        	}
	        	
	            if( p.exitValue() != 0) p.destroy(); 
	            	
	            } catch (IOException e){
	            }finally{
	            	if(str!=null&&"Success".equals(str)){
            			//卸载完成
//	            		mHandler.sendEmptyMessage(AppConstant.UNINSTALL_SUCCEE);
            		}else {
						//卸载失败
//            			mHandler.sendEmptyMessage(AppConstant.UNINSTALL_DEFEATE);
					}
	            	
	            	if(is!=null){
	            		try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
	            }
			}
		});	
	}
	
	/**adb 安装 apk*/
	public static void installApp(final String fileName,final Handler mHandler) {
		
		new Thread(new Runnable() {
			
			int resault	=	-1; 
			InputStream is;
			Process p;
			String str;
			
			@Override
			public void run() {
				try { 
					Logger.e(TAG, "安装文件");
					String adb="adb install -r " + fileName;
		        	p = Runtime.getRuntime().exec(adb); 
		        	is	=	p.getInputStream();
		        	str = StringUtils.converStreamToString(is);
		        	Logger.e(TAG, str);
		        	
		        	if(is!=null){
		        		resault=0;
		        	}
		        	
//		            if( p.exitValue() != 0) p.destroy(); 
		            	
		            } catch (IOException e){
		            	e.printStackTrace();
		            }finally{
		            	Logger.w(TAG, str.substring(str.length()-7));
		            	if("Success".equals(str.substring(str.length()-7))){
		            		//安装完成
		            		Logger.e(TAG, "安装成功");
		            		mHandler.sendEmptyMessage(AppConstant.INSTALL_SUCCEE);
		            	}else {
		            		//安装失败
		            		Logger.e(TAG, "安装失败");
		            		mHandler.sendEmptyMessage(AppConstant.INSTALL_DEFEATE);
						}
		            	
		            	if(is!=null){
		            		try {
								is.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		            	}
		            	
		            	 if( p!=null) p.destroy(); 
		            
		            }
				}
		}).start();
		
//		HttpTaskManager.getInstance().submit(new Runnable() {
//			
//			int resault	=	-1; 
//			InputStream is;
//			Process p;
//			String str;
//			
//			@Override
//			public void run() {
//				try { 
//					Logger.e(TAG, "安装文件");
//					String adb="adb install -r " + fileName;
//		        	p = Runtime.getRuntime().exec(adb); 
//		        	is	=	p.getInputStream();
//		        	str = StringUtils.converStreamToString(is);
//		        	Logger.e(TAG, str);
//		        	
//		        	if(is!=null){
//		        		resault=0;
//		        	}
//		        	
//		            if( p.exitValue() != 0) p.destroy(); 
//		            	
//		            } catch (IOException e){
//		            	e.printStackTrace();
//		            }finally{
//		            	if("Success".equals(str.substring(str.length()-7))){
//		            		//安装完成
//		            		Logger.e(TAG, "安装成功");
//		            		mHandler.sendEmptyMessage(AppConstant.INSTALL_SUCCEE);
//		            	}else {
//		            		//安装失败
//		            		Logger.e(TAG, "安装失败");
//		            		mHandler.sendEmptyMessage(AppConstant.INSTALL_DEFEATE);
//						}
//		            	
//		            	if(is!=null){
//		            		try {
//								is.close();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//		            	}
//		            	
//		            	 if( p!=null) p.destroy(); 
//		            
//		            }
//				}
//		});
		
		
		
	}
	
	/**adb 安装 apk*/
	public static void installSystemApp(final File file,final Handler mHandler) {
//		FileUtils.copyFileToSystemApp(file);
		
//		new Thread(new Runnable() {
//			
//			int resault	=	-1; 
//			InputStream is;
//			Process p;
//			String str;
//			
//			@Override
//			public void run() {
//				try { 
//					Logger.e(TAG, "安装文件");
//					String adb="adb push " + fileName +"system/app";
//		        	p = Runtime.getRuntime().exec(adb); 
//		        	is	=	p.getInputStream();
//		        	str = StringUtils.converStreamToString(is);
//		        	Logger.e(TAG, str);
//		        	
//		        	if(is!=null){
//		        		resault=0;
//		        	}
//		        	
////		            if( p.exitValue() != 0) p.destroy(); 
//		            	
//		            } catch (IOException e){
//		            	e.printStackTrace();
//		            }finally{
//		            	Logger.w(TAG, str.substring(str.length()-7));
//		            	if("Success".equals(str.substring(str.length()-7))){
//		            		//安装完成
//		            		Logger.e(TAG, "安装成功");
//		            		mHandler.sendEmptyMessage(AppConstant.INSTALL_SUCCEE);
//		            	}else {
//		            		//安装失败
//		            		Logger.e(TAG, "安装失败");
//		            		mHandler.sendEmptyMessage(AppConstant.INSTALL_DEFEATE);
//						}
//		            	
//		            	if(is!=null){
//		            		try {
//								is.close();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//		            	}
//		            	
//		            	 if( p!=null) p.destroy(); 
//		            
//		            }
//				}
//		}).start();
	}
	
	
	
	/**adb su 安装 apk------中文名出线错误
	 * @param mHandler */
	public static void installAppSU(final String fileName ) {
		new Thread(new Runnable() {
			private DataInputStream dis;
			private DataOutputStream os; 
			@Override
			public void run() {
				try { 
					String cmd = "pm install "+fileName;
					String push = "adb push "+fileName+"/system/app";
					Logger.e(TAG, cmd);
					Process install = Runtime.getRuntime().exec("su");
					dis = new DataInputStream(install.getInputStream());
					os 	= new DataOutputStream(install.getOutputStream());
//					os.writeBytes("adb shell" + "\n");
//					os.writeBytes("su" + "\n");
//					os.writeBytes("mount -o rw,remount /system" + "\n");
//					os.writeBytes("chmod 777 /system/app" + "\n");

					
					os.writeBytes(push + "\n");
					
					
					os.writeBytes("exit\n");
					os.flush();
					byte[] buffer = new byte[1024];
					int len = 0;
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while ((len = dis.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
					}
					String msg = bos.toString();
					install.waitFor();
					Logger.e("install", msg);
					
//					mHandler.sendEmptyMessage(AppConstant.MSG_DIALOG_INSTALL_COMPLETE);
		            	
		            } catch (IOException e){
		            	e.printStackTrace();
		            } catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						if(dis!=null){
							try {
								dis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(os!=null){
							try {
								os.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				
			}
		}).start();
		

	}
	
	
	/**下载壁纸---
	 * @param mHandler */
	public static void getWallpager(final String filePath,final String imageUrl, final Handler mHandler) {
		ImageTaskManager.getInstance().submit(new Runnable() {
			InputStream in;
			FileOutputStream outStream;
			@Override
			public void run() {
				try {
					BaseGetRequest request = new ImageRequest(imageUrl);
					BaseHttpGetConnector connector = new HttpGetConnector(request);
					in = connector.getGetResponse();
					
					File saveFile = new File(filePath+"/"+imageUrl.substring(imageUrl.lastIndexOf("/")+1));
					if(!saveFile.getParentFile().exists()) {
		                 if(!saveFile.getParentFile().mkdirs()) {
		                	 Logger.i("创建目录", imageUrl);
		                 }
		             }
					saveFile.createNewFile();
					
					outStream = new FileOutputStream(saveFile);
					if(in!=null){
						 byte[] buffer = new byte[2048];
						 int length=0 ;
						 while ((length=in.read(buffer))!=-1) {
							 outStream.write(buffer, 0, length);
						    
						 }
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					
					if(in!=null){
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if(in!=null){
						try {
							outStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				}
			}
		});
	}
	
	
	public static String getAppClientKey(){
		StringBuffer client_key = new StringBuffer();
		client_key.append(AppUtils.getDeviceMac());
		client_key.append("/");
		client_key.append(ApiConstant.UUID);
		client_key.append("/");
		client_key.append(System.currentTimeMillis());
		
		System.out.println("client_key: " + client_key);
		return Base64.encodeToString(client_key.toString().trim().getBytes(), Base64.DEFAULT).replaceAll("\n", "");
	}
	
	public static String getDeviceMac() {
        String macSerial = null;
        String str = "";
        try {
                Process pp = Runtime.getRuntime().exec(
                                "cat /sys/class/net/wlan0/address ");
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);


                for (; null != str;) {
                        str = input.readLine();
                        if (str != null) {
                                macSerial = str.trim();// 去空格
                                break;
                        }
                }
        } catch (IOException ex) {
                // 赋予默认值
        }
        return macSerial;
	}
	
	
	public static String getBoxDeviceMac() {
		String Mac=null;  
	    try{  
	        String path="sys/class/net/wlan0/address";  
	        if((new File(path)).exists())  
	        {  
	            FileInputStream fis = new FileInputStream(path);  
	            byte[] buffer = new byte[8192];  
	            int byteCount = fis.read(buffer);  
	            if(byteCount>0)  
	            {  
	                Mac = new String(buffer, 0, byteCount, "utf-8");  
	            }  
	        }  
	        Log.v("daming.zou***wifi**mac11**", ""+Mac);  
	        if(Mac==null||Mac.length()==0)  
	        {  
	            path="sys/class/net/eth0/address";  
	            FileInputStream fis_name = new FileInputStream(path);  
	            byte[] buffer_name = new byte[8192];  
	            int byteCount_name = fis_name.read(buffer_name);  
	            if(byteCount_name>0)  
	            {  
	                Mac = new String(buffer_name, 0, byteCount_name, "utf-8");  
	            }  
	        }  
	        Log.v("daming.zou***eth0**mac11**", ""+Mac);  
//	      String path="sys/class/net/eth0/address";  
//	      FileInputStream fis_name = new FileInputStream(path);  
//	      byte[] buffer_name = new byte[8192];  
//	      int byteCount_name = fis_name.read(buffer_name);  
//	      if(byteCount_name>0)  
//	      {  
//	          mac = new String(buffer_name, 0, byteCount_name, "utf-8");  
//	      }  
	          
//	      if(mac.length()==0||mac==null){  
//	          path="sys/class/net/eth0/wlan0";  
//	          FileInputStream fis = new FileInputStream(path);  
//	          byte[] buffer = new byte[8192];  
//	          int byteCount = fis.read(buffer);  
//	          if(byteCount>0)  
//	          {  
//	              mac = new String(buffer, 0, byteCount, "utf-8");  
//	          }  
//	      }  
	          
	        if(Mac.length()==0||Mac==null){  
	            return "";  
	        }  
	    }catch(Exception io){  
	        Log.v("daming.zou**exception*", ""+io.toString());  
	    }  
	      
	    Log.v("xulongheng*Mac", Mac);  
	    return Mac.trim();  
	//  WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
	//  WifiInfo info = wifi.getConnectionInfo();  
	//  if (info.getMacAddress() != null) {  
//	      return info.getMacAddress().toString();  
	//  }   
	    
	}
	
	/**获取已经安装 非系统的 app */
	public static ArrayList<AppInfoEntity> getAppInfo(Context conext) {

		List<PackageInfo> packages = conext.getPackageManager().getInstalledPackages(0);
		ArrayList<AppInfoEntity> appList = new ArrayList<AppInfoEntity>();
		ArrayList<AppInfoEntity> appSystemList = new ArrayList<AppInfoEntity>();
        for(int i=0;i<packages.size();i++) { 
	        PackageInfo packageInfo = packages.get(i); 
	        
	        if(!"com.hiveview.tv.onlive".equals(packageInfo.packageName)
	        		&&!"com.videoplayer".equals(packageInfo.packageName)
	        		&&!"com.qiyi.video".equals(packageInfo.packageName)){
	        	
	        	AppInfoEntity info = new AppInfoEntity(); 
	        	info.setAppName(packageInfo.applicationInfo.loadLabel(conext.getPackageManager()).toString());
	        	info.setPackageName(packageInfo.packageName); 
	        	info.setVersionName(packageInfo.versionName); 
	        	info.setVersionCode(packageInfo.versionCode); 
	        	info.setAppIcon(packageInfo.applicationInfo.loadIcon(conext.getPackageManager()));
	        	//Only display the non-system app info
	        	if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
	        		appList.add(info);//如果非系统应用，则添加至appList
	        	}
	        }else {
				//---  组件	---//
	        	AppInfoEntity info = new AppInfoEntity(); 
	        	info.setAppName(packageInfo.applicationInfo.loadLabel(conext.getPackageManager()).toString());
	        	info.setPackageName(packageInfo.packageName); 
	        	info.setVersionName(packageInfo.versionName); 
	        	info.setVersionCode(packageInfo.versionCode); 
	        	info.setAppIcon(packageInfo.applicationInfo.loadIcon(conext.getPackageManager()));
	        	appSystemList.add(info);
			}
	        
       }
       PengDoctorApplication.appSystemList = appSystemList;
       PengDoctorApplication.appInstalledList = appList;
       return appList;
	}

	public static void reBoot() {
		
		HttpTaskManager.getInstance().submit(new Runnable() {
		
		@Override
		public void run() {
			try { 
				String reboot="adb reboot";
	        	Process p = Runtime.getRuntime().exec(reboot); 
	        	
	            if( p.exitValue() != 0) p.destroy(); 
	            	
	            } catch (IOException e){
	            	e.printStackTrace();
	            }
			}
		});	
	}

	
	public static void commondSystemApp(final File file ) {
		
		HttpTaskManager.getInstance().submit(new Runnable() {
		private DataInputStream dis;
		private DataOutputStream os; 
		
		@Override
		public void run() {
			try { 
				String shell = "adb shell";
				Logger.e(TAG, shell);
				Process install = Runtime.getRuntime().exec("shell");
//				Process install = Runtime.getRuntime().exec("su");
				dis = new DataInputStream(install.getInputStream());
				os 	= new DataOutputStream(install.getOutputStream());
//				os.writeBytes("adb shell" + "\n");
				os.writeBytes("su" + "\n");
				os.writeBytes("mount -o rw,remount /system" + "\n");
				os.writeBytes("chmod 777 /system/app" + "\n");
				
				os.writeBytes("cp" +file.getAbsolutePath()+" "+"/system/app" + "\n");
				
				os.writeBytes("exit\n");
				os.flush();
				byte[] buffer = new byte[1024];
				int len = 0;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				while ((len = dis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
				}
				String msg = bos.toString();
				install.waitFor();
				Logger.e("install", msg);
				
//				mHandler.sendEmptyMessage(AppConstant.MSG_DIALOG_INSTALL_COMPLETE);
	            	
	            } catch (IOException e){
	            	e.printStackTrace();
	            } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(dis!=null){
						try {
							dis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(os!=null){
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	
	public static void push(final File file) {

		new Thread(new Runnable() {
			private DataInputStream dis;
			private DataOutputStream os;
			Process install;
			@Override
			public void run() {
				try { 
					String push = "adb push "+file.getAbsolutePath()+" /system/app";
					Logger.e(TAG, push);
					install = Runtime.getRuntime().exec(push);
					
		        	InputStream is = install.getInputStream();
		        	if(is!=null){
		        		String str = StringUtils.converStreamToString(is);
		        		Logger.e(TAG, str);
		        	}
		        	
//		            if( install.exitValue() != 0) install.destroy(); 
//					dis = new DataInputStream(install.getInputStream());
//					os 	= new DataOutputStream(install.getOutputStream());
//					os.writeBytes("adb shell" + "\n");
//					os.writeBytes("su" + "\n");
//					os.writeBytes("mount -o rw,remount /system" + "\n");
//					os.writeBytes("chmod 777 /system/app" + "\n");

					
//					os.writeBytes(push + "\n");
//					
//					
//					os.writeBytes("exit\n");
//					os.flush();
//					byte[] buffer = new byte[1024];
//					int len = 0;
//					ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					while ((len = dis.read(buffer)) != -1) {
//						bos.write(buffer, 0, len);
//					}
//					String msg = bos.toString();
//					install.waitFor();
//					Logger.e("install", msg);
					
//					mHandler.sendEmptyMessage(AppConstant.MSG_DIALOG_INSTALL_COMPLETE);
		            	
		            } catch (IOException e){
		            	e.printStackTrace();
		            
					}finally{
						
						if(install!=null)install.destroy();
						if(dis!=null){
							try {
								dis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(os!=null){
							try {
								os.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						//删除 源文件
						file.delete();
					}
				
			}
		}).start();
		

	
	}
	



}
