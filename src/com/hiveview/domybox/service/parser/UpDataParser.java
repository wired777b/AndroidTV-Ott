package com.hiveview.domybox.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.service.entity.SYSUpDataEntity;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.utils.StringUtils;

public class UpDataParser extends BaseParser{

	@Override
	public ArrayList<AppMarketEntity> executeToObject(InputStream in) throws ServiceException {
		return null;
	}

	public SYSUpDataEntity executeObject(InputStream in) throws ServiceException {
		String jsonStr = StringUtils.converStreamToString(in);
		SYSUpDataEntity entity ;
		
		try {
			JSONObject obj = new JSONObject(jsonStr);
			
//			JSONObject dataObj = obj.getJSONObject("data");
			errorCode = obj.getString("code");
			
			JSONObject resultObj = obj.getJSONObject("result");
			
			entity = new SYSUpDataEntity();
			entity.setVersion(resultObj.getString("version"));
			entity.setUpdateTime(resultObj.getInt("updateTime"));
			entity.setType(resultObj.getInt("type"));
			entity.setFeatures(resultObj.getString("features"));
			entity.setUrl(resultObj.getString("url"));
			entity.setMd5(resultObj.getString("md5"));
			entity.setSize(resultObj.getInt("size"));
				
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ServiceException(10);
		}
		return entity;
	}
	
	@Override
	public String getErrorCode(){
		
		return errorCode;
		
	}

}
