package com.hiveview.domybox.activity;


import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.service.net.HttpTaskManager;
import com.hiveview.domybox.utils.Logger;

public class BaseActivity extends Activity {
	
	protected HttpTaskManager taskManager = HttpTaskManager.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		PengDoctorApplication.Display_width = metric.widthPixels; 
		Logger.i("Display_width", metric.widthPixels+"");
		
		PengDoctorApplication.Display_hight = metric.heightPixels; 
		Logger.i("Display_hight", metric.heightPixels+"");
	}

}
