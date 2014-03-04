package com.hiveview.domybox.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class StringUtils {
	/** 获取当前版本号 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();// 得到包管理对象
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);// 获取指定包的信息
			return info.versionName;// 获取版本
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "unkonwn";
		}
	}

	public static String converStreamToString(InputStream is) {
		
		if(is==null) return null;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		
		int count = 0 ;
		while(count < 3){
			
			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				break;
			} catch (IOException e) {
				count ++;
			} 
		}
		return buffer.toString();
	}

	public static String timeString(int time){
		time=time/1000;
		String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
  
    public static String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
	}
    
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	
    public static String getDateTime(long value) {
    	System.out.println(value);
    	Date date = new Date(value);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(format.format(date));
	  
	  return format.format(date);
     }
    
    //Convert Unix timestamp to normal date style  
	public static String TimeStamp2Date(String timestampString){  
	  Long timestamp = Long.parseLong(timestampString)*1000;  
	  String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(timestamp));  
	  return date;  
	} 
}
