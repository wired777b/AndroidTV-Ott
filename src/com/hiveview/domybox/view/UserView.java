package com.hiveview.domybox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.TypefaceUtils;

public class UserView extends RelativeLayout {
	private Context context;
	private TextView about_account_1;

	public UserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public UserView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() 
	{
		View view = View.inflate(context, R.layout.user_relativelayout, null);
		about_account_1 = (TextView) view.findViewById(R.id.about_account_1);
		about_account_1.setTypeface(TypefaceUtils.getStandardfFontFace());
		addView(view);
	}
	
	

}
