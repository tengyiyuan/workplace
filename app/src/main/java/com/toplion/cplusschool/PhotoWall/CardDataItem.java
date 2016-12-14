package com.toplion.cplusschool.PhotoWall;

import java.io.Serializable;

/**
 * 卡片数据装载对象
 *
 * @author tengyy
 */
public class CardDataItem implements Serializable{
    int photoId;
    String imagePath;
    String userName;
    String schoolName;
    String sex;
    String userHeadPath;
    int likeNum;
    int imageNum;
}
