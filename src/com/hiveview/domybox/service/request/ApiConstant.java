package com.hiveview.domybox.service.request;

import com.hiveview.domybox.utils.AppUtils;





public interface ApiConstant {
	
	int CLIENT_KEY=1;
	int VERSION	=1;
	String UUID = "2013jstx0001";
	
	String APP_VERSION="1.0.0";
	String APP_KEY = AppUtils.getAppClientKey();
	
	String HIVEVIEW_DOMAIN 	= 	"http://124.207.119.79";
	
	/**(开机鉴权接口)
	 * /api/sys/deviceCheck/
	 * 124.207.119.79/api/sys/deviceCheck/%s.json
	 * http://<Server>/api/sys/deviceCheck/<version>.json */
	String HIVEVIEW_API_DEVICE_CHECK =HIVEVIEW_DOMAIN + "/api/sys/deviceCheck/%s.json";
	
	/**(取天气接口)
	 * http://124.207.119.79/api/open/weather/getWeatherInfo/01010101/1.0.0.json
	 * http://<Server>/api/weather/getWeatherInfo/<hsienNo>/<version>.json
	 * */
	String HIVEVIEW_API_GET_WEATHER = HIVEVIEW_DOMAIN + "/api/open/weather/getWeatherInfo/%s/%s.json";
	 
	/**应用列表接口 
	 * http://<Server>/api/open/app/getAppList/<pageNo>/<pageSize>/<version>.json */
	String HIVEVIEW_API_GET_APP_LIST = HIVEVIEW_DOMAIN + "/api/open/app/getAppList/%s/%s/%s.json";

	/** 
	 *  http://124.207.119.79/api/open/app/getAppInfo/com.hiveview.tv.onlive/1.json 
	*/	
	String HIVEVIEW_API_GET_APP_INFO = HIVEVIEW_DOMAIN + "/api/open/app/getAppInfo/%s/%s.json";
	
	
	/*3、升级接口 */
//	http://124.207.119.79/api/open/sys/version/getInfo /1.json
	String HIVEVIEW_API_GET_SYS_UPDATA = HIVEVIEW_DOMAIN + "/api/open/sys/version/getInfo/%s.json";
	
}
