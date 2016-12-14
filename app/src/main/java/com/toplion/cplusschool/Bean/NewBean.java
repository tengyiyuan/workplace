package com.toplion.cplusschool.Bean;

import java.io.Serializable;

/**
 * Created by toplion on 2016/6/27.
 */
public class NewBean implements Serializable {
    private String newsID;
    private String time;
    private String news_title;
    private String news_Info;

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_Info() {
        return news_Info;
    }

    public void setNews_Info(String news_Info) {
        this.news_Info = news_Info;
    }
}
