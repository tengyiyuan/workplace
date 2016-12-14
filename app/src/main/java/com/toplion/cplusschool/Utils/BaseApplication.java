package com.toplion.cplusschool.Utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.toplion.cplusschool.Common.Constants;

import java.io.File;


/**
 * 程序入口
 *
 * @author WangShengbo
 */
public class BaseApplication extends Application {


    /**
     * 单例 *
     */
    private static BaseApplication mBaseApplication;

    public static int ScreenWidth, ScreenHeight;

    /**
     * 单例 Context *
     */
    private Context context;
    private SharePreferenceUtils share;

    /**
     * 单例的BaseApplication
     *
     * @return
     */
    public static BaseApplication getInstance() {
        if (mBaseApplication == null) {
            synchronized (BaseApplication.class) {
                if (mBaseApplication == null) {
                    mBaseApplication = new BaseApplication();
                    return mBaseApplication;
                }
            }
        }
        return mBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**推送开启
         * 1.0
         */
        share = new SharePreferenceUtils(this);
//        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);            // 初始化 JPush
        /**
         * 搜集程序崩溃日志
         * begin
         */
        SaveException mSaveException = SaveException.getInstance();
        mSaveException.init(getApplicationContext());

//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(getApplicationContext()); //在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
        /**
         * -------end-------
         */
        //友盟统计打开调试模式.
        //打开调试模式后，您可以在logcat中查看您的数据是否成功发送到友盟服务器，以及集成过程中的出错原因等，友盟相关log的tag是MobclickAgent。
//		MobclickAgent.setDebugMode( true );

        mBaseApplication = this;
        context = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        //    SDKInitializer.initialize(getApplicationContext());
        /** 初始化 **/
        start();

    }

    /**
     * 初始化信息
     */
    public void start() {

        // 文件路径设置
        String parentPath = null;

        // 存在SDCARD的时候，路径设置到SDCARD
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            parentPath =
                    Environment.getExternalStorageDirectory().getPath()
                            + File.separatorChar + getPackageName();
            // 不存在SDCARD的时候
        } else {
            parentPath =
                    Environment.getDataDirectory().getPath()
                            + File.separatorChar + "data" + File.separatorChar + getPackageName();
        }

        // 路径设置
        Constants.rootPath = parentPath;
        Constants.imgPath = parentPath + "/imagecache/";

        // 创建目录
        new File(Constants.imgPath).mkdirs();

        // 图片加载和缓存路径
        initImageLoader();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // 清除缓存和內存
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.clearMemoryCache();
        imageLoader.clearDiscCache();
    }

    public void initImageLoader() {

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        // 缓存路径,第二个参数
        File cacheDir =
                StorageUtils.getOwnCacheDirectory(mBaseApplication, Constants.imgLoaderPath);

        DisplayImageOptions options =
                new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                        .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
                        .build();

        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context)
                        // 缓存在内存的图片的宽和高度
//                        .memoryCacheExtraOptions(480, 800)
                        .threadPriority(Thread.NORM_PRIORITY - 3)
                        .threadPoolSize(3)
                        // 配置软引用
                        .memoryCache(new WeakMemoryCache())
                        .memoryCacheSize(cacheSize)
                        // 缓存到内存的最大数据BaseBean.java
                        .discCacheSize(50 * 1024 * 1024)
                        . // 缓存到文件的最大数据
                        discCacheFileCount(500)
                        // 文件数量
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        .defaultDisplayImageOptions(options)
                        // 上面的options对象，一些属性配置
                        .discCache(new UnlimitedDiscCache(cacheDir)).writeDebugLogs().build();

        ImageLoader.getInstance().init(config); // 全局初始化设置
    }
}
