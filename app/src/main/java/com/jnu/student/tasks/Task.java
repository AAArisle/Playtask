package com.jnu.student.tasks;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public static List<Task> taskList0 = new ArrayList<>(); //每日任务
    public static List<Task> taskList1 = new ArrayList<>(); //每周任务
    public static List<Task> taskList2 = new ArrayList<>(); //普通任务
    private String title;
    private int coin;
    private int type;
    private int pin = 0;
    private int complete = 0;
    private int times;

    public Task(String title, int coin, int times, int type) {
        this.title = title;
        this.coin = coin;
        this.times = times;
        this.type = type;
    }

    //返回任务名称String
    public String getTitle() {
        return title;
    }

    //返回任务积分
    public int getCoin() {
        return coin;
    }

    //返回任务类型
    public int getType(){
        return type;
    }
    public boolean isPinned(){
        if (pin == 0){
            return false;
        }
        else {
            return true;
        }
    }

    public void changePinned(boolean ispinned) {
        if (ispinned){
            this.pin = 0;
        }
        else {
            this.pin = 1;
        }
    }

    public boolean isCompleted() {
        if (this.complete == this.times){
            return true;
        }
        else {
            return false;
        }
    }

    public void setComplete(int complete){
        this.complete = complete;
    }

    public int getComplete(){
        return this.complete;
    }

    public int getTimes(){
        return this.times;
    }
}
