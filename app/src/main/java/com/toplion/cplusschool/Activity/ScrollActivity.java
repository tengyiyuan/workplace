package com.toplion.cplusschool.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ab.util.AbSharedUtil;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航引导页面
 * 显示导航图片
 * 目前图片加载为本地图片
 *
 * @author liyb
 * @version 1.1.1
 */
@SuppressWarnings("ALL")
@SuppressLint("HandlerLeak")
public class ScrollActivity extends BaseActivity {
    private SharePreferenceUtils share;
    private AbSharedUtil abSharedUtil;
    private ViewPager mViewPaper;
    private List<ImageView> images;             // 图片集合
    private int currentItem;                    // 当前点的位置
    private int[] imageIds = new int[]{R.mipmap.app_lead, R.mipmap.app_lead2,
            R.mipmap.app_lead3, R.mipmap.app_lead4};             // 显示的导航引导图片的id
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_scroll);
        // 查看是否是首次登录
        share = new SharePreferenceUtils(this);
        abSharedUtil = new AbSharedUtil();
        // 初始化图片 , 显示导航引导
        initPhoto();
    }

    // 初始化导航引导
    private void initPhoto() {
        mViewPaper = (ViewPager) findViewById(R.id.vp);
        // 显示的图片添加进集合
        images = new ArrayList<ImageView>();
        for (int imageId : imageIds) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageId);
            images.add(imageView);
        }

        // 添加适配器
        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                currentItem = position;
                int totalSize = images.size() - 1;
                //当当前的位置等于总的数量时，添加点击事件
                if (currentItem == totalSize) {
                    ImageView vp = images.get(currentItem);
                    vp.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // 更改isFirst为false, 说明不是第一次登录
                            share.put("isFirst", false);
                            abSharedUtil.putBoolean(ScrollActivity.this, "isFirst", false);
                            //点击立即体验，跳转到登陆页面，销毁当前页面
                            Intent intent = new Intent(ScrollActivity.this, MainActivity.class);
//							Intent intent = new Intent(ScrollActivity.this, MainFrameActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    onStop();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 自定义Adapter
     *
     * @author liyb
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            int totalSize = images.size() - 1;
            //当当前的位置等于总的数量时，按钮生效
            if (currentItem == totalSize) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        share.put("isFirst", false);
                        abSharedUtil.putBoolean(ScrollActivity.this, "isFirst", false);
                        //点击立即体验，跳转到登陆页面
//						Intent intent = new Intent(ScrollActivity.this, MainActivity.class);
                        Intent intent = new Intent(ScrollActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            return images.get(position);
        }

    }

    /**
     * 利用线程池定时执行动画轮播
     */
    public void onStart() {
        super.onStart();
    }

    @SuppressWarnings("unused")
    private class ViewPageTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
            int totalSize = images.size() - 1;
            //当当前的位置等于总的数量时，
            if (currentItem == totalSize) {
                onStop();
            }
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        }
    };

    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
//      super.onBackPressed();
        finish();
    }
}
