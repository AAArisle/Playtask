package com.jnu.student;

public class Book {
    private String title;
    private int coverResourceId;

    public Book(String title, int coverResourceId) {
        this.title = title;
        this.coverResourceId = coverResourceId;
    }

    //返回图书名称String
    public String getTitle() {
        return title;
    }

    //返回封面图片资源Id
    public int getCoverResourceId() {
        return coverResourceId;
    }
}
