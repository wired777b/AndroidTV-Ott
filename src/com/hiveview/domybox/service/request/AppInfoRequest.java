package com.hiveview.domybox.service.request;

public class AppInfoRequest extends BaseGetRequest {

	private String bundlerId;

	public AppInfoRequest( String version, String bundlerId) {
		this.version = version;
		this.bundlerId = bundlerId;
	}

	@Override
	public String executeToREST() {
		return String.format(HIVEVIEW_API_GET_APP_INFO, bundlerId,version);
	}

}
