package com.toplion.cplusschool.TeacherContacts;

import com.toplion.cplusschool.Bean.ContactsBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<ContactsBean> {
    @Override
    public int compare(ContactsBean lhs, ContactsBean rhs) {
        // TODO Auto-generated method stub
        return sort(lhs, rhs);
    }
    private int sort(ContactsBean lhs, ContactsBean rhs) {
        // 获取ascii值
        int lhs_ascii = lhs.getFirstPinYin().toUpperCase().charAt(0);
        int rhs_ascii = rhs.getFirstPinYin().toUpperCase().charAt(0);
        // 判断若不是字母，则排在字母之后
        if (lhs_ascii < 65 || lhs_ascii > 90)
            return 1;
        else if (rhs_ascii < 65 || rhs_ascii > 90)
            return -1;
        else
            return lhs.getPinYin().compareTo(rhs.getPinYin());
    }
}
