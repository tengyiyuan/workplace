package com.toplion.cplusschool.dao;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.toplion.cplusschool.Bean.ContactsBean;
import com.toplion.cplusschool.db.DBInsideHelper;

/**
 * Created by wang
 * on 2016/8/15
 * 通讯录dao
 * @Des 缓存通讯录搜索框 列表
 */
public class ContactsDao extends AbDBDaoImpl<ContactsBean>{
    public ContactsDao(Context context) {
        super(new DBInsideHelper(context), ContactsBean.class);
    }

}
