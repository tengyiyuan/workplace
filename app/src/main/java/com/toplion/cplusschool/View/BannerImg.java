package com.toplion.cplusschool.View;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ab.image.AbImageLoader;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wang on 2016-2-25.
 */
public class BannerImg extends FrameLayout {
    private final static boolean isAutoPlay = true;
    //图片下载器
    private AbImageLoader mAbImageLoader = null;
    private List<String> imageUris;
    private List<ImageView> imageViewsList;
    private List<ImageView> dotViewsList;
    private LinearLayout mLinearLayout;
    private ViewPager mViewPager;
    private int currentItem  = 0;
    private ScheduledExecutorService scheduledExecutorService;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            mViewPager.setCurrentItem(currentItem);
        }
    };
    public BannerImg(Context context) {
        this(context,null);
    }
    public BannerImg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public BannerImg(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initUI(context);
        if(isAutoPlay){
            startPlay();
        }

    }
    private void initUI(Context context){
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<ImageView>();
        imageUris=new ArrayList<String>();
        LayoutInflater.from(context).inflate(R.layout.bannerimg, this, true);
        mLinearLayout=(LinearLayout)findViewById(R.id.dotsImg);
        mViewPager = (ViewPager) findViewById(R.id.imagePager);
        mAbImageLoader = new AbImageLoader(context);
    }
    public void setImageUris(List<String> imageuris)
    {
        imageUris.clear();
        imageViewsList.clear();
        dotViewsList.clear();
        mLinearLayout.removeAllViews();
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) (BaseApplication.ScreenWidth * 0.66));//0.66
        mViewPager.setLayoutParams(rp);
        for(int i=0;i<imageuris.size();i++)
        {
            imageUris.add(imageuris.get(i));
        }
        for (int i = 0; i < imageUris.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);//铺满屏幕
            //ImageLoader.getInstance().displayImage(imageUris.get(i), imageView);
            mAbImageLoader.display(imageView,imageUris.get(i), BaseApplication.ScreenWidth,(int)(BaseApplication.ScreenWidth*0.66));
            imageViewsList.add(imageView);
            ImageView viewDot =  new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25,25);
            params.leftMargin = 4;
            params.rightMargin = 4;
            params.bottomMargin = 15;
            if(i == 0){
                viewDot.setBackgroundResource(R.drawable.dot_focused);
            }else{
                viewDot.setBackgroundResource(R.drawable.dot_normal);
            }
            dotViewsList.add(viewDot);
            mLinearLayout.addView(viewDot,params);
        }
        mViewPager.setFocusable(true);
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
    }


    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new BannerTask(), 1, 4, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unused")
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }
    /**
     * 设置选中的圆点的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<dotViewsList.size(); i++){
            if(i == selectItems){
                dotViewsList.get(i).setBackgroundResource(R.drawable.dot_focused);
            }else{
                dotViewsList.get(i).setBackgroundResource(R.drawable.dot_normal);
            }
        }
    }
    private class MyPagerAdapter  extends PagerAdapter {
        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager)container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            // TODO Auto-generated method stub
            ((ViewPager)container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub
        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }
    }


    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay = false;
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        mViewPager.setCurrentItem(0);
                    }
                    //如果滑到头就从尾开始
                    else if (mViewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        mViewPager.setCurrentItem(mViewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int pos) {
//        	currentItem = pos;
            setImageBackground(pos);
        }
    }



    private class BannerTask implements Runnable{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (mViewPager) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

}
