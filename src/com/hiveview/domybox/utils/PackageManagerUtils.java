package com.hiveview.domybox.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageManagerUtils {
	
	
	public static ApplicationInfo getApplicationInfo(Context context,String packageName){
		
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> appinfoList = pm.getInstalledApplications(ApplicationInfo.FLAG_SYSTEM);
		
		for(ApplicationInfo appInfo : appinfoList){
			
			if(appInfo.packageName.equals(packageName)) {
				
				return appInfo;
			}
		}
		
		
		return null;
		
	}
	
	
	public static PackageInfo getPackageInfo(Context context,String path) {
		
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);
        
        return info;
        
    }
	
}
