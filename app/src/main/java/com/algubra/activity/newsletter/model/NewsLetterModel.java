package com.algubra.activity.newsletter.model;

import java.io.Serializable;

/**
 * Created by user2 on 4/5/17.
 */
public class NewsLetterModel implements Serializable{
    String newsTitle;
    String newsId;
    String newsDescription;
    String newsImage;
    String newsWebLink;
    String newsPdfLink;
    String newsCreatedTime;


    public String getTitle() {
        return newsTitle;
    }

    public void setTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsWebLink() {
        return newsWebLink;
    }

    public void setNewsWebLink(String newsWebLink) {
        this.newsWebLink = newsWebLink;
    }

    public String getNewsPdfLink() {
        return newsPdfLink;
    }

    public void setNewsPdfLink(String newsPdfLink) {
        this.newsPdfLink = newsPdfLink;
    }

    public String getNewsCreatedTime() {
        return newsCreatedTime;
    }

    public void setNewsCreatedTime(String newsCreatedTime) {
        this.newsCreatedTime = newsCreatedTime;
    }
}
