package com.hiveview.domybox.common;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ethernet.EthernetManager;
import android.widget.Toast;

import com.domy.service.DomyPreferences;
import com.hiveview.domybox.service.entity.AppInfoEntity;
import com.hiveview.domybox.utils.AppUtils;
import com.hiveview.domybox.utils.PullPaserCities;


public class PengDoctorApplication extends Application {
	
	public static Bitmap wallPager;
	public static String wallpaperUrl="";
	
	public static PengDoctorApplication instance;
	
	private static List<Cookie> cookies; 
	
	public static boolean isFrist = true;	//是否是第一次启动
	
	/**组件程序*/
	public static ArrayList<AppInfoEntity> appSystemList;
	
	/**已安装程序集合（非系统）*/
	public static ArrayList<AppInfoEntity> appInstalledList;	
	/**已安装程序包名集合（非系统）*/
	public static HashMap<String, AppInfoEntity> appinfo;
	public static Context mContext;
	private static String mVersion;
	private static long uiThreadID;
	public static List<String> list;
	private static Date now = null;  
	private static long mTime = 0;
	public static Map<String,Map<String,List<String>>> citiesMap;
	public static Map<String,String> zonesMap;
	private EtherNetReceiver mEtherNetReceiver;

	public static String DEFAULT_SDCARD_PATH = null;
	public static float mDisplayScale;
	
	public static int Display_hight = 0;
	public static int Display_width = 0;
	private static LinkedHashMap<String, Boolean> componentMap;
	private static SharedPreferences spCookies;
	
	public static long getUiThreadID() {
		return uiThreadID;
	}

	public static String getVersion() {
		return mVersion;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
//		instance = this;
//		mEtherNetReceiver = new EtherNetReceiver();
//		IntentFilter ether_filter = new IntentFilter();
//		ether_filter.addAction(EthernetManager.ETH_STATE_CHANGED_ACTION);
//		this.registerReceiver(mEtherNetReceiver, ether_filter);
		mContext = getBaseContext();
		mVersion = AppUtils.getVersionName(getApplicationContext());
		mDisplayScale = mContext.getResources().getDisplayMetrics().density;
		spCookies = mContext.getSharedPreferences(AppConstant.SP_COOKIES, Context.MODE_PRIVATE);
		
		try {
			InputStream inputStream1 = this.getClassLoader().getResourceAsStream("weathercn.xml");
			citiesMap = PullPaserCities.getCities(inputStream1);
			InputStream inputStream2 = this.getClassLoader().getResourceAsStream("weathercn.xml");
			zonesMap = PullPaserCities.getZones(inputStream2);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		
		DomyPreferences.initDomyKey(getApplicationContext());
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public static Date getNow() {
		return now;
	}

	public static void setNow(Date nowtime) {
		mTime = nowtime.getTime() - System.currentTimeMillis();
		now = new Date(System.currentTimeMillis() + mTime);
	}
	
	
	public static void getPackageNameList(){
		appinfo =	new HashMap<String, AppInfoEntity>();
		if(appInstalledList!=null){
			for (AppInfoEntity entity : appInstalledList) {
				appinfo.put(entity.getPackageName(), entity);
			}
		}
	}
	
	public static AppInfoEntity isInstalled(String packageName){
		for (AppInfoEntity entity : appInstalledList) {
			if(packageName.equals(entity.getPackageName())){
				return entity;
			}
		}
		return null;
	}

	/**获取组件集合*/
	public static LinkedHashMap<String, Boolean> getComponentCollection() {
		if(componentMap==null){
			componentMap = new LinkedHashMap<String,Boolean>();
			componentMap.put(AppConstant.PACKAGE_NAME_DOMYBOX, false);
			componentMap.put(AppConstant.PACKAGE_NAME_HAPPY_SHARE, false);
			componentMap.put(AppConstant.PACKAGE_NAME_HIVEVIEW, false);
			componentMap.put(AppConstant.PACKAGE_NAME_QIYI, false);
			componentMap.put(AppConstant.PACKAGE_NAME_LANUCHER, false);
			
		}
		return componentMap;
	}
	
	
	 public static String getCookie(){   
		 if (spCookies==null)  spCookies = mContext.getSharedPreferences(AppConstant.SP_COOKIES, Context.MODE_PRIVATE);
		 return spCookies.getString(AppConstant.SP_COOKIES, "");
    }
	 
    @SuppressLint("CommitPrefEdits")
	public static void saveCookie(List<Cookie> cookies){
    	if (cookies != null && cookies.size() > 0) {
        	StringBuilder sb = new StringBuilder();
        	for (Cookie ck : cookies) {
        		sb.append(ck.getName()).append('=').append(ck.getValue()).append(";");
        	}
        	String strCookies = sb.toString();
        	if (spCookies == null)  spCookies = mContext.getSharedPreferences(AppConstant.SP_COOKIES, Context.MODE_PRIVATE);
        		
    		Editor  edit = spCookies.edit();
    		edit.putString(AppConstant.SP_COOKIES, strCookies);
    		edit.commit();
    	}
    }
    
    public interface EtherListener{
    	void isEtherNetChanged(boolean connected);
    }
    EtherListener listener;
    public void setEtherChangeListener(EtherListener listener){
    	this.listener = listener;
    }
    public static boolean hasEtherNet = false;
    public class EtherNetReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			 if (EthernetManager.ETH_STATE_CHANGED_ACTION.equals(intent.getAction())){
				 
				 Toast.makeText(getApplicationContext(), intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)+"--", 0).show();
				 if(intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)==4){
					 hasEtherNet = false;
					 if(listener!=null)
						 listener.isEtherNetChanged(false);
				 }else if(intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)==5||intent.getIntExtra(EthernetManager.EXTRA_ETH_STATE, 0)==1){
					 hasEtherNet = true;
					 if(listener!=null)
						 listener.isEtherNetChanged(true);
				 }
			 }
		}

    }
	
	
}
