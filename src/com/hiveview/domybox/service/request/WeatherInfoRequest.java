package com.hiveview.domybox.service.request;


public class WeatherInfoRequest extends BaseGetRequest {
	
	private String cityId;
	
	public WeatherInfoRequest(String cityId ,String version){
		this.cityId = cityId;
		this.version = version;
	}

	@Override
	public String executeToREST() {
		return String.format(HIVEVIEW_API_GET_WEATHER,cityId,version);
	}


}
