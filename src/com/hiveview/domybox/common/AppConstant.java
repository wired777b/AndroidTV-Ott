package com.hiveview.domybox.common;

import android.os.Environment;
import android.os.StatFs;

public interface AppConstant {
	
	
	
	public static StatFs statfsROM 	= 	new StatFs(Environment.getDataDirectory().getAbsolutePath());
	public static StatFs statfsSD 	= 	new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	
	public static final String LOCATION_PATH	=	Environment.getExternalStorageDirectory()+"/"+"DomyBoxLanucher"+"/"+"DownLoad";
	
	public static final String SYSTEM_APP_PATH	=	Environment.getExternalStorageDirectory()+"/"+"DomyBoxLanucher"+"/"+"systemApp";
	
	public static final String WALLPAGER_PATH=Environment.getExternalStorageDirectory()+"/"+"DomyBoxLanucher"+"/"+"wallpager";
	
	public static final String WALLPAGER_PATH_SD="/mnt/sdcard/"+"/"+"DomyBoxLanucher"+"/"+"wallpager";
	
	//墙纸
	public static final String SP_WALL_PAGER="wallpager";
	
	public static final String SP_UPDATA="updata";
	
	public static final String SP_COOKIES="cookies";
	
	
	public static final int MSG_WALLPAPER_CHANGE = 468;
	
	public static final int MSG_DIALOG_PROGRESS 	= 2001;	//
	public static final int MSG_DIALOG_GETSIZE 		= 2002;	//获取文件大小

	public static final int MSG_DIALOG_UNINSTALL 	= 2003;	//卸载进度条
	
	public static final int MSG_DIALOG_INSTALL 		= 2004;	//获取安装
	public static final int MSG_GET_APPINFO 		= 2005;	//获取都App信息
	
	public static final int MSG_DIALOG_INSTALL_COMPLETE	= 2006;	//安装完成
	
	public static final int MSG_DOWNLOAD_DEFEATED	= 2007;	//下载失败
	
	public static final String APP_CLIENTKEY 	= "1";	//key
	public static final String APP_VERSION 		= "1";	//version
	
	public static final String PACKAGE_NAME_DOMYBOX 	= "com.hiveview.domybox";		//domybox
	public static final String PACKAGE_NAME_HIVEVIEW 	= "com.hiveview.tv.onlive";		//version
	public static final String PACKAGE_NAME_HAPPY_SHARE = "com.videoplayer";			//version
	/** 奇异  */
	public static final String PACKAGE_NAME_QIYI 		= "com.qiyi.video";				//version
	/**多媒体*/
	public static final String PACKAGE_NAME_MULTI_MEDIA = "com.amlogic.mediacenter";
	
	/** lanucher */
	public static final String PACKAGE_NAME_LANUCHER 	= "com.hiveview.domybox";
	
	/** 卸载 成功 */
	public static final int UNINSTALL_SUCCEE 	= 2010;
	
	/** 卸载 失败 */
	public static final int UNINSTALL_DEFEATE 	= UNINSTALL_SUCCEE	+	1;
	
	/** 安装 成功 */
	public static final int INSTALL_SUCCEE 		= UNINSTALL_SUCCEE 	+	2;
	
	/** 安装 失败 */
	public static final int INSTALL_DEFEATE 	= UNINSTALL_SUCCEE	+	3;
	
	
	/**
	 * number from - 1 to 9 
	 * */
		int NO_0 = 0;
		int NO_1 = 1;
		int NO_2 = 2;
		int NO_3 = 3;
		int NO_4 = 4;
		int NO_5 = 5;
		int NO_6 = 6;
		int NO_7 = 7;
		int NO_8 = 8;
		int NO_9 = 9;
		int NO_10 = 10;
		int NO_40 = 40;
		int NO_41 = 41;
		int NO_42 = 42;
	

}
