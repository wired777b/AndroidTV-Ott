package com.hiveview.domybox.service.request;

public class AppMarketListRequest extends BaseGetRequest {

	private int pageNo;
	private int pageSize;

	public AppMarketListRequest(int pageNo,int pageSize,String version) {
		this.version = version;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	@Override
	public String executeToREST() {
		return String.format(HIVEVIEW_API_GET_APP_LIST, pageNo, pageSize,version);
	}

}
