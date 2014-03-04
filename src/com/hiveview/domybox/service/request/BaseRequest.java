package com.hiveview.domybox.service.request;

/**
 * Request for  REST url 
 * */
public abstract class BaseRequest implements ApiConstant{
	
	protected String version;
	protected String key;
	protected String device;
	protected final String VERSION ="version";
	protected final String CLIENT_KEY = "client_key";
	protected final String DEVICES = "devices";
	protected final String VIDEO_ID  = "video_id";
	protected final String VIDEO_TYPE  = "video_type";
	protected final String DEVICE = "device";
	
	
	
	protected final String UUID = "uuid";
	protected final String MAC 	= "mac";
	protected final String DEVICEID = "deviceid";
	
	
	protected BaseRequest() {};

	
}
