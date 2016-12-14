package com.toplion.cplusschool.db;

import android.content.Context;

import com.ab.db.orm.AbDBHelper;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.Bean.Course;
import com.toplion.cplusschool.Bean.CourseDate;
import com.toplion.cplusschool.Bean.DepartmentBean;
import com.toplion.cplusschool.Bean.SchoolBean;

/**
 * 名称：DBInsideHelper.java
 * 描述：手机data/data下面的数据库
 *
 * @author tengyiyuan
 * @version v1.1.9
 * @date：2016-4-15 下午3:50:18
 */
public class DBInsideHelper extends AbDBHelper {
    // 数据库名
    private static final String DBNAME = "dlgf.db";

    // 当前数据库的版本
    private static final int DBVERSION = 1;
    // 要初始化的表
    private static final Class<?>[] clazz = {Course.class,CourseDate.class, DepartmentBean.class, ContactsBean.class, SchoolBean.class};

    public DBInsideHelper(Context context) {
        super(context, DBNAME, null, DBVERSION, clazz);
    }
}



