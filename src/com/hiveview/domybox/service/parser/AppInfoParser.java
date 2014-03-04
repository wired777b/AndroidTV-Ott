package com.hiveview.domybox.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.domybox.service.entity.AppMarketEntity;
import com.hiveview.domybox.service.exception.ServiceException;
import com.hiveview.domybox.utils.StringUtils;

public class AppInfoParser extends BaseParser{

	@Override
	public ArrayList<AppMarketEntity> executeToObject(InputStream in) throws ServiceException {

		ArrayList<AppMarketEntity> list = new ArrayList<AppMarketEntity>();
		String jsonStr = StringUtils.converStreamToString(in);
		AppMarketEntity entity ;
		
		try {
			JSONObject obj = new JSONObject(jsonStr);
			
//			JSONObject dataObj = obj.getJSONObject("data");
			errorCode = obj.getString("code");
			
			JSONObject resultObj = obj.getJSONObject("result");
			
				entity = new AppMarketEntity();
				entity.setAppId(resultObj.getInt("appId"));
				entity.setSeq(resultObj.getInt("seq"));
				entity.setBundlerId(resultObj.getString("bundleId"));
				entity.setAppName(resultObj.getString("appName"));
				entity.setAppIcon(resultObj.getString("appIcon"));
				entity.setAppType(resultObj.getInt("appType"));
				entity.setTagName(resultObj.getString("tagName"));

				entity.setDeveloper(resultObj.getString("developer"));
				entity.setAppDesc(resultObj.getString("appDesc"));
				
				
				JSONObject versionObj = resultObj.getJSONObject("version");
				entity.setVersionNo(versionObj.getString("versionNo"));
				entity.setAppSize(versionObj.getString("appSize"));
				entity.setVersionDesc(versionObj.getString("versionDesc"));
				entity.setCtime(versionObj.getLong("ctime"));
				entity.setVersionUrl(versionObj.getString("versionUrl"));
				
				entity.setMd5(versionObj.getString("md5"));
				entity.setSize(versionObj.getString("size"));
				list.add(entity);
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ServiceException(10);
		}
		return list;
	}

	@Override
	public String getErrorCode(){
		
		return errorCode;
		
	}

}
