package com.pigbear.hi_andgmusic.ui.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 杨阳洋 on 2017/9/28.
 * usg:
 */

public class CustomerViewPager extends ViewPager {
    public CustomerViewPager(Context context) {
        super(context);
    }

    public CustomerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    PointF mPointF = new PointF();

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取按下的坐标
                mPointF.x = ev.getX();
                mPointF.y = ev.getY();
                if (this.getChildCount() > 1) {
                    //通知父控件不拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.getChildCount() > 1) {
                    //通知父控件不拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (PointF.length(ev.getX() - mPointF.x, ev.getY() - mPointF.y) < (float) 5.0F) {
                    //单纯的点击
                    onSingleTouch(this);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void onSingleTouch(View view) {
        if (mOnSingleTouchListener != null) {
            mOnSingleTouchListener.onSingleTouch();
        }
    }

    protected OnSingleTouchListener mOnSingleTouchListener;

    public interface OnSingleTouchListener {
        void onSingleTouch();
    }

    public void setmOnSingleTouchListener(OnSingleTouchListener mOnSingleTouchListener) {
        this.mOnSingleTouchListener = mOnSingleTouchListener;
    }
}
