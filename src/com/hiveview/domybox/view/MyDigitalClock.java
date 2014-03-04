package com.hiveview.domybox.view;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.DigitalClock;

import com.hiveview.domybox.utils.TypefaceUtils;
/**
 * 自定义格式的DigitalClock
 * @author jz
 *
 */
@SuppressLint("NewApi")
public class MyDigitalClock extends android.widget.DigitalClock {


	private Context context;
   

	Calendar mCalendar;
    private final static String m12 = "hh:mm";
    private final static String m24 = "kk:mm";
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    String mFormat;
    private String hourIn24 = "AM";

    public String getHourIn24() {
		return hourIn24;
	}
	public void setHourIn24(String hourIn24) {
		this.hourIn24 = hourIn24;
	}
	public MyDigitalClock(Context context) {
        super(context);
        initClock(context);
        this.context = context;
    }
    public MyDigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		initClock(context);
		this.context = context;
	}


    private void initClock(Context context) {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }
    /* @Override
    public void setTextSize(float size) {
    	super.setTextSize(57);
    }*/

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
                public void run() {
                    if (mTickerStopped) return;
                    mCalendar.setTimeInMillis(System.currentTimeMillis());
                    setText(DateFormat.format(mFormat, mCalendar));
                    setHourIn24(DateFormat.format("AA", mCalendar).toString());
                    invalidate();
                    long now = SystemClock.uptimeMillis();
                    long next = now + (1000 - now % 1000);
                    mHandler.postAtTime(mTicker, next);
                }
            };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }
    public void setContext(Context context)
    { 
    	this.context = context;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    public void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }
    
    public void setFormat(String format) {
    	mFormat = format;
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(DigitalClock.class.getName());
    }

	@Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(DigitalClock.class.getName());
    }
	
	@Override
	public void setTypeface(Typeface tf) {
		
		super.setTypeface(TypefaceUtils.getCE35FontFace());
	}
}
