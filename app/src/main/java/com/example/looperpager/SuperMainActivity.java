package com.example.looperpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.example.looperpager.views.PagerItem;
import com.example.looperpager.views.SobLooperPager;

import java.util.ArrayList;
import java.util.List;

public class SuperMainActivity extends AppCompatActivity {

    private SobLooperPager mLooperPager;
    private List<PagerItem> mData = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_main);
        initData();//数据先有
        initView();
        initEvent();
    }

    private void initEvent() {
        if (mLooperPager != null) {
            mLooperPager.setOnItemClickListener(new SobLooperPager.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Toast.makeText(SuperMainActivity.this,"You Clicked ->"+position +"个 item",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initData() {
        mData.add(new PagerItem("第一个图片",R.mipmap.pic1));
        mData.add(new PagerItem("第2个图片",R.mipmap.pic2));
        mData.add(new PagerItem("第三个图片",R.mipmap.pic3));
        mData.add(new PagerItem("第4个图片",R.mipmap.pic4));
    }

    private SobLooperPager.InnerAdapter mInnerAdapter = new SobLooperPager.InnerAdapter() {

        @Override
        protected int getDataSize() {
            return mData.size();
        }

        @Override
        public View getSubView(ViewGroup container, int position) {
            ImageView iv = new ImageView(container.getContext());
            iv.setImageResource(mData.get(position).getPicResId());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            return iv;
        }

    };
    private void initView() {
        mLooperPager = this.findViewById(R.id.sob_looper_pager);
        mLooperPager.setData(mInnerAdapter, new SobLooperPager.BindExListener() {
            @Override
            public String getTitle(int position) {
                return mData.get(position).getTitle();
            }
        });
    }
}
