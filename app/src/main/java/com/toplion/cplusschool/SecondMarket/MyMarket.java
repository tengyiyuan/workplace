package com.toplion.cplusschool.SecondMarket;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.MyAnimations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toplion on 2016/10/10.
 */
public class MyMarket extends BaseActivity {
    private ImageView back;
    private TextView newtitle;
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
    View view;
    private int style = 1;
    private MyFrament frament;
    private MyTwoFrament myfragment;
    public static String flag = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytabwidget);
        flag = "false";
        init();
    }

    @Override
    protected void init() {
        super.init();
        MyAnimations.initOffset(this);
        style = getIntent().getIntExtra("style", 1);
        right = (LinearLayout) findViewById(R.id.right);
        left = (LinearLayout) findViewById(R.id.left);
        newtitle = (TextView) findViewById(R.id.newtitle);
        back = (ImageView) findViewById(R.id.back);
        composer_buttons_show_hide_button = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
        composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
        composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
        composer_buttons_show_hide_button.setVisibility(View.GONE);
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
        frament = new MyFrament(style, 1);
        myfragment = new MyTwoFrament(style, 2);
        fragments.add(frament);
        fragments.add(myfragment);
        viewPager.setAdapter(new myPagerAdapter(this
                .getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        voiceAnswer = (TextView) findViewById(R.id.tab_1);
        healthPedia = (TextView) findViewById(R.id.tab_2);
        if (style == 1) {
            voiceAnswer.setText("我的发布");
            healthPedia.setText("我的招领");
        } else if (style == 2) {
            voiceAnswer.setText("我的发布");
            healthPedia.setText("我的需求");
        } else if (style == 3) {
            voiceAnswer.setText("我的发布");
            healthPedia.setText("我的兼职");
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("flag", flag);
                //设置返回数据
                setResult(RESULT_OK, intent);
                finish();
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
                intent.setClass(MyMarket.this, ReleaseActivity.class);
                MyMarket.this.startActivity(intent);
                composerButtonsWrapper.setVisibility(View.GONE);
                areButtonsShowing = false;

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyMarket.this, ReleaseActivity.class);
                MyMarket.this.startActivity(intent);
                composerButtonsWrapper.setVisibility(View.GONE);
                areButtonsShowing = false;
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
                    newtitle.setText("我的发布");
                    break;
                case 1:
                    healthPedia.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    if (style == 1) {
                        newtitle.setText("我的招领");
                    } else if (style == 2) {
                        newtitle.setText("我的需求");
                    } else if (style == 3) {
                        newtitle.setText("我的兼职");
                    }
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
                    newtitle.setText("我的发布");
                    break;
                case 1:
                    healthPedia.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    if (style == 1) {
                        newtitle.setText("我的招领");
                    } else if (style == 2) {
                        newtitle.setText("我的需求");
                    } else if (style == 3) {
                        newtitle.setText("我的兼职");
                    }
                    break;
                case 2:

                    voiceAnswer.setTextColor(unSelectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case 1:
                    frament.getData();
                    flag = "true";
                    break;
                case 2:
                    myfragment.getData();
                    flag = "true";
                    break;
                default:
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("flag", flag);
            //设置返回数据
            setResult(RESULT_OK, intent);
            finish();
        }
        return false;
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
