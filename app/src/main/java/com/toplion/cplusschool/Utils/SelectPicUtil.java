package com.toplion.cplusschool.Utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 选择本地图片工具类
 * <br>
 * 因为直接获取图片容易崩溃，所以直接存入SD卡，再获取
 * <p>
 * 使用方法：
 * <br>
 * 1、调用getByAlbum、getByCamera去获取图片
 * <br>
 * 2、在onActivityResult中调用本工具类的onActivityResult方法进行处理
 * <br>
 * 3、onActivityResult返回的Bitmap记得空指针判断
 * <p>
 * <br><br>
 * PS：本工具类只能处理裁剪图片，如果不想裁剪，不使用本工具类的onActivityResult，自己做处理即可
 * <p>
 * Created by wang
 * on 2016/10/18.
 */
public class SelectPicUtil {
    public static final int GET_BY_ALBUM = 801;//如果有冲突，记得修改
    public static final int GET_BY_CAMERA = 802;//拍完照裁剪
     public static final int GET_BY_CAMERANOCROP = 803;//拍完照不裁剪
    public static final int CROP = 804;//如果有冲突，记得修改
    public static final int IMGCROP = 805;

    private static Uri requestUri;
    private static String ImageName;//图片名称
    public static final String IMAGE_UNSPECIFIED = "image/*";

    /**
     * 从相册获取图片
     */
    public static void getByAlbum(Activity act) {
        // 选择照片
        Intent getAlbum = new Intent(Intent.ACTION_PICK, null);
        getAlbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        act.startActivityForResult(getAlbum, GET_BY_ALBUM);
    }

    public static Intent goImgCrop(Uri uri, int w, int h, int aspectX, int aspectY) {
        return crop(uri, w, h, aspectX, aspectY);
    }

    /**
     * 通过拍照获取图片
     */
    public static void  getByCamera(Activity act, boolean isCrop) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            ImageName = "/" + getNowDate() + ".jpg";
            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), ImageName)));
            getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            act.startActivityForResult(getImageByCamera,isCrop?GET_BY_CAMERA:GET_BY_CAMERANOCROP);
        } else {
            Log.e("error", "请确认已经插入SD卡");
        }
    }

    private static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cld = Calendar.getInstance();
        cld.setTime(new Date());
        return sdf.format(cld.getTime());
    }

    /**
     * 处理获取的图片，注意判断空指针
     */
    public static Bitmap onActivityResult(Activity act, int requestCode, int resultCode, Intent data, int w, int h, int aspectX, int aspectY) {
        Bitmap bm = null;
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            switch (requestCode) {
                case GET_BY_ALBUM:
                    uri = data.getData();
//                    uri = dealUri(act, uri);//适配4.4系统
                    act.startActivityForResult(crop(uri, w, h, aspectX, aspectY), CROP);
                    break;
                case GET_BY_CAMERA:
                    // 设置文件保存路径这里放在跟目录下
                    File picture = new File(Environment.getExternalStorageDirectory() + ImageName);
                    uri = Uri.fromFile(picture);
                    act.startActivityForResult(crop(uri, w, h, aspectX, aspectY), CROP);
                    break;
                case CROP:
                    bm = dealCrop(act, requestUri);
                    break;
            }
        }
        return bm;
    }

    /**
     * 处理获取的图片，注意判断空指针
     */
    public static Uri onActivityResultUri(Activity act, int requestCode, int resultCode, Intent data, int w, int h, int aspectX, int aspectY) {
        Uri u = null;
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            switch (requestCode) {
                case GET_BY_ALBUM:
                    uri = data.getData();
                    act.startActivityForResult(crop(uri, w, h, aspectX, aspectY), CROP);
                    break;
                case GET_BY_CAMERA:
                    // 设置文件保存路径这里放在跟目录下
                    File picture = new File(Environment.getExternalStorageDirectory() + ImageName);
                    uri = Uri.fromFile(picture);
                    act.startActivityForResult(crop(uri, w, h, aspectX, aspectY), CROP);
                    break;
                case CROP:
                    u = requestUri;
                    break;
            }
        }
        return u;
    }

    /**
     * 不裁剪回调
     * @param act
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public static Uri onActivityResultUri(int requestCode, int resultCode){
        Uri u = null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GET_BY_CAMERANOCROP:
                    // 设置文件保存路径这里放在跟目录下
                    File picture = new File(Environment.getExternalStorageDirectory() + ImageName);
                    u = Uri.fromFile(picture);
                    break;
                case IMGCROP:
                     u = requestUri;
                break;
            }
        }
        return u;
    }

    /**
     * 处理获取的图片，注意判断空指针 不需要裁剪图片
     */
    public  Uri onActivityResultUriNoCut(Activity act, int requestCode, int resultCode, Intent data) {
        Uri u = null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GET_BY_ALBUM:
                    u = data.getData();
                    act.startActivityForResult(data, CROP);
                    break;
                case GET_BY_CAMERA:
                    // 设置文件保存路径这里放在跟目录下
                    File picture = new File(Environment.getExternalStorageDirectory() + ImageName);
                    u = Uri.fromFile(picture);
                    act.startActivityForResult(data, CROP);
                    break;
            }
        }
        return u;
    }

    /**
     * 裁剪，例如：输出100*100大小的图片，宽高比例是1:1
     *
     * @param w       输出宽
     * @param h       输出高
     * @param aspectX 宽比例
     * @param aspectY 高比例
     */
    public static Intent crop(Uri uri, int w, int h, int aspectX, int aspectY) {
        if (w == 0 && h == 0) {
            w = h = 480;
        }
        if (aspectX == 0 && aspectY == 0) {
            aspectX = aspectY = 1;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 照片URL地址
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", w);
        intent.putExtra("outputY", h);
        if (new File(uri.getPath()).getName().endsWith(".JPG") || new File(uri.getPath()).getName().endsWith(".PNG")
                || new File(uri.getPath()).getName().endsWith(".jpg") || new File(uri.getPath()).getName().endsWith(".png")) {
            requestUri = Tools.getCropImageUri(new File(uri.getPath()).getName());
        } else {
            requestUri = Tools.getCropImageUri(new File(uri.getPath()).getName() + ".jpg");
        }
        // 输出路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, requestUri);
        // 输出格式
//        intent.putExtra("outputFormat","jpg");
        // 不启用人脸识别
//        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        return intent;
    }

    /**
     * 处理裁剪，获取裁剪后的图片
     */
    public static Bitmap dealCrop(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 适配4.4系统
     */
    public  Uri dealUri(Activity act, Uri uri) {
        String filePath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(act, uri)) {
                String wholeID = DocumentsContract.getDocumentId(uri);
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = act.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            } else {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = act.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
            }
        }
        uri = Uri.fromFile(new File(filePath));
        return uri;
    }
}
