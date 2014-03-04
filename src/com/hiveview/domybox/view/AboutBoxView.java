package com.hiveview.domybox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.TypefaceUtils;

public class AboutBoxView extends RelativeLayout {
	private Context context;
	private TextView about_box_title_1;
	public AboutBoxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public AboutBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public AboutBoxView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() 
	{
		View view = View.inflate(context, R.layout.aboutbox_relativelayout, null);
		about_box_title_1 = (TextView) view.findViewById(R.id.about_box_title_1);
		about_box_title_1.setTypeface(TypefaceUtils.getStandardfFontFace());
		addView(view);
	}
}
