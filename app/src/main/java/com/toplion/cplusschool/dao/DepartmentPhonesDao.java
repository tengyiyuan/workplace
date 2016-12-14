package com.toplion.cplusschool.dao;

import android.content.Context;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.toplion.cplusschool.Bean.DepartmentBean;
import com.toplion.cplusschool.db.DBInsideHelper;

/**
 * Created by wang
 * on 2016/7/11.
 * 成绩列表dao
 * @Des 缓存学校办公电话搜索框 列表
 */
public class DepartmentPhonesDao extends AbDBDaoImpl<DepartmentBean>{
    public DepartmentPhonesDao(Context context) {
        super(new DBInsideHelper(context), DepartmentBean.class);
    }

}
