package com.hiveview.domybox.common.listenter;

public interface ITabPage{
	
	
	public static final int FOCUS_LEFT_OUT = 0;
	
	public static final int FOCUS_UP_OUT = 1;
	
	public static final int FOCUS_RIGHT_OUT = 2;
	
	public static final int FOCUS_DOWN_OUT = 3;
	

	public boolean requestDefaultFocusLeft();
	
	public boolean requestDefaultFocusRight();
	
	public boolean requestDefaultFocusBottom();
	
	public boolean requestDefaultFocusUp();
	
	public boolean canGoUp();
	
	public boolean canGoLeft();
	
	public boolean canGoDown();
	
	public boolean canGoRight();
	public void setDefaultFocuse();
	
	
	
	

}
