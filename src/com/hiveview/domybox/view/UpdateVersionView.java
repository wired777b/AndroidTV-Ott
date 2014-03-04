package com.hiveview.domybox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.domybox.R;
import com.hiveview.domybox.utils.TypefaceUtils;

public class UpdateVersionView extends RelativeLayout {
	private Context context;
	private TextView version_update_title;
	public UpdateVersionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public UpdateVersionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public UpdateVersionView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		View view = View.inflate(context, R.layout.update_relativelayout, null);
		version_update_title = (TextView) view.findViewById(R.id.version_update_title_1);
		version_update_title.setTypeface(TypefaceUtils.getStandardfFontFace());
		addView(view);
	}

}
