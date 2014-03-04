package com.hiveview.domybox.service.net;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.service.request.BaseGetRequest;
import com.hiveview.domybox.utils.Logger;

public class HttpGetConnector extends BaseHttpGetConnector{

	private final int REPEATS = 3;
	
	private String 		  urlStr;
	
	@SuppressWarnings("unused")
	private HttpGetConnector() {}

	public HttpGetConnector(BaseGetRequest request) {
		this.urlStr = request.executeToREST();
	}
	
	/**
	 * get data stream from net.
	 * @author kylinhuang
	 * @throws HiveException .
	 * */
	public InputStream getGetResponse(){
		InputStream in = null;
		HttpClient httpclient = new DefaultHttpClient();
		int count = 0;
		while(count<REPEATS){
			// 创建Get方法实例     
	        try {
	        	HttpGet httpgets = new HttpGet(urlStr);    
	        	String  strCookies =PengDoctorApplication.getCookie();
	        	httpgets.setHeader("Cookie", strCookies);
	        	

				HttpResponse response = httpclient.execute(httpgets);    
				HttpEntity entity = response.getEntity();    
				if (entity != null) {    
				    in = entity.getContent();
				    if (null == in) throw new RuntimeException();
	                count = REPEATS; 
				}
			} catch (ClientProtocolException e) {
				count++;
				e.printStackTrace();
			} catch (IllegalStateException e) {
				count++;
				e.printStackTrace();
			} catch (IOException e) {
				count++;
				e.printStackTrace();
			}  catch (RuntimeException e) {
				e.printStackTrace();
				count++; 
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					throw new ServiceException(e.getLocalizedMessage(), e);
				}
			}  
		}
		return in;
	}

}
