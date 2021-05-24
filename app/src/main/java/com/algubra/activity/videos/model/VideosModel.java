package com.algubra.activity.videos.model;

import java.io.Serializable;

/**
 * Created by gayatri on 5/5/17.
 */
public class VideosModel implements Serializable {
     String description;
    String id;
    String title;
    String video_link;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }
}
