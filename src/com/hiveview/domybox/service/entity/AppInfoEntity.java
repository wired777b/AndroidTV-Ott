package com.hiveview.domybox.service.entity;

import android.graphics.drawable.Drawable;

public class AppInfoEntity extends BaseEntity {

	private String 	appName;
	private String 	packageName;
	private String 	versionName;
	private int 	versionCode;
	private Drawable appIcon;
	private Boolean isUpdating =false;
	
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getVersionName() {
		return versionName;
	}
	
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	public int getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	
	public Drawable getAppIcon() {
		return appIcon;
	}
	
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public Boolean getIsUpdating() {
		return isUpdating;
	}

	public void setIsUpdating(Boolean isUpdating) {
		this.isUpdating = isUpdating;
	}
	
	
   
}
