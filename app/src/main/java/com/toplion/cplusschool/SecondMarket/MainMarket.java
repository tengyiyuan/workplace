package com.toplion.cplusschool.SecondMarket;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.StandardInfo;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.MyAnimations;
import com.toplion.cplusschool.Utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toplion on 2016/10/10.
 */
public class MainMarket extends BaseActivity {
    private TextView lefttext, righttext;
    private ImageView myhead;
    private LinearLayout right, left;
    private RelativeLayout composer_buttons_show_hide_button;
    private boolean areButtonsShowing;
    private ImageView composerButtonsShowHideButtonIcon;
    private RelativeLayout composerButtonsWrapper;
    private ViewPager viewPager;
    private ImageView imageView;
    private TextView voiceAnswer, healthPedia;
    private List<Fragment> fragments;
    private int offset = 0;
    private int currIndex = 0;
    private int bmpW;
    private int selectedColor, unSelectedColor;
    private static final int pageSize = 2;
    private ImageView my_order_iv_return;
    View view;
    private EditText marketserch;
    private int type = 1;
    private int style = 1;
    private myPagerAdapter adapter;
    private TabOneFrament tabOneFragment;
    private TabTwoFrament tabTwoFrament;
    private ImageView composer_button_fabu;
    private ImageView composer_button_qiugou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabwidget);
        init();

    }

    @Override
    protected void init() {
        super.init();
        MyAnimations.initOffset(this);
        type = getIntent().getIntExtra("module", 1);
        composer_button_fabu=(ImageView)findViewById(R.id.composer_button_fabu);
        composer_button_qiugou=(ImageView)findViewById(R.id.composer_button_qiugou);
        lefttext = (TextView) findViewById(R.id.lefttext);
        righttext = (TextView) findViewById(R.id.righttext);
        my_order_iv_return = (ImageView) findViewById(R.id.my_order_iv_return);
        myhead = (ImageView) findViewById(R.id.myhead);
        right = (LinearLayout) findViewById(R.id.right);
        left = (LinearLayout) findViewById(R.id.left);
        marketserch = (EditText) findViewById(R.id.marketserch);
        composer_buttons_show_hide_button = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
        composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
        composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
        selectedColor = getResources().getColor(R.color.logo_color);
        unSelectedColor = getResources().getColor(R.color.yuanshicolor);
        imageView = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(),
                R.mipmap.tab_selected_bg).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);
        viewPager = (ViewPager) findViewById(R.id.vPager);
        fragments = new ArrayList<Fragment>();
        tabOneFragment = new TabOneFrament(type, 1);
        tabTwoFrament = new TabTwoFrament(type, 2);
        fragments.add(tabOneFragment);
        fragments.add(tabTwoFrament);
        adapter = new myPagerAdapter(this
                .getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        voiceAnswer = (TextView) findViewById(R.id.tab_1);
        healthPedia = (TextView) findViewById(R.id.tab_2);
        if (type == 1) {
            voiceAnswer.setText("失物发布");
            healthPedia.setText("招领发布");
            lefttext.setText("失物发布");
            righttext.setText("招领发布");
            composer_button_fabu.setImageResource(R.mipmap.lost);
            composer_button_qiugou.setImageResource(R.mipmap.found);
        } else if (type == 2) {
            voiceAnswer.setText("二手发布");
            healthPedia.setText("二手求购");
            lefttext.setText("商品发布");
            righttext.setText("求购发布");
            composer_button_fabu.setImageResource(R.mipmap.fabu);
            composer_button_qiugou.setImageResource(R.mipmap.qiugou);
        } else if (type == 3) {
            voiceAnswer.setText("招聘发布");
            healthPedia.setText("求职发布");
            lefttext.setText("招聘发布");
            righttext.setText("求职发布");
            composer_button_fabu.setImageResource(R.mipmap.zhaopin);
            composer_button_qiugou.setImageResource(R.mipmap.qiuzhi);
        }

        voiceAnswer.setOnClickListener(new MyOnClickListener(0));
        healthPedia.setOnClickListener(new MyOnClickListener(1));
        voiceAnswer.setTextColor(selectedColor);

        composer_buttons_show_hide_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!areButtonsShowing) {
                    composerButtonsWrapper.setVisibility(View.VISIBLE);
                    viewPager.setClickable(false);
                    MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
                    composerButtonsShowHideButtonIcon
                            .startAnimation(MyAnimations.getRotateAnimation(0,
                                    -270, 300));
                } else {
                    MyAnimations
                            .startAnimationsOut(composerButtonsWrapper, 300, composerButtonsWrapper);
                    composerButtonsShowHideButtonIcon
                            .startAnimation(MyAnimations.getRotateAnimation(
                                    -270, 0, 300));
                }
                areButtonsShowing = !areButtonsShowing;
            }
        });
        composerButtonsWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areButtonsShowing) {
                    MyAnimations
                            .startAnimationsOut(composerButtonsWrapper, 300, composerButtonsWrapper);
                    composerButtonsShowHideButtonIcon
                            .startAnimation(MyAnimations.getRotateAnimation(
                                    -270, 0, 300));
                    areButtonsShowing = !areButtonsShowing;
                }
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainMarket.this, ReleaseActivity.class);
                intent.putExtra("module", type);
                intent.putExtra("reltype", 1);
                MainMarket.this.startActivityForResult(intent, 1);
                composerButtonsWrapper.setVisibility(View.GONE);
                areButtonsShowing = false;

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainMarket.this, ReleaseActivity.class);
                intent.putExtra("module", type);
                intent.putExtra("reltype", 2);
                MainMarket.this.startActivityForResult(intent, 2);
                composerButtonsWrapper.setVisibility(View.GONE);
                areButtonsShowing = false;
            }
        });
        myhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("style", type);
                intent.setClass(MainMarket.this, MyMarket.class);
                MainMarket.this.startActivityForResult(intent, style);
            }
        });
        marketserch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMarket.this, MarketSearchActivity.class);
                intent.putExtra("module", type);
                intent.putExtra("reltype", style);
                startActivity(intent);
            }
        });
        my_order_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            switch (index) {
                case 0:
                    voiceAnswer.setTextColor(selectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    style = 1;
                    break;
                case 1:
                    healthPedia.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    style = 2;
                    break;
                case 2:

                    voiceAnswer.setTextColor(unSelectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    break;
            }
            viewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        int one = offset * 2 + bmpW;
        int two = one * 2;

        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {
            Animation animation = new TranslateAnimation(one * currIndex, one
                    * index, 0, 0);
            currIndex = index;
            animation.setFillAfter(true);
            animation.setDuration(100);
            imageView.startAnimation(animation);
            switch (index) {
                case 0:
                    voiceAnswer.setTextColor(selectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    style = 1;
                    break;
                case 1:
                    healthPedia.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    style = 2;
                    break;
                case 2:
                    voiceAnswer.setTextColor(unSelectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String flag = data.getExtras().getString("flag");//得到新Activity 关闭后返回的数据
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case 1:
                    if (flag.isEmpty()) {

                    } else if (flag.equals("true")) {
                        tabOneFragment.getData();
                    }
                    break;
                case 2:
                    if (flag.isEmpty()) {

                    } else if (flag.equals("true")) {
                        tabTwoFrament.getData();
                    }
                    break;
                default:
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 定义适配器
     */
    class myPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null
                    : fragmentList.get(arg0);
        }

        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }
}
