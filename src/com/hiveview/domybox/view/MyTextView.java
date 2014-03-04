package com.hiveview.domybox.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class MyTextView extends TextView {
	private boolean isStart = false;


	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return isStart;
	}
	
	@Override
	public void setSingleLine(boolean singleLine) {
		// TODO Auto-generated method stub
		super.setSingleLine(true);
	}
	
	@Override
	public void setEllipsize(TruncateAt where) {
		// TODO Auto-generated method stub
		super.setEllipsize(where.MARQUEE);
	}
}
