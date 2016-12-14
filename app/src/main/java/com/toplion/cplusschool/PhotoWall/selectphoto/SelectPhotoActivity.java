package com.toplion.cplusschool.PhotoWall.SelectPhoto;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.PhotoWall.SelectPhoto.util.ListImageDirPopWindow;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.SelectPicUtil;
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

public class SelectPhotoActivity extends BaseActivity  implements ListImageDirPopWindow.OnImageDirSelected {
    private ImageView iv_select_photo_return;
    private GridView gv_photo;//图片列表
    private RelativeLayout rl_bottom_ly;//底部布局
    private TextView tv_choose_dir;//文件夹名称
    private TextView tv_total_count;//下一步
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
    private int totalCount = 0;
    /**
     * 所有的图片
     */
    private List<String> mImgs;
    private MyPhotoAdapter myPhotoAdapter;
    private int screenWidth;
    private int mScreenHeight;
    private ListImageDirPopWindow mListImageDirPopupWindow;
    private List<String> selectedImgs;
    private int canSelectImgCount = 1; //最多可选图片数量

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            int i = msg.what;
            if(i == 233){
                AbDialogUtil.removeDialog(SelectPhotoActivity.this);
                // 为View绑定数据
                data2View();
                // 初始化展示文件夹的popupWindw
                initListDirPopupWindw();
            }else if(i == 232){
                selectedImgs = new ArrayList<String>();
                selectedImgs = myPhotoAdapter.getSelectedImgs();
                tv_total_count.setText("下一步("+selectedImgs.size()+"/"+canSelectImgCount+")");
                if(selectedImgs.size()>0){
                    tv_total_count.setEnabled(true);
                }else{
                    tv_total_count.setEnabled(false);
                }
            }
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
        canSelectImgCount = getIntent().getIntExtra("maxImgCount",1);
        iv_select_photo_return = (ImageView) findViewById(R.id.iv_select_photo_return);
        gv_photo = (GridView) findViewById(R.id.gv_photo);
        rl_bottom_ly = (RelativeLayout) findViewById(R.id.rl_bottom_ly);
        tv_choose_dir = (TextView) findViewById(R.id.tv_choose_dir);
        tv_total_count = (TextView) findViewById(R.id.tv_total_count);
        if(canSelectImgCount>1){
            tv_total_count.setVisibility(View.VISIBLE);
        }
        screenWidth = BaseApplication.ScreenWidth;
        mScreenHeight = BaseApplication.ScreenHeight;
        setListener();
    }
    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw(){
        mListImageDirPopupWindow = new ListImageDirPopWindow(this,(int) (mScreenHeight * 0.6),mImageFloders);
        // 设置弹出窗体可点击
        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss(){
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SelectPicUtil.CROP){
            Uri uri = SelectPicUtil.onActivityResultUri(this, requestCode, resultCode, data, screenWidth, screenWidth, 1, 1);
            if (uri != null && uri.getPath() != null) {
                List<String> imgList = new ArrayList<String>();
                imgList.add(uri.getPath());
                setResultImgList(imgList);
            }
        }else if(requestCode == SelectPicUtil.GET_BY_CAMERANOCROP ){
            Uri uri = SelectPicUtil.onActivityResultUri(requestCode, resultCode);
            if (uri != null && uri.getPath() != null) {
                setResultImgPath(uri.getPath());
            }
        }
    }
    /**
     * 设置返回值 列表
     * @param imgList 选择的照片
     */
    private void setResultImgList(List<String> imgList){
        Intent intent = new Intent();
        intent.putStringArrayListExtra("imgList" , (ArrayList<String>) imgList);
        setResult(RESULT_OK,intent);
        finish();
    }
    /**
     * 设置返回值 一张
     * @param path 选择的照片
     */
    private void setResultImgPath(String path){
        Intent intent = new Intent();
        intent.putExtra("imgPath",path);
        setResult(RESULT_OK,intent);
        finish();
    }
    @Override
    protected void setListener() {
        super.setListener();
        //下一步
        tv_total_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultImgList(selectedImgs);
//                if(selectedImgs.size()>0){
//                    String imgPath = selectedImgs.get(0);
//                    Uri uri = Uri.fromFile(new File(imgPath));//将paht转换uri （切记必须这样转换，直接使用Uri.parse()报错）
//                    if(uri!=null&&uri.getPath()!=null){
//                       Intent intent = SelectPicUtil.goImgCrop(uri, screenWidth, screenWidth, 1, 1);
//                        startActivityForResult(intent,SelectPicUtil.IMGCROP);
//                    }
//                }
            }
        });
        //camera点击事件
//        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ImageView cameraImg = (ImageView) parent.findViewById(R.id.id_item_image_camera);
//                cameraImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SelectPicUtil.getByCamera(SelectPhotoActivity.this,false);//拍完照不裁剪
//                    }
//                });
//            }
//        });
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        rl_bottom_ly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                mListImageDirPopupWindow.showAsDropUp(rl_bottom_ly);
               // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .6f;
                getWindow().setAttributes(lp);
                rl_bottom_ly.bringToFront();
            }
        });
        iv_select_photo_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**
     * 为View绑定数据
     */
    private void data2View(){
        if (mImgDir == null){
            ToastManager.getInstance().showToast(SelectPhotoActivity.this,"擦，一张图片没扫描到");
            return;
        }
        mImgs = new LinkedList<String>();
        List list = Arrays.asList(mImgDir.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String filename)
            {
                return filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg");
            }
        }));
        mImgs.add(0,"camera_flag");
        mImgs.addAll(list);
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        myPhotoAdapter = new MyPhotoAdapter(this, mImgs,R.layout.select_photo_item,mImgDir.getAbsolutePath(),screenWidth/3-8,mHandler);
        myPhotoAdapter.setSelectedMaxImgCount(canSelectImgCount);
        gv_photo.setAdapter(myPhotoAdapter);
//        tv_total_count.setText(totalCount + "张");
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages()
    {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            ToastManager.getInstance().showToast(SelectPhotoActivity.this,"暂无外部存储");
            return;
        }
        AbDialogUtil.showProgressDialog(SelectPhotoActivity.this, 0, "正在加载...");
        // 显示进度条
        new Thread(new Runnable(){
            @Override
            public void run(){
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,MediaStore.Images.Media.MIME_TYPE + "=? or "
                      + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_TAKEN + " DESC ");
                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()){
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
                    if (mDirPaths.contains(dirPath)){
                        continue;
                    }else{
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    int picSize = parentFile.list(new FilenameFilter(){
                        @Override
                        public boolean accept(File dir, String filename){
                            return filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg");
                        }
                    }).length;
                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize){
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

    @Override
    public void selected(ImageFloder floder) {
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String filename)
            {
                return filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg");
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        myPhotoAdapter = null;//释放
        myPhotoAdapter = new MyPhotoAdapter(SelectPhotoActivity.this, mImgs,R.layout.select_photo_item, mImgDir.getAbsolutePath(),screenWidth/3-8,mHandler);
        myPhotoAdapter.setSelectedMaxImgCount(canSelectImgCount);
        gv_photo.setAdapter(myPhotoAdapter);
//        tv_total_count.setText(floder.getCount() + "张");
        tv_choose_dir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myPhotoAdapter!=null){
            myPhotoAdapter.clearImgsList();
        }
    }
}
