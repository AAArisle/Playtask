package com.jnu.student.rewards;

import java.util.ArrayList;
import java.util.List;

public class Reward {
    public static List<Reward> rewardList = new ArrayList<>(); // 奖励列表
    private String title;
    private int coin;
    private int type;
    private int complete = 0;

    public Reward(String title, int coin, int type){
        this.title = title;
        this.coin = coin;
        this.type = type;
    }

    //返回奖励名称String
    public String getTitle() {
        return title;
    }

    //返回成就点数
    public int getCoin() {
        return coin;
    }

    //返回奖励类型
    public int getType(){
        return type;
    }

    public void setComplete(int complete){
        this.complete = complete;
    }

    public int getComplete(){
        return this.complete;
    }
}
