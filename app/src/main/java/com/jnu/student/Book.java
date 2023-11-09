package com.jnu.student;

public class Book {
    private String title;
    private String coin;

    public Book(String title, String coin) {
        this.title = title;
        this.coin = coin;
    }

    //返回图书名称String
    public String getTitle() {
        return title;
    }

    //返回任务积分
    public String getCoin() {
        return coin;
    }
}
