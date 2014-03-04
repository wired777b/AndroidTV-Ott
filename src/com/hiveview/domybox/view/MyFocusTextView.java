package com.hiveview.domybox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class MyFocusTextView extends TextView {
	private boolean isStart = false;

	public MyFocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyFocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyFocusTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return isStart;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

}
