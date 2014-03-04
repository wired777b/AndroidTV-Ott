package com.hiveview.domybox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.TypefaceUtils;

public class SettingsItemView extends FrameLayout{
	private Context mContext;
	private View view;
	public TextView tv_item_name;
//	public ImageView iv_item_icon;
	public RelativeLayout rl_item_bg;

	public SettingsItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		init();
	}

	public SettingsItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
	}

	public SettingsItemView(Context context) {
		super(context);
		mContext=context;
		init();
	}

	private void init() {
		setLayout();
		initView();
		setcharacters();
	}

	private void initView() {
		tv_item_name = 	(TextView)view.findViewById(R.id.tv_item_name);
//		iv_item_icon = 	(ImageView)view.findViewById(R.id.iv_item_icon);
		rl_item_bg 	 = 	(RelativeLayout)view.findViewById(R.id.rl_item_bg);
		
	}

	protected void setLayout() {
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (View) mLayoutInflater.inflate(R.layout.view_layout_item_setting, null);
		addView(view);
	}
	
	private void setcharacters() {
		tv_item_name.setTypeface(TypefaceUtils.getStandardfFontFace());
	}
	
}
