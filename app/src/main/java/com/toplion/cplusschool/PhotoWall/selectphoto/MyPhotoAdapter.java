package com.toplion.cplusschool.PhotoWall.selectphoto;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.toplion.cplusschool.PhotoWall.selectphoto.util.ImageLoader;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ToastManager;

import java.util.List;

/**
 * Created by wang
 * on 2016/11/14.
 */

public class MyPhotoAdapter extends BaseAdapter {
    /**
     * 文件夹路径
     */
    private String mDirPath;
    private List<String> mDatas;
    private Context mContext;
    private String selectImgDri = "";

    public MyPhotoAdapter(Context context, List<String> mDatas, String dirPath) {
        this.mDirPath = dirPath;
        this.mDatas = mDatas;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (mDatas.get(position).equals("camera_flag")) {
                convertView = View.inflate(mContext, R.layout.select_photo_item_camera, null);
                viewHolder.id_item_image = (ImageView) convertView.findViewById(R.id.id_item_image);
            } else {
                convertView = View.inflate(mContext, R.layout.select_photo_item, null);
                viewHolder.id_item_image = (ImageView) convertView.findViewById(R.id.id_item_image);
                viewHolder.id_item_select = (ImageView) convertView.findViewById(R.id.id_item_select);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.id_item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatas.get(position).equals("camera_flag")) {
                    ToastManager.getInstance().showToast(mContext, "camera");
                } else {
                    viewHolder.id_item_image.setImageResource(R.mipmap.zhanwei);
                    viewHolder.id_item_select.setVisibility(View.VISIBLE);
                    String url = mDirPath + "/" + mDatas.get(position);
                    ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(url, viewHolder.id_item_image);
                    viewHolder.id_item_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 已经选择过该图片
                            if (selectImgDri.contains(mDirPath + "/" + mDatas.get(position))) {
                                viewHolder.id_item_select.setImageResource(R.mipmap.photo_check_default);
                                viewHolder.id_item_image.setColorFilter(null);
                            } else { // 未选择该图片
                                selectImgDri = mDirPath + "/" + mDatas.get(position);
                                viewHolder.id_item_select.setImageResource(R.mipmap.photo_check_selected);
                                viewHolder.id_item_image.setColorFilter(Color.parseColor("#77000000"));
                            }
                        }
                    });
                }
            }
        });
        return convertView;
    }


    class ViewHolder {
        private ImageView id_item_image;
        private ImageView id_item_select;

    }
}
