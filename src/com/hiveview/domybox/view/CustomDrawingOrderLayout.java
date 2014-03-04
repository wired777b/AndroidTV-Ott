package com.hiveview.domybox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 
 * Custom RelativeLayout, change the drawing order childrens.
 * 
 * @author mxw
 *
 */
public class CustomDrawingOrderLayout extends RelativeLayout{
	
	public static final String TAG = "CustomDrawingOrderLayout";

	public CustomDrawingOrderLayout(Context context) {
		super(context);
		init();
	}

	public CustomDrawingOrderLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomDrawingOrderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		super.setChildrenDrawingOrderEnabled(true);
	}
	
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		
		View focusView = this.getFocusedChild();
		
		int focusPosition = -1;
		
		for (int index = 0; index < childCount; ++index) {
			View view = this.getChildAt(index);
			if (view == focusView) {
				focusPosition = index;
				break;
			}
		}
		
		if (focusPosition >= 0) {
			if (i < focusPosition) {
				return i;
			} else if (i > focusPosition)  {
				return i - 1;
			} else {
				return childCount - 1;
			}
		} else {
			return i;
		}
		
	}

}
