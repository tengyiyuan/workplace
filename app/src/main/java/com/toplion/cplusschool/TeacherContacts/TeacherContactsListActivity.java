package com.toplion.cplusschool.TeacherContacts;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.PhotoWall.MyHistoryFragment;
import com.toplion.cplusschool.PhotoWall.MyReleaseFragment;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.MyAnimations;
import com.toplion.cplusschool.Utils.ToastManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang
 * on 2016/11/30.
 * 教工通讯录
 */
public class TeacherContactsListActivity extends BaseActivity {
    private ViewPager viewPager;
    private ImageView imageView;
    private ImageView iv_tea_contacts_search;
    private TextView voiceAnswer, healthPedia;
    private List<Fragment> fragments;
    private int offset = 0;
    private int currIndex = 0;
    private int bmpW;
    private int selectedColor, unSelectedColor;
    private static final int pageSize = 2;
    private ImageView iv_tea_contacts_return;
    private myPagerAdapter adapter;
    private DepartmentListFragment dListFragment;
    private ContactsByNameListFragment cListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_contacts_home);
        init();

    }

    @Override
    protected void init() {
        super.init();
        MyAnimations.initOffset(this);
        iv_tea_contacts_return = (ImageView) findViewById(R.id.iv_tea_contacts_return);
        iv_tea_contacts_search = (ImageView) findViewById(R.id.iv_tea_contacts_search);
        selectedColor = getResources().getColor(R.color.logo_color);
        unSelectedColor = getResources().getColor(R.color.yuanshicolor);
        imageView = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.tab_selected_bg).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);
        viewPager = (ViewPager) findViewById(R.id.vPager);
        fragments = new ArrayList<Fragment>();
        dListFragment = new DepartmentListFragment();
        cListFragment = new ContactsByNameListFragment();
        fragments.add(dListFragment);
        fragments.add(cListFragment);
        adapter = new myPagerAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        voiceAnswer = (TextView) findViewById(R.id.tab_1);
        healthPedia = (TextView) findViewById(R.id.tab_2);

        voiceAnswer.setOnClickListener(new MyOnClickListener(0));
        healthPedia.setOnClickListener(new MyOnClickListener(1));
        voiceAnswer.setTextColor(selectedColor);
        iv_tea_contacts_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_tea_contacts_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherContactsListActivity.this, ContactsTeaSearcheActivity.class);
                startActivity(intent);
//                List<ContactsBean> colplist = cListFragment.getListData();
//                if (colplist != null && colplist.size() > 0) {
//                    Intent intent = new Intent(TeacherContactsListActivity.this, ContactsTeaSearcheActivity.class);
//                    intent.putExtra("colplist", (Serializable) colplist);
//                    startActivity(intent);
//                }else{
//                    ToastManager.getInstance().showToast(TeacherContactsListActivity.this,"暂无数据");
//                }
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
                    break;
                case 1:
                    healthPedia.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
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

        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {
            Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);
            currIndex = index;
            animation.setFillAfter(true);
            animation.setDuration(100);
            imageView.startAnimation(animation);
            switch (index) {
                case 0:
                    voiceAnswer.setTextColor(selectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    break;
                case 1:
                    healthPedia.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    break;
                case 2:
                    voiceAnswer.setTextColor(unSelectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    break;
            }
        }
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
