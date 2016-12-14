package com.toplion.cplusschool.PhotoWall.selectphoto;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ToastManager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wang
 * on 2016/11/14.
 * @选取照片
 */

public class SelectPhotoActivity extends BaseActivity{
    private GridView gv_photo;
    private RelativeLayout rl_bottom_ly;
    private TextView tv_choose_dir;
    private TextView tv_total_count;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    int totalCount = 0;
    /**
     * 所有的图片
     */
    private List<String> mImgs;
    private MyPhotoAdapter myPhotoAdapter;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
//            initListDirPopupWindw();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_photo);
        init();
        getImages();
    }

    @Override
    protected void init() {
        super.init();
        gv_photo = (GridView) findViewById(R.id.gv_photo);
        rl_bottom_ly = (RelativeLayout) findViewById(R.id.rl_bottom_ly);
        tv_choose_dir = (TextView) findViewById(R.id.tv_choose_dir);
        tv_total_count = (TextView) findViewById(R.id.tv_total_count);
    }

    /**
     * 为View绑定数据
     */
    private void data2View()
    {
        if (mImgDir == null)
        {
            ToastManager.getInstance().showToast(SelectPhotoActivity.this,"擦，一张图片没扫描到");
            return;
        }
        mImgs = new LinkedList<String>();
        List list = Arrays.asList(mImgDir.list());
        mImgs.add(0,"camera_flag");
        mImgs.addAll(list);
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        myPhotoAdapter = new MyPhotoAdapter(this, mImgs,mImgDir.getAbsolutePath());
        gv_photo.setAdapter(myPhotoAdapter);
        tv_total_count.setText(totalCount + "张");
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages()
    {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            ToastManager.getInstance().showToast(SelectPhotoActivity.this,"暂无外部存储");
            return;
        }
        // 显示进度条
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,MediaStore.Images.Media.MIME_TYPE + "=? or "
                      + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] { "image/jpeg", "image/png" },MediaStore.Images.Media.DATE_MODIFIED);
                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext())
                {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath))
                    {
                        continue;
                    }else{
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    int picSize = parentFile.list(new FilenameFilter()
                    {
                        @Override
                        public boolean accept(File dir, String filename)
                        {
                            return filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg");
                        }
                    }).length;
                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize)
                    {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(233);
            }
        }).start();
    }
}
