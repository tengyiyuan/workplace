package com.toplion.cplusschool.dao;

import android.content.Context;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.toplion.cplusschool.Bean.GradeInfoBean;
import com.toplion.cplusschool.db.DBInsideHelper;

/**
 * Created by wang
 * on 2016/5/27.
 * 成绩列表dao
 * @Des 缓存成绩列表数据数据
 */
public class GradesDao extends AbDBDaoImpl<GradeInfoBean> {
    public GradesDao(Context context) {
        super(new DBInsideHelper(context), GradeInfoBean.class);
    }
}
