package com.hiveview.domybox.service.entity;

public class SYSUpDataEntity extends BaseEntity {


    private String version;	
	private String features;	
	private String url;	
	private String md5;	
	private int size;	
	private int updateTime;		
	private int type;
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}		
	
}
