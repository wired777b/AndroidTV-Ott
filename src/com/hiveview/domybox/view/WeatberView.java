package com.hiveview.domybox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.TypefaceUtils;

public class WeatberView extends RelativeLayout {
	private Context context;
	private TextView tv_weather_title;
	public WeatberView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public WeatberView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public WeatberView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	private void init() {
		View view = View.inflate(context, R.layout.weather_set_relativelayout, null);
		tv_weather_title = (TextView) view.findViewById(R.id.tv_weather_title);
		tv_weather_title.setTypeface(TypefaceUtils.getStandardfFontFace());
		addView(view);
	}
	

}
