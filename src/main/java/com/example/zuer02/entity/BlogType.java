package com.example.zuer02.entity;

public class BlogType {

    private String blogTypeId;
    private String blogId;
    private String blogTypeName;
    private String blogTypeCrtDate;
    private String blogTypeAltDate;

    public String getBlogTypeAltDate() {
        return blogTypeAltDate;
    }

    public void setBlogTypeAltDate(String blogTypeAltDate) {
        this.blogTypeAltDate = blogTypeAltDate;
    }

    public String getBlogTypeId() {
        return blogTypeId;
    }

    public void setBlogTypeId(String blogTypeId) {
        this.blogTypeId = blogTypeId;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getBlogTypeName() {
        return blogTypeName;
    }

    public void setBlogTypeName(String blogTypeName) {
        this.blogTypeName = blogTypeName;
    }

    public String getBlogTypeCrtDate() {
        return blogTypeCrtDate;
    }

    public void setBlogTypeCrtDate(String blogTypeCrtDate) {
        this.blogTypeCrtDate = blogTypeCrtDate;
    }

    @Override
    public String toString() {
        return "BlogType{" +
                "blogTypeId='" + blogTypeId + '\'' +
                ", blogId='" + blogId + '\'' +
                ", blogTypeName='" + blogTypeName + '\'' +
                ", blogTypeCrtDate='" + blogTypeCrtDate + '\'' +
                ", blogTypeAltDate='" + blogTypeAltDate + '\'' +
                '}';
    }
}
