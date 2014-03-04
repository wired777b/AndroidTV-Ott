package com.hiveview.domybox.view;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.TypefaceUtils;

public class WeatherView extends FrameLayout{

	private Context mContext;
	private MyTextView district;
	private TextView wind;
	private TextView temperature;
	private TextView day;
	

	public WeatherView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	public WeatherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
	}

	public WeatherView(Context context) {
		super(context);
		mContext=context;
		init();
	}

	private void init() {
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (View) mLayoutInflater.inflate(R.layout.view_layout_weather,null);
		addView(view);
		//listener = new myKeyDownListener();
		//focusListener = new myFocusChangeListener();
		wind	 	=	(TextView)view.findViewById(R.id.tv_weather_wind);
		day 		=	(TextView)view.findViewById(R.id.tv_weather_day);
		district 	=	(MyTextView)view.findViewById(R.id.tv_weather_district);
		temperature =	(TextView)view.findViewById(R.id.tv_weather_temperature);
		//设置字体
		setcharacters();
	}
	
	public void setFocused(boolean start)
	{
		district.setStart(start);
	}



	private void setcharacters() {
		Typeface standardfFontFace = TypefaceUtils.getStandardfFontFace();
		Typeface ce35FontFace = TypefaceUtils.getCE35FontFace();

		district.setTypeface(standardfFontFace);
		wind.setTypeface(standardfFontFace);
		day.setTypeface(standardfFontFace);
		temperature.setTypeface(ce35FontFace);
	}

	/**
	 * 焦点改变的监听
	 * @author xlk
	 *
	 */
		
	/*	private void getWeatherFromIntent(final String cityName)
		{
			lv_zone.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) {
					HttpTaskManager.getInstance().submit(new Runnable() 
					{
						@Override
						public void run() 
						{
							HiveViewService service = new HiveViewService();
							String cityId = GetCitiesId.getInstance().getCitiesId(cityName);
							ArrayList<WeatherInfo> videoRelatedList = service.getVideoRelatedList(cityId);
							today01 = videoRelatedList.get(0);
							today02 = videoRelatedList.get(1);
							tomorrow01 = videoRelatedList.get(2);
							tomorrow02 = videoRelatedList.get(3);
							afterTomorrow01 = videoRelatedList.get(4);
							afterTomorrow02 = videoRelatedList.get(5);
						}
					});
				}
			});
		}
		*/
	}
	



