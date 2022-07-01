package com.example.looperpager.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.looperpager.R;


public class SobLooperPager extends LinearLayout {

    private static final String TAG = "SobLooperPager";
    private SobViewPagaer mViewPager;
    private LinearLayout mPointContainer;
    private TextView mTitle;
    private BindExListener mTitleSetListener = null;
    private InnerAdapter mInnerAdapter = null;
    private OnItemClickListener mOnItemClickListener = null;


    public SobLooperPager(Context context) {
        this(context,null);
    }

    public SobLooperPager(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SobLooperPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.looper_pager,this,true);
        //等价于一下俩行代码
//        View item = LayoutInflater.from(context).inflate(R.layout.looper_pager, this, false);
//        addView(item);
        init();
    }

    private void init() {
        initView();
        //设置滑动监听
        initEvent();
    }

    private void initEvent() {
        mViewPager.setPagerItemClickListener(new SobViewPagaer.OnPagerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mOnItemClickListener!=null&&mInnerAdapter!=null){
                    int realPositon = position%(mInnerAdapter.getDataSize());
                    mOnItemClickListener.onItemClick(realPositon);
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //切换的一个回调方法
            }

            @Override
            public void onPageSelected(int position) {
                if (mInnerAdapter!=null){

                //切换停下来的回调
                //停下来以后，设置标题
                int realPosition = position%mInnerAdapter.getDataSize();
            if (mTitleSetListener !=null){
                mTitle.setText(mTitleSetListener.getTitle(realPosition));
            }
            //切换指示器焦点
                updateIndicator();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //切换状态的改变

            }
        });
    }



    public interface BindExListener {
        String getTitle(int position);
    }

    public void setData(InnerAdapter innerAdapter, BindExListener listener){
        this.mTitleSetListener = listener;
        mViewPager.setAdapter(innerAdapter);
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2+1);
        this.mInnerAdapter= innerAdapter;
        if (listener != null) {
            mTitle.setText(listener.getTitle(mViewPager.getCurrentItem()%mInnerAdapter.getDataSize()));
        }
        //可以拿到数据的个数 根据数据的个数动态的创建圆点，indicator
        updateIndicator();
    }
    public abstract static class InnerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final int realPosition = position%getDataSize();
            View itemVeiw = getSubView(container,realPosition);
//            itemVeiw.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mItemClickListener != null) {
//                        mItemClickListener.onItemClick(realPosition);
//                    }
//                }
//            });
            container.addView(itemVeiw);
            return itemVeiw;
        }

        protected abstract int getDataSize();

        public abstract View getSubView(ViewGroup container, int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
     public interface OnItemClickListener{
        void onItemClick(int position);

     }
    private void updateIndicator() {
        if (mInnerAdapter != null||mTitleSetListener!=null) {
            int count = mInnerAdapter.getDataSize();
            Log.d(TAG,"indicator is "+ count);
            mPointContainer.removeAllViews();
            for (int i = 0; i < count; i++) {
                //先创建一个点
                View  point  = new View(getContext());
                Log.d(TAG,"debug ->mViewPager.getCurrentItem()->"+mViewPager.getCurrentItem());
                if (mViewPager.getCurrentItem()%mInnerAdapter.getDataSize() == i){
                    //圆形
                    point.setBackground(getResources().getDrawable(R.drawable.shape_circle_read));
//                    point.setBackgroundColor(Color.parseColor("#ff0000"));
                }else{
                    point.setBackground(getResources().getDrawable(R.drawable.shape_circle_white));
//                    point.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                //设置大小  或者说设置图像
                LinearLayout.LayoutParams layoutParams = new LayoutParams(SizeUtils.dip2px(getContext(),10),SizeUtils.dip2px(getContext(),10));
                layoutParams.setMargins(SizeUtils.dip2px(getContext(),5),0,SizeUtils.dip2px(getContext(),5),0);
                point.setLayoutParams(layoutParams);
                //添加到容器里面去
                mPointContainer.addView(point);
            }
        }
    }

    private void initView() {
        mViewPager = this.findViewById(R.id.looper_pager_vp);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(SizeUtils.dip2px(getContext(),10));
        mPointContainer = this.findViewById(R.id.looper_point_container_iv);
        mTitle = this.findViewById(R.id.looper_title_tv);
    }

}
