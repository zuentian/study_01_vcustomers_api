package com.example.zuer02.entity;

import java.util.Arrays;

public class Blog {

    private String title;
    private String author;
    private String content;
    private String blogCrtDate;
    private String blogId;
    private String[] categories;
    private String blogAltDate;

    public String getBlogAltDate() {
        return blogAltDate;
    }

    public void setBlogAltDate(String blogAltDate) {
        this.blogAltDate = blogAltDate;
    }

    public String getBlogCrtDate() {
        return blogCrtDate;
    }

    public void setBlogCrtDate(String blogCrtDate) {
        this.blogCrtDate = blogCrtDate;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", blogCrtDate='" + blogCrtDate + '\'' +
                ", blogId='" + blogId + '\'' +
                ", categories=" + Arrays.toString(categories) +
                ", blogAltDate='" + blogAltDate + '\'' +
                '}';
    }
}
