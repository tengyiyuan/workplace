package com.toplion.cplusschool.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ab.image.AbImageLoader;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2016/6/29.
 */
public class BannerViewPager extends ViewPager {
    private Context mContext;
    private List<String> mAdsId;   // 图片本地资源ID组
    private List<String> mUrllist = new ArrayList<String>();  // 图片网络路径数组
    List<ImageView> listImgs;  // ImageView组
    private Timer mTimer;
    private int curIndex = 0;
    private LinearLayout mOvalLayout;
    private int scrollTime = 3 * 1000; //滚动时间间隔

    private MyOnItemClickListener onItemClickListener;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        listImgs = new ArrayList<ImageView>(); // 图片组
        int len = mUrllist.size() != 0 ? mUrllist.size() : mAdsId.size();
        for (int i = 0; i < len; i++) {
            ImageView imageview = new ImageView(mContext); // 实例化ImageView的对象
            AbImageLoader.getInstance(mContext).display(imageview,mUrllist.get(i), BaseApplication.ScreenWidth,(int)(BaseApplication.ScreenWidth*0.66));
            final int imagePosition = i;
            imageview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null)
                        onItemClickListener.onItemClick(imagePosition);
                }
            });
            listImgs.add(imageview);
        }
    }

    /** 初始化圆点 */
    private void initOvalLayout() {
        if (mOvalLayout != null) {
            mOvalLayout.removeAllViews();
            for (int i = 0; i < listImgs.size(); i++) {
                View v = new View(mContext); // 员点
                int radio = (int) (BaseApplication.ScreenWidth * 0.022);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(radio,
                        radio);
                lp.leftMargin = 15;
                v.setLayoutParams(lp);
                v.setBackgroundResource(R.drawable.dot_normal);
                mOvalLayout.addView(v);
            }
            // 选中第一个
            mOvalLayout.getChildAt(0).setBackgroundResource(R.drawable.dot_focused);
        }
    }

    public void start(Context context, List<String> list, List<String> adsId, LinearLayout ovalLayout){
        stopTimer();
        this.mContext = context;
        this.mUrllist = list;
        this.mAdsId = adsId;
        this.mOvalLayout = ovalLayout;
        this.curIndex = 0;

        init();
        initOvalLayout();
        ImagePaperAdapter adapter = new ImagePaperAdapter(listImgs);
        this.setAdapter(adapter);
        this.setCurrentItem(0);
        this.setOnPageChangeListener(new MyPageChangeListener(this));
        startTimer();
    }

    /** 设置项目点击事件监听器 */
    public void setMyOnItemClickListener(MyOnItemClickListener listener) {
        onItemClickListener = listener;
    }

    // 项目点击事件监听器接口
    public interface MyOnItemClickListener {
        void onItemClick(int curIndex);
    }

    class ImagePaperAdapter extends PagerAdapter {

        private List<ImageView> list;
        public ImagePaperAdapter(List<ImageView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = list.get(position) ;
            ViewParent vp =  view.getParent();
            if(vp != null){
                ViewGroup parent = (ViewGroup)vp;
                parent.removeView(view);
            }
            ((ViewPager)container).addView(list.get(position));
            return list.get(position);
        }

    }

    private class MyPageChangeListener implements OnPageChangeListener{

        boolean isAutoPlay = false;
        private BannerViewPager bannerViewPager;

        public MyPageChangeListener(BannerViewPager bannerViewPager){
            this.bannerViewPager = bannerViewPager;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    stopTimer();
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    if (bannerViewPager.getCurrentItem() == bannerViewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        bannerViewPager.setCurrentItem(0);
                    }
                    else if (bannerViewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        bannerViewPager.setCurrentItem(bannerViewPager.getAdapter().getCount() - 1);
                    }
                    startTimer();
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub
            //这里面动态改变小圆点的被背景，来实现效果
            curIndex = pos;
            for(int i=0;i < listImgs.size();i++){
                mOvalLayout.getChildAt(i).setBackgroundResource(R.drawable.dot_normal); // 圆点取消
                if(i == curIndex)
                    mOvalLayout.getChildAt(curIndex).setBackgroundResource(R.drawable.dot_focused);// 圆点选中
            }
        }

    }

    /** 处理定时滚动任务 */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            curIndex = (curIndex + 1)%listImgs.size();
            setCurrentItem(curIndex);
        }
    };

    /** 停止自动滚动任务 */
    public void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /** 开始自动滚动任务 图片大于1张才滚动 */
    public void startTimer() {
        if (mTimer == null && listImgs.size() > 1) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                public void run() {
                    handler.sendMessage(handler.obtainMessage(1));
                }
            }, scrollTime, scrollTime);
        }
    }

}
