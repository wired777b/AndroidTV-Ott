package com.hiveview.domybox.utils;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

import android.os.Handler;
import android.os.Message;

import com.hiveview.domybox.common.AppConstant;
import com.hiveview.domybox.service.exception.ServiceException;

public class DownLoadThread extends Thread {

	private static final int DOWN_FINISH = 4489;
	private String urlstr;
	private int start;
	private int end;
	private HttpURLConnection connection;
	private RandomAccessFile randomAccessFile;
	private InputStream is;
	private Handler handler;
	private String locationpath;
	private static NumberFormat format = NumberFormat.getInstance();
	private String percent = null;


	public DownLoadThread(String url,String locationpath, int start, int end,Handler handler) {
		urlstr=url;
		this.start=start;
		this.end=end;
		this.handler=handler;
		this.locationpath=locationpath;
	}

	@Override
	public void run() {
		format.setMinimumFractionDigits(2);
		 try {
			 URL url = new URL(urlstr);
			 connection = (HttpURLConnection) url.openConnection();
			 connection.setConnectTimeout(5000);
			 connection.setRequestMethod("GET");
			
			 // 设置范围，格式为Range：bytes x-y;
			 connection.setRequestProperty("Range", "bytes="+start + "-" + end);
			 randomAccessFile = new RandomAccessFile(locationpath+"/"+urlstr.substring(urlstr.lastIndexOf("/")+1), "rwd");
			 randomAccessFile.seek(start);
			 // 将要下载的文件写到保存在保存路径下的文件中
			 is = connection.getInputStream();
			 byte[] buffer = new byte[2048];
			 int length=0 ;
			 long count = 0;
			 System.out.println("urlstr-------------------" + urlstr);
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
		
	}
	
	public void calculateProgress()
	{
		;
		System.out.println();
	}
	
}
