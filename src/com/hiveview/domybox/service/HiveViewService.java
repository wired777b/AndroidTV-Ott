package com.hiveview.domybox.service;

import java.io.InputStream;
import java.util.ArrayList;

import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.service.entity.SYSUpDataEntity;
import com.hiveview.domybox.service.entity.WeatherInfo;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.service.net.BaseHttpGetConnector;
import com.hiveview.domybox.service.net.BaseHttpPostConnector;
import com.hiveview.domybox.service.net.HttpGetConnector;
import com.hiveview.domybox.service.net.HttpPostConnector;
import com.hiveview.domybox.service.parser.AppInfoParser;
import com.hiveview.domybox.service.parser.AppMarketListParser;
import com.hiveview.domybox.service.parser.CommonParser;
import com.hiveview.domybox.service.parser.UpDataParser;
import com.hiveview.domybox.service.parser.WeatherInfoParser;
import com.hiveview.domybox.service.request.ApiConstant;
import com.hiveview.domybox.service.request.AppInfoRequest;
import com.hiveview.domybox.service.request.AppMarketListRequest;
import com.hiveview.domybox.service.request.BaseGetRequest;
import com.hiveview.domybox.service.request.BasePostRequest;
import com.hiveview.domybox.service.request.DevCheckRequest;
import com.hiveview.domybox.service.request.UpDataRequest;
import com.hiveview.domybox.service.request.WeatherInfoRequest;
import com.hiveview.domybox.utils.AppUtils;

public class HiveViewService {
	
	//得到天气预报的信息
	public ArrayList<WeatherInfo> getWeatherInfoList(String cityId,String version) throws Exception {
		BaseGetRequest request = new WeatherInfoRequest(cityId,version);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();
		
		WeatherInfoParser parser = new WeatherInfoParser();
		ArrayList<WeatherInfo > list = parser.executeToObject(in);
		String errorCode  = parser.getErrorCode();
		if(errorCode.equals("N000000")) return list;
		else if(errorCode.equals("N000006")) {
			//重新鉴权
			String dateStr =	deviceCheck(
									ApiConstant.APP_VERSION, 
									AppUtils.getLocaldeviceId(PengDoctorApplication.mContext), 
									AppUtils.getBoxDeviceMac(), 
									ApiConstant.UUID);
			if(dateStr!=null)getWeatherInfoList(cityId, version);
			
			
			
		}else {
			throw new ServiceException(errorCode);
		}
		return list;
	}
	
	
	/** 应用列表接口 
	 * http://<Server>/api/open/app/getAppList/<pageNo>/<pageSize>/<version>.json
	 * @throws Exception 
	 * @throws ServiceException 
	 * */ 
	public ArrayList<AppMarketEntity> getAppMarketList(int pageNo,int pageSize,String version) throws ServiceException, Exception {
		BaseGetRequest request 	= new AppMarketListRequest(pageNo,pageSize,version);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();
		
		AppMarketListParser parser = new AppMarketListParser();
		ArrayList<AppMarketEntity > list = parser.executeToObject(in);
		String errorCode  = parser.getErrorCode();
		if(errorCode.equals("N000000")) return list;
		
		else if(errorCode.equals("N000006")) {
			//重新鉴权
			String dateStr =	deviceCheck(
									ApiConstant.APP_VERSION, 
									AppUtils.getLocaldeviceId(PengDoctorApplication.mContext), 
									AppUtils.getBoxDeviceMac(), 
									ApiConstant.UUID);
			if(dateStr!=null)getAppMarketList(pageNo, pageSize, version);
			
		}else {
			throw new ServiceException(errorCode);
		}
		return list;
	}
	
	/**2、应用详情接口	
	 * @throws Exception 
	 * @throws ServiceException */ 
	public ArrayList<AppMarketEntity> getAppInfo(String version,String bundlerId) throws ServiceException, Exception {
		BaseGetRequest request = new AppInfoRequest(version,bundlerId);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();
		
		AppInfoParser parser = new AppInfoParser();
		ArrayList<AppMarketEntity > list = parser.executeToObject(in);
		String errorCode  = parser.getErrorCode();
		if(errorCode.equals("N000000")) return list;
		else if(errorCode.equals("N000006")) {
			//重新鉴权
			String dateStr =	deviceCheck(
									ApiConstant.APP_VERSION, 
									AppUtils.getLocaldeviceId(PengDoctorApplication.mContext), 
									AppUtils.getBoxDeviceMac(), 
									ApiConstant.UUID);
			if(dateStr!=null)getAppInfo(version, bundlerId);
		}else {
			throw new ServiceException(errorCode);
		}
		return list;
		
	}

	
	
	/**3、升级接口		
	 * @throws Exception 
	 * @throws ServiceException */ 
	public SYSUpDataEntity getInfo(String version) throws ServiceException, Exception {
		BaseGetRequest request = new UpDataRequest(version);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();
		
		UpDataParser parser = new UpDataParser();
		SYSUpDataEntity entity = parser.executeObject(in);
		String errorCode  = parser.getErrorCode();
		if(errorCode.equals("N000000")) return entity;
		else if(errorCode.equals("N000006")) {
			//重新鉴权
			String dateStr =	deviceCheck(
									ApiConstant.APP_VERSION, 
									AppUtils.getLocaldeviceId(PengDoctorApplication.mContext), 
									AppUtils.getBoxDeviceMac(), 
									ApiConstant.UUID);
			if(dateStr!=null)getInfo(version);
			
		}else {
			throw new ServiceException(errorCode);
		}
		return entity;
		
	}
	
	
	/** 鉴权
	 * http://<Server>/api/sys/deviceCheck/<version>.json
	 * 	deviceid	设备id
		mac			设备mac
		uuid		渠道密钥
	 *  */
	public String deviceCheck(String version,String deviceid,String mac,String uuid) throws ServiceException,Exception{
			BasePostRequest request = new DevCheckRequest(version, deviceid, mac,uuid);
			BaseHttpPostConnector connector = new HttpPostConnector(request);
			InputStream in = connector.getPostResponse();
			CommonParser parser = new CommonParser();
			parser.executeToObject(in);
			String errorCode  = parser.getErrorCode();
			if(errorCode.equals("N000000")) 
				return parser.getDateString();
			else throw new ServiceException(errorCode);
		
	}
}
