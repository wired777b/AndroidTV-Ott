package com.hiveview.domybox.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.activity.adapter.AbstractWheelTextAdapter;
import com.hiveview.domybox.activity.adapter.WheelViewAdapter;
import com.hiveview.domybox.common.PengDoctorApplication;
import com.hiveview.domybox.common.image.ImageLoadView;
import com.hiveview.domybox.common.image.ImageRequest;
import com.hiveview.domybox.common.image.ImageTaskManager;
import com.hiveview.domybox.service.HiveViewService;
import com.hiveview.domybox.service.dao.CommonPreferenceDAO;
import com.hiveview.domybox.service.entity.WeatherInfo;
import com.hiveview.domybox.service.net.BaseHttpGetConnector;
import com.hiveview.domybox.service.net.HttpGetConnector;
import com.hiveview.domybox.service.net.HttpTaskManager;
import com.hiveview.domybox.service.request.ApiConstant;
import com.hiveview.domybox.service.request.BaseGetRequest;
import com.hiveview.domybox.utils.TypefaceUtils;
import com.hiveview.domybox.view.MarqueeText;
import com.hiveview.domybox.view.WheelView;

@SuppressLint("HandlerLeak")
public class MyWeatherActivity extends BaseActivity {
	
	public static final int FINISH_DATA = 10000;
	private static final int WALLPAPER_FINISH = 4523;
	//三级地区View
	private WheelView lv_provice;
	private WheelView lv_city;
	private WheelView lv_zone;
	private MyKeyDownListener keyDownlistener;
	private MyFocusChangeListener focusListener;
	//三级地区适配器
	private CountryAdapter proviceAdapter;
	private CountryAdapter cityAdapter;
	private CountryAdapter zoneAdapter;
	//城市集合
	private static Map<String,Map<String,List<String>>> citiesMap = PengDoctorApplication.citiesMap;;
	private ArrayList<WeatherInfo> weatherList;
	private WheelView wheelView;
	protected Bitmap bitmapWallPaper;
	//wheel的中间背景色
	private ImageView iv_center_black_zone;
	private ImageView iv_center_black_city;
	//省级集合
	private Object[] proviceArrays;
	private Object[] cityArray;
	private Object[] zoneArray;
	private SharedPreferences sp ;
	private ImageView fullparent;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			if(msg.what==FINISH_DATA)
			{
				if(weatherList!=null&& !weatherList.isEmpty()){
					progressDialogView.setVisibility(View.INVISIBLE);
					updateUI();
					String wallpaperUrl = null;
					switch (weatherList.get(0).getType()) {
					case 1:
						wallpaperUrl = weatherList.get(0).getDayWallpaper();
						break;
					case 2:
						wallpaperUrl = weatherList.get(0).getNightWallpaper();
						break;

					default:
						break;
					}
					
					
					if(!TextUtils.isEmpty(PengDoctorApplication.wallpaperUrl)&&!PengDoctorApplication.wallpaperUrl.equals(wallpaperUrl)) {
						getBackgroundImage(wallpaperUrl);
						PengDoctorApplication.wallpaperUrl = wallpaperUrl;
					}
					//}
				}
			}
			if(msg.what == WALLPAPER_FINISH)
			{
				if(null != bitmapWallPaper)
				{
					fullparent.setImageBitmap(bitmapWallPaper);
					
				}
				else
				{
					fullparent.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.iv_main_bg));
					
				}
			}
			
		}
	};
	private View progressDialogView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_weather);
		separator(citiesMap);
		sp = getSharedPreferences("weatherSet", Context.MODE_PRIVATE);
		init();
		getWeatherInfo();
		
		cityArrayFill();
		zoneArrayFill();
		//省市区的适配器
		proviceAdapter = new CountryAdapter(PengDoctorApplication.mContext,proviceArrays);
		cityAdapter = new CountryAdapter(PengDoctorApplication.mContext,cityArray);
		zoneAdapter = new CountryAdapter(PengDoctorApplication.mContext,zoneArray);

		setWVAdapter();
		lv_zone.requestFocus();
		
	}
	/**
	 * 修改wheelview的显示样式
	 */
	private void setWVAdapter() {
		lv_provice.setViewAdapter(proviceAdapter);
		lv_city.setViewAdapter(cityAdapter);
		lv_zone.setViewAdapter(zoneAdapter);
		lv_provice.setCurrentItem(sp.getInt("provice_index",0));
		lv_provice.setVisibleItems(3);
		lv_provice.setCyclic(true);
		if(cityArray.length == 1)
		{
			lv_city.setVisibleItems(1);
			lv_city.setCyclic(false);
		} 
		else if(cityArray.length == 2)
		{
			lv_city.setVisibleItems(2);
			lv_city.setCyclic(false);
		}
		else
		{
			lv_city.setVisibleItems(3);
			lv_city.setCyclic(true);
		}

		if(zoneArray.length == 1)
		{
			lv_zone.setVisibleItems(1);
			lv_zone.setCyclic(false);
		}
		else if(zoneArray.length == 2)
		{
			lv_zone.setVisibleItems(2);
			lv_zone.setCyclic(false);
		}
		else
		{
			lv_zone.setVisibleItems(3);
			lv_zone.setCyclic(true);
		}
		lv_city.setCurrentItem(sp.getInt("city_index",0));
		lv_zone.requestFocus();
		
		if(cityArray.length == 1)
		{
			lv_city.setVisibleItems(1);
			lv_city.setCyclic(false);
		} 
		else if(cityArray.length == 2)
		{
			lv_city.setVisibleItems(2);
			lv_city.setCyclic(false);
		}
		else
		{
			lv_city.setVisibleItems(3);
			lv_city.setCyclic(true);
		}
		//获取指定城市名的索引
		lv_city.setCurrentItem(getCityIndex());

		if(zoneArray.length == 1)
		{
			lv_zone.setVisibleItems(1);
			lv_zone.setCyclic(false);
		}
		else if(zoneArray.length == 2)
		{
			lv_zone.setVisibleItems(2);
			lv_zone.setCyclic(false);
		}
		else
		{
			lv_zone.setVisibleItems(3);
			lv_zone.setCyclic(true);
		}
		lv_zone.setCurrentItem(sp.getInt("zone_index",0));
		
		if(cityArray.length >0)
		{
			iv_center_black_city.setVisibility(View.VISIBLE);
		}
		if(zoneArray.length >0)
		{
			iv_center_black_zone.setVisibility(View.VISIBLE);
		}
	}
	
	private int getCityIndex() {
		String city_item = sp.getString("city_item", "北京");
		for(int i = 0 ; i < cityArray.length ; i++)
		{
			if(city_item.equals(cityArray[i]))
			{
				return i;
			}
		}
		return 0;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(null != PengDoctorApplication.wallPager)
		{
			fullparent.setImageBitmap(PengDoctorApplication.wallPager);
		}
		
	}
	
	/**
	 * 为市级数组赋值
	 */
	private void cityArrayFill() {
		String proviceName = sp.getString("provice_item", "直辖市");
		cityMap = citiesMap.get(proviceName);
		Set<String> keySet = cityMap.keySet();
		cityArray = keySet.toArray();
	}
	/**
	 * 为区级数组赋值
	 */
	private void zoneArrayFill() {
		String cityName = sp.getString("city_item", "北京");
		List<String> zoneList = cityMap.get(cityName);
		zoneArray = zoneList.toArray();
	}
	
	

	@SuppressLint("NewApi")
	public void init(){
		fullparent = (ImageView) findViewById(R.id.fullparent);
		//将天气模块的背景图与主页面的背景图保持一致
		if(PengDoctorApplication.wallPager != null)
			fullparent.setImageBitmap((PengDoctorApplication.wallPager));
		else
			fullparent.setBackgroundResource(R.drawable.iv_main_bg);
		TextView weather_title = (TextView) this.findViewById(R.id.weather_title);
		TextView weather_title1 = (TextView) this.findViewById(R.id.weather_title1);
		lv_provice = (WheelView) this.findViewById(R.id.province);
		lv_city = (WheelView) this.findViewById(R.id.city);
		lv_zone = (WheelView) this.findViewById(R.id.zone);
		progressDialogView 	= 	findViewById(R.id.iv_hiveload);
		focusListener = new MyFocusChangeListener();

		//设置点击的监听
		lv_provice.setOnFocusChangeListener(focusListener);
		lv_city.setOnFocusChangeListener(focusListener);
		lv_zone.setOnFocusChangeListener(focusListener);
		
		iv_center_black_zone = (ImageView) findViewById(R.id.iv_center_black_zone);
		iv_center_black_city = (ImageView) findViewById(R.id.iv_center_black_city);

		//设置字体
		setTextType();

		
		
		
		keyDownlistener = new MyKeyDownListener();
		//设置监听
		lv_provice.setOnKeyListener(keyDownlistener);
		lv_city.setOnKeyListener(keyDownlistener);
		lv_zone.setOnKeyListener(keyDownlistener);
		//alpha
		lv_provice.setAlpha(204);
		lv_city.setAlpha(204);
		lv_zone.setAlpha(204);
		
		weather_title.setTypeface(TypefaceUtils.getStandardfFontFace());
		weather_title1.setTypeface(TypefaceUtils.getStandardfFontFace());

		
		
	}
	
	/**
	 * 分解省的map集合
	 */
	private Object[] separator(Map<String, Map<String, List<String>>> map) 
	{
		Set<String> proviceSet = map.keySet();
		proviceArrays = proviceSet.toArray();
		return proviceArrays;
	}
	
	private void getWeatherInfo(){
		progressDialogView.setVisibility(View.VISIBLE);
		HttpTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				HiveViewService service = new HiveViewService();
				CommonPreferenceDAO dao = new CommonPreferenceDAO(getApplicationContext());
				try {
					weatherList = service.getWeatherInfoList(dao.getCityID(),ApiConstant.APP_VERSION);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(FINISH_DATA);
			}
		});
	}
	private void getBackgroundImage(final String wallpaperUrl) {
		ImageTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				BaseGetRequest request = new ImageRequest(wallpaperUrl);
				BaseHttpGetConnector connector = new HttpGetConnector(request);
				InputStream in = connector.getGetResponse();
				if(in!=null){
					bitmapWallPaper = BitmapFactory.decodeStream(in);
					PengDoctorApplication.wallPager = bitmapWallPaper;
					mHandler.sendEmptyMessage(WALLPAPER_FINISH);
				}
				
			}
		});
		
	}
	
	
	

	
	/**
	 * 焦点改变的监听
	 * @author xlk
	 *
	 */
	@SuppressLint("NewApi")
	class MyFocusChangeListener implements OnFocusChangeListener{


		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			Drawable centerDrawable ;
			WheelView view = (WheelView)v;
	        if(hasFocus)
	        {    	
	        	wheelView = (WheelView)v;
	        	centerDrawable = MyWeatherActivity.this.getResources().getDrawable(R.drawable.checked);
	        	centerDrawable.setAlpha(255);
	        }
	        else
	        {
	        	centerDrawable = MyWeatherActivity.this.getResources().getDrawable(R.drawable.wheel_bg);
				centerDrawable.setAlpha(0);
				if(view == lv_city)
				{
					if(lv_zone.getViewAdapter().getItemsCount()==0)
					{
						Object[] obj = new Object[0];
						CountryAdapter adapter = new CountryAdapter(PengDoctorApplication.mContext,obj);
						lv_city.setViewAdapter(adapter);
						lv_zone.setViewAdapter(adapter);
					}
					if(lv_provice.hasFocus())
					{
						iv_center_black_zone.setVisibility(View.INVISIBLE);
						iv_center_black_city.setVisibility(View.INVISIBLE);
					}

			 }else if(view == lv_zone){
					
					Object[] obj = new Object[0];
					CountryAdapter adapter = new CountryAdapter(PengDoctorApplication.mContext,obj);
					lv_zone.setViewAdapter(adapter);
					iv_center_black_zone.setVisibility(View.INVISIBLE);
				}
				
	        }
	        if(hasFocus && view == lv_provice)
	        {
	        	Object[] obj = new Object[0];
	        	CountryAdapter adapter = new CountryAdapter(PengDoctorApplication.mContext,obj);
	        	lv_city.setViewAdapter(adapter);
				lv_zone.setViewAdapter(adapter);

	        }
	        view.setCenterDrawable(centerDrawable);
	       
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT)
		{
			if(wheelView == lv_provice)
			{
				if(lv_city.getViewAdapter().getItemsCount()==0)
				{
					return true;
				}
			}
			else if(wheelView == lv_city)
			{
				if(lv_zone.getViewAdapter().getItemsCount()==0)
				{
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * @author jz
	 *
	 */
	class MyKeyDownListener implements OnKeyListener
	{
		
		private WheelViewAdapter viewAdapter;
		private Editor editor = sp.edit();
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) 
		{
			if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN)
			{
				
				
				viewAdapter = wheelView.getViewAdapter();
				int index = wheelView.getCurrentItem();
				index++;
				if(index >= viewAdapter.getItemsCount())
				{
					index = index - viewAdapter.getItemsCount();
				}
				wheelView.setCurrentItem(index,true);
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN)
			{
				
				viewAdapter = wheelView.getViewAdapter();
				int index = wheelView.getCurrentItem();
				index--;
				if(index < 0)
				{
					index = index + viewAdapter.getItemsCount();
				}
				wheelView.setCurrentItem(index,true);
			 }
			
			else if(wheelView == lv_provice&&keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN)
			{
			
				selectCities(lv_provice.getCurrentItem());
				lv_city.setCurrentItem(0);
				iv_center_black_city.setVisibility(View.VISIBLE);
				CountryAdapter adapter = (CountryAdapter) lv_provice.getViewAdapter();
				
				editor.putString("provice_item", adapter.getItemText(lv_provice.getCurrentItem()).toString());
				editor.putInt("provice_index", lv_provice.getCurrentItem());
				editor.commit();
			}
			else if(wheelView == lv_city&&keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN)
			{
				
				selectZones(lv_city.getCurrentItem());
				lv_zone.setCurrentItem(0);
				iv_center_black_zone.setVisibility(View.VISIBLE);
				CountryAdapter adapter = (CountryAdapter) lv_city.getViewAdapter();
				
				editor.putString("city_item", adapter.getItemText(lv_city.getCurrentItem()).toString());
				editor.putInt("city_index", lv_city.getCurrentItem());
				editor.commit();
			}
			else if(wheelView == lv_zone&&keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN)
			{
			
				lv_zone.setCurrentItem(lv_zone.getCurrentItem());
				CountryAdapter adapter = (CountryAdapter) lv_zone.getViewAdapter();
				
				editor.putString("zone_item", adapter.getItemText(lv_zone.getCurrentItem()).toString());
				editor.putInt("zone_index", lv_zone.getCurrentItem());
				editor.commit();
				
				clickZone(lv_zone.getCurrentItem());
			}
			
			
			return false;
		
		}
		
	}
	
	
	private Map<String, List<String>> cityMap;
	private void selectCities(int index)
	{
		cityArray = citiesMap.get(proviceArrays[index]).keySet().toArray();
		CountryAdapter adapter = new CountryAdapter(PengDoctorApplication.mContext,cityArray);
		if(cityArray.length == 1)
		{
			lv_city.setVisibleItems(1);
			lv_city.setCyclic(false);
		}
		else
		{
			lv_city.setVisibleItems(3);
			lv_city.setCyclic(true);
		}

		lv_city.setViewAdapter(adapter);
	}
	
	private void selectZones(int index){
		String zoneValue = sp.getString("provice_item", "直辖市");
		Map<String,List<String>> map = citiesMap.get(zoneValue);
		String cityName = cityArray[index].toString();
		zoneArray = map.get(cityName).toArray();
		if(zoneArray.length == 1)
		{
			lv_zone.setVisibleItems(1);
			lv_zone.setCyclic(false);
		}
		else
		{
			lv_zone.setVisibleItems(3);
			lv_zone.setCyclic(true);
		}
		CountryAdapter adapter = new CountryAdapter(PengDoctorApplication.mContext,zoneArray);
		lv_zone.setViewAdapter(adapter);
	}
	
	private void clickZone(int index) {
		progressDialogView.setVisibility(View.VISIBLE);
		//二级市的名字
		if(zoneArray.length == 1)
		{
			index = 0;
			lv_zone.setVisibleItems(1);
			lv_zone.setCyclic(false);
		}
		//三级区的名字
		String zoneName = zoneArray[index].toString();
		String cityId = PengDoctorApplication.zonesMap.get(zoneName);
		CommonPreferenceDAO dao = new CommonPreferenceDAO(getApplicationContext());
		dao.setCityId(cityId);
		
		getWeatherInfo();
	}
	
	/**
	 * 自定义适配器
	 * 可匹配XML文件以及数组形式的数据
     * 如需其他类型数据可自行扩展
	 * @author jz
	 *
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		Object[] areaArray = null;
        protected CountryAdapter(Context context,int resourceId){
        	super(context, R.layout.text_item, resourceId);
        	areaArray = MyWeatherActivity.this.getResources().getStringArray(resourceId);
        }
        
        public CountryAdapter(Context context,Object[] array){
        	super(context, R.layout.text_item, NO_RESOURCE);
        	areaArray = array;
        }

        @SuppressLint("NewApi")
		@Override
        public View getItem(final int index, View cachedView, ViewGroup parent){
        	
        	View view = super.getItem(index, cachedView, parent);
        	MarqueeText tv = (MarqueeText) view.findViewById(R.id.tv_marquee);
            tv.setTypeface(TypefaceUtils.getStandardfFontFace());

            String content = getItemText(index).toString();
            int length = content.length();
            switch (length) {
			case 2:
				tv.setTextSize(100/3-3);
				break;
			case 3:
				tv.setTextSize(100/3-3);
				break;
			case 4:
				tv.setTextSize(110/4-3);
				break;
			case 5:
				tv.setTextSize(110/5-3);
				break;
			case 6:
				tv.setTextSize(110/6-3);
				break;
			case 7:
				tv.setTextSize(110/7-3);
				break;

			}
        	tv.setText(getItemText(index));
            return view;
        }
        
        
        @Override
        public int getItemsCount() {
            return areaArray.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) 
        {
            return areaArray[index].toString();
        }
    }


	
	/**
	 * 设置全部的字体样式
	 * @param view
	 */
	private void setTextType() {
		Typeface standardfFontFace = TypefaceUtils.getStandardfFontFace();
		Typeface ce35FontFace = TypefaceUtils.getCE35FontFace();
		//今天的天气
		RelativeLayout today_weather = (RelativeLayout) this.findViewById(R.id.today_weather);
		TextView area = (TextView) today_weather.findViewById(R.id.area);
		TextView mouth = (TextView) today_weather.findViewById(R.id.mouth);
		TextView day = (TextView) today_weather.findViewById(R.id.day);
		TextView temperature = (TextView) today_weather.findViewById(R.id.temperature);
		TextView daytime = (TextView) today_weather.findViewById(R.id.daytime);
		TextView describe = (TextView) today_weather.findViewById(R.id.describe);
		//第一天的textView
		RelativeLayout tomorrow01 = (RelativeLayout) this.findViewById(R.id.tomorrow01);
		TextView weather_item_mouth = (TextView) tomorrow01.findViewById(R.id.weather_item_mouth);
		TextView weather_item_day = (TextView) tomorrow01.findViewById(R.id.weather_item_day);
		TextView weather_item_temperature = (TextView) tomorrow01.findViewById(R.id.weather_item_temperature);
		TextView weather_item_daytime = (TextView) tomorrow01.findViewById(R.id.weather_item_daytime);
		TextView weather_item_describe = (TextView) tomorrow01.findViewById(R.id.weather_item_describe);
		//第二天的textView
		RelativeLayout tomorrow02 = (RelativeLayout) this.findViewById(R.id.tomorrow02);
		TextView weather_item_mouth02 = (TextView) tomorrow02.findViewById(R.id.weather_item_mouth);
		TextView weather_item_day02 = (TextView) tomorrow02.findViewById(R.id.weather_item_day);
		TextView weather_item_temperature02 = (TextView) tomorrow02.findViewById(R.id.weather_item_temperature);
		TextView weather_item_daytime02 = (TextView) tomorrow02.findViewById(R.id.weather_item_daytime);
		TextView weather_item_describe02 = (TextView) tomorrow02.findViewById(R.id.weather_item_describe);
		//第三天的textView
		RelativeLayout tomorrow03 = (RelativeLayout) this.findViewById(R.id.tomorrow03);
		TextView weather_item_mouth03 = (TextView) tomorrow03.findViewById(R.id.weather_item_mouth);
		TextView weather_item_day03 = (TextView) tomorrow03.findViewById(R.id.weather_item_day);
		TextView weather_item_temperature03 = (TextView) tomorrow03.findViewById(R.id.weather_item_temperature);
		TextView weather_item_daytime03 = (TextView) tomorrow03.findViewById(R.id.weather_item_daytime);
		TextView weather_item_describe03 = (TextView) tomorrow03.findViewById(R.id.weather_item_describe);
		
		
		area.setTypeface(standardfFontFace);
		mouth.setTypeface(standardfFontFace);
		day.setTypeface(standardfFontFace);
		temperature.setTypeface(ce35FontFace);
		daytime.setTypeface(standardfFontFace);
		describe.setTypeface(standardfFontFace);
		
		weather_item_mouth.setTypeface(standardfFontFace);
		weather_item_day.setTypeface(standardfFontFace);
		weather_item_temperature.setTypeface(ce35FontFace);
		weather_item_daytime.setTypeface(standardfFontFace);
		weather_item_describe.setTypeface(standardfFontFace);
		
		weather_item_mouth02.setTypeface(standardfFontFace);
		weather_item_day02.setTypeface(standardfFontFace);
		weather_item_temperature02.setTypeface(ce35FontFace);
		weather_item_daytime02.setTypeface(standardfFontFace);
		weather_item_describe02.setTypeface(standardfFontFace);
		
		weather_item_mouth03.setTypeface(standardfFontFace);
		weather_item_day03.setTypeface(standardfFontFace);
		weather_item_temperature03.setTypeface(ce35FontFace);
		weather_item_daytime03.setTypeface(standardfFontFace);
		weather_item_describe03.setTypeface(standardfFontFace);
		
	}
	
	/**
	 * 填充UI数据
	 */
	public void updateUI()
	{
		//今天
		WeatherInfo todayDay = weatherList.get(0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("place", todayDay.getHsienName());
		editor.putString("temperature", todayDay.getTemperature());
		editor.putString("day", todayDay.getWeather());
		editor.commit();
		//明天
		WeatherInfo tomorrowDay = weatherList.get(1);
		//后天
		WeatherInfo afterTomorrowDay = weatherList.get(2);
		//大后天
		WeatherInfo afterTomorrowDay1 = weatherList.get(3);
		//今天的天气
		RelativeLayout today_weather = (RelativeLayout) this.findViewById(R.id.today_weather);
		TextView area = (TextView) today_weather.findViewById(R.id.area);
		TextView mouth = (TextView) today_weather.findViewById(R.id.mouth);
		TextView day = (TextView) today_weather.findViewById(R.id.day);
		TextView temperature = (TextView) today_weather.findViewById(R.id.temperature);
		TextView daytime = (TextView) today_weather.findViewById(R.id.daytime);
		TextView describe = (TextView) today_weather.findViewById(R.id.describe);
		//设置今天的天气
		area.setText(todayDay.getHsienName());
		mouth.setText(todayDay.getDate());
		day.setText(todayDay.getWeekDay());
		daytime.setText( todayDay.getWeather());
		temperature.setText(todayDay.getTemperature());
		describe.setText( todayDay.getWind() + " " + todayDay.getWindDir());
		
		//第一天的textView
		RelativeLayout tomorrow01 = (RelativeLayout) this.findViewById(R.id.tomorrow01);
		TextView weather_item_mouth = (TextView) tomorrow01.findViewById(R.id.weather_item_mouth);
		TextView weather_item_day = (TextView) tomorrow01.findViewById(R.id.weather_item_day);
		TextView weather_item_temperature = (TextView) tomorrow01.findViewById(R.id.weather_item_temperature);
		TextView weather_item_daytime = (TextView) tomorrow01.findViewById(R.id.weather_item_daytime);
		TextView weather_item_describe = (TextView) tomorrow01.findViewById(R.id.weather_item_describe);
		ImageLoadView weather_item_logo1 = (ImageLoadView) tomorrow01.findViewById(R.id.weather_logo);
		//设置第一天的天气
		weather_item_mouth.setText(tomorrowDay.getDate());
		weather_item_day.setText(tomorrowDay.getWeekDay());
		weather_item_daytime.setText( tomorrowDay.getWeather());
		weather_item_temperature.setText(tomorrowDay.getTemperature());
		weather_item_logo1.setImageUrl(tomorrowDay.getDayIcon(), mHandler);
		//-----------------------
		weather_item_describe.setText( tomorrowDay.getWind() + " " + tomorrowDay.getWindDir());
		//第二天的textView
		RelativeLayout tomorrow02 = (RelativeLayout) this.findViewById(R.id.tomorrow02);
		TextView weather_item_mouth02 = (TextView) tomorrow02.findViewById(R.id.weather_item_mouth);
		TextView weather_item_day02 = (TextView) tomorrow02.findViewById(R.id.weather_item_day);
		TextView weather_item_temperature02 = (TextView) tomorrow02.findViewById(R.id.weather_item_temperature);
		TextView weather_item_daytime02 = (TextView) tomorrow02.findViewById(R.id.weather_item_daytime);
		TextView weather_item_describe02 = (TextView) tomorrow02.findViewById(R.id.weather_item_describe);
		ImageLoadView weather_item_logo2 = (ImageLoadView) tomorrow02.findViewById(R.id.weather_logo);
		//设置第二天的天气
		weather_item_mouth02.setText(afterTomorrowDay.getDate());
		weather_item_day02.setText(afterTomorrowDay.getWeekDay());
		weather_item_daytime02.setText( afterTomorrowDay.getWeather());
		weather_item_temperature02.setText(afterTomorrowDay.getTemperature());
		weather_item_logo2.setImageUrl(afterTomorrowDay.getDayIcon(), mHandler);
		//-----------------------
		weather_item_describe02.setText(  afterTomorrowDay.getWind() + " " + afterTomorrowDay.getWindDir());
		//第三天的textView
		RelativeLayout tomorrow03 = (RelativeLayout) this.findViewById(R.id.tomorrow03);
		TextView weather_item_mouth03 = (TextView) tomorrow03.findViewById(R.id.weather_item_mouth);
		TextView weather_item_day03 = (TextView) tomorrow03.findViewById(R.id.weather_item_day);
		TextView weather_item_temperature03 = (TextView) tomorrow03.findViewById(R.id.weather_item_temperature);
		TextView weather_item_daytime03 = (TextView) tomorrow03.findViewById(R.id.weather_item_daytime);
		TextView weather_item_describe03 = (TextView) tomorrow03.findViewById(R.id.weather_item_describe);
		ImageLoadView weather_item_logo3 = (ImageLoadView) tomorrow03.findViewById(R.id.weather_logo);
		
		//设置第三天的天气
		weather_item_mouth03.setText(afterTomorrowDay1.getDate());
		weather_item_day03.setText(afterTomorrowDay1.getWeekDay());
		weather_item_daytime03.setText(afterTomorrowDay1.getWeather());
		weather_item_temperature03.setText(afterTomorrowDay1.getTemperature());
		weather_item_logo3.setImageUrl(afterTomorrowDay1.getDayIcon(), mHandler);
		//-----------------------
		weather_item_describe03.setText(afterTomorrowDay1.getWind() + " " + afterTomorrowDay1.getWindDir());
	}
	/**
	 * 温度比对
	 * @param t1 温度1
	 * @param t2 温度2
	 * @return
	 */
	public String getTemperature(int t1,int t2)
	{
		if(t1 > t2)
			return t2 + "-" + t1 + "℃";
		else{
			return t1 + "-" + t2 + "℃";
		}
	}

}
