package com.hiveview.domybox.service.request;

public class UpDataRequest extends BaseGetRequest {


	public UpDataRequest(String version) {
		this.version = version;
	}

	@Override
	public String executeToREST() {
		return String.format(HIVEVIEW_API_GET_SYS_UPDATA , version);
	}

}
