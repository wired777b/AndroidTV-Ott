package com.hiveview.domybox.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.NumberFormat;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.common.image.DownLoadTaskManager;
import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.service.exception.ServiceException;

	public class FileUtils {
		private static final String TAG = "FileUtils";
		private static final int DOWN_FINISH = 4489;
		private static NumberFormat format = NumberFormat.getInstance();
		private static File file;
		
		public static void downLoad(String url,String locationpath,int size,Handler handler) throws Exception{
		  int range = size / 4;
		  int start;
		  int end;
		  
		  for(int i=0;i<4;i++){
			  start=i * range;
			  end=  (i + 1)* range - 1;
			  if(i==3){
				  end=size;
			  }
			  
//			  new com.hiveview.domybox.utils.DownLoadThread(url, locationpath, start, end, handler)
			  
			  DownLoadThread(url,locationpath,start,end,handler);
		  }
		  
		}
	
	
	 @SuppressWarnings("resource")
	private static RandomAccessFile DownLoadThread(final String urlstr, final String locationpath,final int start, final int end, final Handler handler) throws Exception {
		    InputStream is;
		    RandomAccessFile randomAccessFile;
			format.setMinimumFractionDigits(2);
			 try {
				 URL url = new URL(urlstr);
				 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				 connection.setConnectTimeout(5000);
				 connection.setReadTimeout(5000);
				 
				 connection.setRequestMethod("GET");
//			    int responseCode = ((HttpURLConnection) connection).getResponseCode();
//                 
//                 if (responseCode != HttpURLConnection.HTTP_OK)
//                     throw new RuntimeException();

				
				 // 设置范围，格式为Range：bytes x-y;
				 connection.setRequestProperty("Range", "bytes="+start + "-" + end);
				 randomAccessFile = new RandomAccessFile(locationpath+"/"+urlstr.substring(urlstr.lastIndexOf("/")+1), "rwd");
				 randomAccessFile.seek(start);
				 // 将要下载的文件写到保存在保存路径下的文件中
				 is = connection.getInputStream();
				 if (null == is) throw new RuntimeException();
				 
				 byte[] buffer = new byte[5120];
				 int length=0 ;
				 long count = 0;
				 System.out.println("urlstr-----------------" + urlstr);
				 while ((length=is.read(buffer))!=-1) {
					 count += length;
				     randomAccessFile.write(buffer, 0, length);
				     
				     Message message = Message.obtain();
				     message.what = AppConstant.MSG_DIALOG_PROGRESS;
				     message.arg1=length;
				     
				     float floatPercent = Float.parseFloat(format.format((float)count/(float)randomAccessFile.length()));
				     int percent = (int) (floatPercent * 100);
				     message.arg2 = percent;
				     handler.sendMessage(message);
				 }
				 
				 handler.sendEmptyMessage(DOWN_FINISH);
				 
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException(e.getLocalizedMessage(), e);
			}
			return randomAccessFile;
			
		}


	public static int fileInit(String urlstr ,String locationpath, Handler mHandler)throws Exception{
    	int fileSize = -1;
             try {
            	 fileSize = createFile(urlstr,locationpath);
				 
				 Message msg =Message.obtain();
				 msg.what=AppConstant.MSG_DIALOG_GETSIZE;
				 msg.arg1=fileSize;
				 msg.obj=file;
				 mHandler.sendMessage(msg);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			} catch (ProtocolException e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			}
             
//    	 }
//		catch (Exception e) {
////        	 e.printStackTrace();
////        	 Toast.makeText(context, "下载异常", 1).show();
//        	 throw new ServiceException(e.getLocalizedMessage(), e);
//         }
		return fileSize;
			
		}


	private static int createFile(String urlstr, String locationpath) throws IOException {
		URL url = new URL(urlstr);
		 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		 connection.setConnectTimeout(5000);
		 connection.setReadTimeout(5000);
		 connection.setRequestMethod("GET");
		 
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        
        if (responseCode != HttpURLConnection.HTTP_OK)
            throw new RuntimeException();

        InputStream in = connection.getInputStream();

        if (null == in)
            throw new RuntimeException();

		 
		  int fileSize = connection.getContentLength();
//         +File.separator+"aishop"
		  
		 String fileName = urlstr.substring(urlstr.lastIndexOf("/")+1);
		 file = new File(locationpath+"/"+fileName);
		 
		 if(!file.getParentFile().exists()) {
		     if(!file.getParentFile().mkdirs()) {
		     }
		 }
		 file.createNewFile();
		 
		 RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
		 accessFile.setLength(fileSize);
		 accessFile.close();
		 connection.disconnect();
		
		
		return fileSize;
	}


	//删除文件
	public static void delecte(final File fileApk) {
		DownLoadTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				if (fileApk.isFile() && fileApk.exists()) {  
					fileApk.delete();  
			    }  
			}
		});
	}
	
	/**
	 * 读取文件夹大小
	 * @param dirPath
	 * @return
	 */
	public static long GetPathLength(String dirPath){
		File dir=new File(dirPath);
		return getDirSize(dir);
	}
	
	/**
	 * 读取文件夹大小
	 * @param dir
	 * @return
	 */
	private static long getDirSize(File dir) {  
	    if (dir == null) {  
	        return 0;  
	    }  
	    if (!dir.isDirectory()) {  
	        return 0;  
	    }  
	    long dirSize = 0;  
	    File[] files = dir.listFiles();  
	    for (File file : files) {  
	        if (file.isFile()) {  
	            dirSize += file.length();  
	        } else if (file.isDirectory()) {  
	            dirSize += file.length();  
	            dirSize += getDirSize(file); // 如果遇到目录则通过递归调用继续统计  
	        }  
	    }  
	    return dirSize;  
	} 
	
	/**下载更新
	 * @param url 
	 * @param mHandler 
	 * @throws Exception */
	public static void downLoad(Context mContext,String url, Handler mHandler) throws Exception {
//		url=entity.getUrl();
		String locationpath = null;
		if(url == null || url.equals("")){
			Logger.e(TAG, "无效地址");
			Toast.makeText(mContext, "无效地址", 1).show();
		}else {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				// SD可用
				locationpath=AppConstant.LOCATION_PATH;
			}
//			else {
////				SD卡不可用
//				locationpath=mContext.getCacheDir().getAbsolutePath()+"/"+"DomyBoxLanucher";
//			}
			
			try {
				//创建文件---获取文件大小
				int size = FileUtils.fileInit(url, locationpath,mHandler);
				//下载文件
				FileUtils.downLoad(url,locationpath,size,mHandler);
			} catch (Exception e) {
				System.out.println("dialog Exception");
				throw new ServiceException(e.getLocalizedMessage(), e);
			}
		}
	}
	
	//获取可用空间大小
	public static boolean isDownload(){
		//可用
		int availableSize=AppConstant.statfsSD.getBlockCount() - AppConstant.statfsSD.getAvailableBlocks();
		return availableSize>15?true:false;
	}
	
	/**下载系统app
	 * @param url 
	 * @param mHandler 
	 * @throws Exception */
	public static void downLoadSYSApp(Context mContext,String url, Handler mHandler) throws Exception {
//		url=entity.getUrl();
		String locationpath = null;
		if(url == null || url.equals("")){
			Logger.e(TAG, "无效地址");
			Toast.makeText(mContext, "无效地址", 1).show();
		}else {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				// SD可用
				locationpath=AppConstant.SYSTEM_APP_PATH;
			}
//			else {
////				SD卡不可用
//				locationpath=mContext.getCacheDir().getAbsolutePath()+"/"+"DomyBoxLanucher";
//			}
			
			try {
				//创建文件---获取文件大小
				int size = FileUtils.SystemfileInit(url, locationpath,mHandler);
				//下载文件
				FileUtils.downLoad(url,locationpath,size,mHandler);
			} catch (Exception e) {
				System.out.println("dialog Exception");
				throw new ServiceException(e.getLocalizedMessage(), e);
			}
		}
	}


	private static int SystemfileInit(String url, String locationpath,Handler mHandler) {
    	int fileSize = -1;
        try {
       	 	 fileSize = createFile(url,locationpath);
			 Message msg =	Message.obtain();
			 msg.what	=	AppConstant.MSG_DIALOG_GETSIZE;
			 msg.arg1	=	fileSize;
			 msg.obj	=	file;
			 mHandler.sendMessage(msg);
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
			throw new ServiceException(e.getMessage());
		} catch (ProtocolException e) {
			System.out.println("ProtocolException");
			throw new ServiceException(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			throw new ServiceException(e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException");
			throw new ServiceException(e.getMessage());
		}
        

	return fileSize;
		
	}


	public static void systemDownLoad(Context mContext, AppMarketEntity appMarketEntity, Handler handler) {
		String url = appMarketEntity.getVersionUrl();
		String locationpath = null;
		if(url == null || url.equals("")){
			Toast.makeText(mContext, "无效地址", 1).show();
		}else {
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				// SD可用
				locationpath=AppConstant.SYSTEM_APP_PATH;
			}
//			else {
////				SD卡不可用
//				locationpath=mContext.getCacheDir().getAbsolutePath()+"/"+"DomyBoxLanucher";
//			}
			try {
				//创建文件---获取文件大小
				int size = FileUtils.SystemfileInit(url, locationpath, handler);
				//下载文件
				FileUtils.downLoad(url,locationpath,size,handler);
			} catch (Exception e) {
				System.out.println("dialog Exception");
				throw new ServiceException(e.getLocalizedMessage(), e);
			}
		}
	}


	/** copy apk to system/app 
	 * @throws IOException 
	 * @throws InterruptedException */
	public static void copyFileToSystemApp(File file) throws IOException, InterruptedException {
		Logger.e(TAG, "copyFile--------system/app");
		Process p = Runtime.getRuntime().exec("/system/xbin/su");
		DataOutputStream os = new DataOutputStream(p.getOutputStream());
		os.writeBytes("adb remount \n");
		os.writeBytes("busybox mv " +file.getAbsolutePath()+  " /system/app \n");
		os.writeBytes("busybox chmod 777 /system/app/"+file.getName()+" \n");
//		if(fileApk !=null){
//			os.writeBytes("busybox rm "		+	fileApk.getAbsolutePath()+" \n");
//			os.writeBytes("adb uninstall "	+	appInfo.packageName+" \n");
//			if(appInfo.packageName.equals("com.galaxyitv.launcher")){
//				os.writeBytes("adb reboot \n");
//			}
//			os.writeBytes("busybox rm -r "+appInfo.dataDir+" \n");
//		}
		
		os.writeBytes("sync \n");
		os.writeBytes("exit \n");
		int code = p.waitFor();
		Logger.i("msg","code = " + code);
//		fileApk.renameTo(new File("/system/app/", fileApk.getName()));
	}
	
	/**
	 * 保存数据到sp
	 * @param key
	 * @param value
	 */
	public static void savePreference(String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(PengDoctorApplication.mContext).edit().putString(key, value).commit();
	}
	
	
	//static 修饰的方法执行效率比较高，相当于c++中的内联函数
	public static void copyFile(String fileFromPath, String fileToPath) throws Exception {
		InputStream 	in 	= null;
		OutputStream 	out = null;
		try {
			in = new FileInputStream(fileFromPath);
			out = new FileOutputStream(fileToPath);
			int length = in.available();
			int len = (length % 1024 == 0) ? (length / 1024) : (length / 1024 + 1);
			byte[] temp = new byte[1024];
			for (int i = 0; i < len; i++) {
				in.read(temp);
				out.write(temp);
			}
		} finally {
			if (in != null) 	in.close();
			if (out != null) 	out.close();
		}
	}
}
