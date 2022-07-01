package com.example.looperpager.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class SobViewPagaer extends ViewPager {
    private OnPagerItemClickListener mItemClickListener = null;
    private static final String TAG = "SobViewPagaer";
    private Handler mHandler;
    private  long delayTime=1000;
    private long downTime;
    public void setDelayTime(long delayTime){
        this.delayTime = delayTime;
    }
    public SobViewPagaer(@NonNull Context context) {
        this(context,null);
    }
    private boolean isClick =false;
    private float downX;
    private float downY;

    public SobViewPagaer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //这行可以省去
        mHandler = new Handler(Looper.getMainLooper());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        downTime = System.currentTimeMillis();
                        stopLooper();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        float ds = Math.abs(motionEvent.getX() - downX);
                        float dy = Math.abs(motionEvent.getY() - downY);
                        long dTime = System.currentTimeMillis() -downTime;
                        isClick = dy<=5&&ds<=5&&dTime<=1000;
                        Log.d(TAG,"is Click->"+isClick);
                        if (isClick&&mItemClickListener!=null) {
                            mItemClickListener.onItemClick(getCurrentItem());
                        }
                        startLooper();
                        break;
                }
                return false;
            }
        });
    }
    public void setPagerItemClickListener(OnPagerItemClickListener itemClickListener)
    {
        this.mItemClickListener = itemClickListener;
    }

    public interface OnPagerItemClickListener{
        void onItemClick(int position);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG,"onAttachedToWindow");
        startLooper();
    }
    private void startLooper(){
        mHandler.removeCallbacks(mTask);
        mHandler.postDelayed(mTask,delayTime);
        //或者
//        postDelayed(mTask,1000);
    }
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            mHandler.postDelayed(this,delayTime);
            //或者
//            postDelayed(this,1000);
        }
    };
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG,"onDetachedFromWindow");
        stopLooper();
    }

    private void stopLooper() {
        mHandler.removeCallbacks(mTask);
        //或者
//        removeCallbacks(mTask);
    }
}
