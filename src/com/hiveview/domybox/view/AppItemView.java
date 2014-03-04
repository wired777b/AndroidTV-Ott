package com.hiveview.domybox.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hiveview.domybox.R;
import com.hiveview.domybox.common.image.ImageLoadView;
import com.hiveview.domybox.utils.TypefaceUtils;

public class AppItemView extends RelativeLayout {

	private Context mContext;
	private RelativeLayout 	rl_app_item_bg;
	private ImageLoadView 	iv_app_item_image;
	private ImageView 		iv_app_installed;
	private ImageView 		iv_app_deleted;
	private MarqueeText 	tv_app_item_name;
	private LinearLayout 	ll_app_delected;

	public AppItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		init();
	}

	public AppItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
	}

	public AppItemView(Context context) {
		super(context);
		mContext=context;
		init();
	}

	private void init() {
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (View) mLayoutInflater.inflate(R.layout.view_layout_app_item, null);

		addView(view);
		

		rl_app_item_bg = (RelativeLayout) view.findViewById(R.id.rl_app_item_bg);
		
		iv_app_item_image = (ImageLoadView) view.findViewById(R.id.iv_app_item_image);
		tv_app_item_name  = (MarqueeText) view.findViewById(R.id.tv_app_item_name);
		
		
		iv_app_installed  = (ImageView) view.findViewById(R.id.iv_app_installed);
		
		iv_app_deleted 	  = (ImageView) view.findViewById(R.id.iv_app_deleted);
		ll_app_delected   = (LinearLayout) view.findViewById(R.id.ll_app_delected);
		
		
		setcharacters();
	}
	
	private void setcharacters() {

		tv_app_item_name.setTypeface(TypefaceUtils.getStandardfFontFace());
		
	}
	
	/**设置获得焦点*/
	public void  setHasFocus(Boolean hasFocus){
		if(hasFocus){
			rl_app_item_bg.setBackgroundResource(R.drawable.bg_b);
			tv_app_item_name.setStart(true);
			tv_app_item_name.setText(tv_app_item_name.getText().toString());
		}else {
			tv_app_item_name.setStart(false);
			tv_app_item_name.setText(tv_app_item_name.getText().toString());
			rl_app_item_bg.setBackgroundResource(R.drawable.bg_a);
		}
	}
	
	
	/**设置DelecteModel*/
	public void  setDeletedModel(Boolean isDelecteModel){
		if(isDelecteModel){
			ll_app_delected.setVisibility(View.VISIBLE);
		}else {
			ll_app_delected.setVisibility(View.GONE);
		}
	}
	
	/**设置	新版本模式*/
	public void  setInstallModel(Boolean isInstalled){
		if(isInstalled){
			iv_app_installed.setVisibility(View.VISIBLE);
		}else {
			iv_app_installed.setVisibility(View.GONE);
		}
	}
	
	
	/**设置	资源
	 * @param mHandler */
	public void  setImageUrl(String url, Handler mHandler){
		iv_app_item_image.setImageUrl(url, mHandler);
	}
	
	/**设置	资源*/
	public void  setBackgroundResource(int idSrc){
		iv_app_item_image.setBackgroundResource(idSrc);
	}
	
	/**设置	资源*/
	public void  setBackgroundResource(Drawable drawable){
		iv_app_item_image.setImageDrawable(drawable);
	}

	public void setAppName(String appName) {
		tv_app_item_name.setText(appName);
	}
	
	public void setInstalled(Boolean isInstalled){
		if(isInstalled){
			iv_app_installed.setVisibility(View.VISIBLE);
		}else {
			iv_app_installed.setVisibility(View.GONE);
		}
	}
	
	/**设置DelecteModel 焦点*/
	@SuppressLint("NewApi")
	public void  setDeletedModelFocused(Boolean hasFocus){
		if(hasFocus){
			ll_app_delected.setAlpha(0.8f);
			iv_app_deleted.setVisibility(View.VISIBLE);
		}else {
			ll_app_delected.setAlpha(0.4f);
			iv_app_deleted.setVisibility(View.GONE);
		}
	}
	
}
