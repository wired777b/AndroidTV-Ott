package com.hiveview.domybox.service.entity;

public class AppMarketEntity extends BaseEntity {

	private int appId;	//应用标识
	private int seq;	//展示次序
	private int appType;//0：系统应用		 1：安装应用	
	
	
	private String bundlerId;	//应用启动包名称
	private String appName;		//应用名称
	private String appIcon;		//应用图标
	private String appDesc;		//应用描述
	private String tagName;		//标签 "娱乐、视频"
	private String developer;		//开发商
	private String versionNo;		//最新版本号
	private String appSize;		//应用大小
	private String versionDesc;		//版本描述
	private long ctime;		//更新时间
	private String versionUrl;		//下载地址
	private String md5;
	private String size;
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getAppType() {
		return appType;
	}
	public void setAppType(int appType) {
		this.appType = appType;
	}
	public String getBundlerId() {
		return bundlerId;
	}
	public void setBundlerId(String bundlerId) {
		this.bundlerId = bundlerId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppDesc() {
		return appDesc;
	}
	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getAppSize() {
		return appSize;
	}
	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}
	public String getVersionDesc() {
		return versionDesc;
	}
	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public String getVersionUrl() {
		return versionUrl;
	}
	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
}
