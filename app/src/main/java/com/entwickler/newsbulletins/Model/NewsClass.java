package com.entwickler.newsbulletins.Model;

public class NewsClass {
    String title,description,content,time,image,urlNews,sourceName,urlSource;

    public NewsClass(String title, String description, String content, String time, String image, String urlNews, String sourceName, String urlSource) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.time = time;
        this.image = image;
        this.urlNews = urlNews;
        this.sourceName = sourceName;
        this.urlSource = urlSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrlNews() {
        return urlNews;
    }

    public void setUrlNews(String urlNews) {
        this.urlNews = urlNews;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }
}
