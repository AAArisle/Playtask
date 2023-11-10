package com.jnu.student;

public class Task {
    private String title;
    private String coin;
    private int type;

    public Task(String title, String coin, int type) {
        this.title = title;
        this.coin = coin;
        this.type = type;
    }

    //返回任务名称String
    public String getTitle() {
        return title;
    }

    //返回任务积分
    public String getCoin() {
        return coin;
    }

    //返回任务类型
    public int getType(){
        return type;
    }
}
