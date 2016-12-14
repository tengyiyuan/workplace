package com.toplion.cplusschool.dao;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;

import com.toplion.cplusschool.Bean.Course;
import com.toplion.cplusschool.db.DBInsideHelper;

/**
 * 名称：UserInsideDao.java
 * 描述：本地数据库 在data下面
 *
 * @author tengyiyuan
 * @version v1.1.9
 * @date：2016-4-15 下午4:12:36
 */
public class UserInsideDao extends AbDBDaoImpl<Course> {
    public UserInsideDao(Context context) {
        super(new DBInsideHelper(context), Course.class);
    }
}
