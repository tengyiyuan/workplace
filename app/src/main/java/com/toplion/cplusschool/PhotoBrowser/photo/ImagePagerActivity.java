package com.toplion.cplusschool.PhotoBrowser.photo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.Utils.Tools;

/**
 * 图片查看器
 */
public class ImagePagerActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private TextView saveimg;
    private int saveindex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        final ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);
        saveimg = (TextView) findViewById(R.id.saveimg);
        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
                saveindex = arg0;
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
        saveimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbImageLoader.getInstance(ImagePagerActivity.this).download(urls.get(saveindex), BaseApplication.ScreenWidth, BaseApplication.ScreenWidth, new AbImageLoader.OnImageListener2() {

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onError() {
                        ToastManager.getInstance().showToast(ImagePagerActivity.this, "图片保存失败");
                    }

                    @Override
                    public void onSuccess(final Bitmap bitmap) {
                        if (Tools.isHasSDCard()) {
                            AbTask abTask = AbTask.newInstance();
                            AbTaskItem item = new AbTaskItem();
                            item.setListener(new AbTaskListener() {
                                @Override
                                public void update() {
                                    super.update();
                                    AbDialogUtil.removeDialog(ImagePagerActivity.this);
                                    ToastManager.getInstance().showToast(ImagePagerActivity.this, "图片保存成功");
                                }
                                @Override
                                public void get() {
                                    super.get();
                                    AbDialogUtil.showProgressDialog(ImagePagerActivity.this,0,"正在下载图片");
                                    saveBitmap(bitmap);
                                }
                            });
                            abTask.execute(item);
                        } else {
                            ToastManager.getInstance().showToast(ImagePagerActivity.this, "无法保存，不存在SD卡");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

    }

    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bm) {
        File f = new File(Tools.getPicsFileDir(), System.currentTimeMillis() + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(f);
            intent.setData(uri);
            ImagePagerActivity.this.sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
