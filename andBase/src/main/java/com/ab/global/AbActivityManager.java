package com.ab.global;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * © 2012 amsoft.cn
 * 名称：AbActivityManager.java
 * 描述：用于处理退出程序时可以退出所有的activity，而编写的通用类
 *
 * @author 还如一梦中
 * @version v1.0
 * @date 2015年4月10日 下午6:10:28
 */
public class AbActivityManager {

    private List<Activity> activityList = new CopyOnWriteArrayList<Activity>();
    private static AbActivityManager instance;


    private AbActivityManager() {
    }

    /**
     * 单例模式中获取唯一的AbActivityManager实例.
     *
     * @return
     */
    public static AbActivityManager getInstance() {
        if (null == instance) {
            instance = new AbActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到容器中.
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移除Activity从容器中.
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 遍历所有Activity并finish.
     */
    public void clearAllActivity() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(Intent intent) {
        try{
            for (int i = 0, size = activityList.size(); i < size; i++) {
                if (null != activityList.get(i)) {// 遍历堆栈中activity，来关闭
                    activityList.get(i).setResult(activityList.get(i).RESULT_OK,intent);
                    activityList.get(i).finish();
                }

            }
            activityList.clear();// 清空堆栈
        }catch (Exception e){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 根据上下文，退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity(null);// 关闭所有的activity
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0); // 关闭虚拟机
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityList) {// 遍历堆栈
            if (activity.getClass().equals(cls)) {// 匹配当前的类对象和结束类名是否一样
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);// 先从堆栈里移除
            activity.finish();// 再结束当前的Activity
            activity = null;// 之后把引用变为null，结束内存
        }
    }
}