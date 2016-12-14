package com.toplion.cplusschool.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbImageUtil;
import com.toplion.cplusschool.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by wang
 * on 2016/9/20.
 *
 * @图片处理工具
 */
public class ImageUtil {
    /**
     * 网络图片变成drawable
     *
     * @param imageUrl
     * @return
     */
    public Drawable ImgUrlToDrawable(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过第二个参数(文件名)来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), null);
        } catch (IOException e) {
            Log.d("skythinking", e.getMessage());
        }
        if (drawable == null) {
            Log.d("skythinking", "null drawable");
        } else {
            Log.d("skythinking", "not null drawable");
        }
        return drawable;
    }
    /**
     * Uri to Bitmap
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param img
     * @param roundPx 半径
     */
    public static void loadHead(Context context, String url, final ImageView img, int imgWidth, int imgHeight, final int roundPx) {
        AbImageLoader.getInstance(context).download(url,imgWidth,imgHeight, new AbImageLoader.OnImageListener2() {
            @Override
            public void onEmpty() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                bitmap = AbImageUtil.toRoundBitmap(bitmap, roundPx);
                img.setImageBitmap(bitmap);
            }
        });
    }

}
