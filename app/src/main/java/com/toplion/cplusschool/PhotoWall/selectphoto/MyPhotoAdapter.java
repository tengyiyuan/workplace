package com.toplion.cplusschool.PhotoWall.SelectPhoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.toplion.cplusschool.PhotoWall.SelectPhoto.util.CommonAdapter;
import com.toplion.cplusschool.PhotoWall.SelectPhoto.util.ViewHolder;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ImageUtil;
import com.toplion.cplusschool.Utils.SelectPicUtil;
import com.toplion.cplusschool.Utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wang
 * on 2016/11/15.
 * 加载本地图片adapter
 */

public class MyPhotoAdapter extends CommonAdapter<String> {
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    private static List<String> mSelectedImage = new ArrayList<String>();
    /**
     * 文件夹路径
     */
    private String mDirPath;
    private List<String> mDatas;
    private Context mContext;
    private int imgWidth = 0; //显示图片的宽高
    private Handler mHandler;
    private int selectedMaxImgCount = 1;//图片的最大选择数量  默认为1

    public void setSelectedMaxImgCount(int selectedMaxImgCount) {
        this.selectedMaxImgCount = selectedMaxImgCount;
    }

    public MyPhotoAdapter(Context context, List<String> mDatas, int itemLayoutId, String dirPath, int sWidth, Handler handler) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.mDatas = mDatas;
        this.mContext = context;
        this.imgWidth = sWidth;
        this.mHandler = handler;
    }

    @Override
    public void convert(final ViewHolder helper, final int item) {
        //设置no_pic
        helper.setImageBitmap(R.id.id_item_image, ImageUtil.readBitMap(mContext, R.mipmap.ic_select_camera));
        //设置no_pic
        helper.setImageBitmap(R.id.id_item_image, ImageUtil.readBitMap(mContext, R.mipmap.zhanwei));
        //设置no_selected
        helper.setImageBitmap(R.id.id_item_select, ImageUtil.readBitMap(mContext, R.mipmap.photo_check_default));
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + mDatas.get(item));
        ImageView cImageView = helper.getView(R.id.id_item_image_camera);
        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imgWidth, imgWidth);
        mImageView.setLayoutParams(layoutParams);
        cImageView.setLayoutParams(layoutParams);
        if (item == 0 && mDatas.get(item).equals("camera_flag")) {
            cImageView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            mSelect.setVisibility(View.GONE);
        } else {
            cImageView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            mSelect.setVisibility(View.VISIBLE);
        }
        if (selectedMaxImgCount <= 1) {
            mSelect.setVisibility(View.GONE);
        }
        mImageView.setColorFilter(null);

        //设置ImageView的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                if (selectedMaxImgCount > 1) {
                    // 已经选择过该图片
                    if (mSelectedImage.contains(mDirPath + "/" + mDatas.get(item))) {
                        mSelectedImage.remove(mDirPath + "/" + mDatas.get(item));
                        mSelect.setImageBitmap(ImageUtil.readBitMap(mContext, R.mipmap.photo_check_default));
                        mImageView.setColorFilter(null);
                    } else {// 未选择该图片
                        if (mSelectedImage.size() < selectedMaxImgCount) {
                            mSelectedImage.add(mDirPath + "/" + mDatas.get(item));
                            mSelect.setImageBitmap(ImageUtil.readBitMap(mContext, R.mipmap.photo_check_selected));
                            mImageView.setColorFilter(Color.parseColor("#77000000"));
                        } else {
                            ToastManager.getInstance().showToast(mContext, "最多选择一张图片");
                        }
                    }
                    mHandler.sendEmptyMessage(232);
                } else {
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                    mSelectedImage.add(mDirPath + "/" + mDatas.get(item));
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("imgList", (ArrayList<String>) mSelectedImage);
                    ((Activity) mContext).setResult(RESULT_OK, intent);
                    ((Activity) mContext).finish();
                }
            }
        });
        cImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPicUtil.getByCamera((SelectPhotoActivity) mContext, false);//拍完照不裁剪
            }
        });
        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + mDatas.get(item))) {
            mSelect.setImageBitmap(ImageUtil.readBitMap(mContext, R.mipmap.photo_check_selected));
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }

    //提供获取选择照片方法
    public List<String> getSelectedImgs() {
        return mSelectedImage;
    }

    public void clearImgsList() {
        mSelectedImage.clear();
    }
}
