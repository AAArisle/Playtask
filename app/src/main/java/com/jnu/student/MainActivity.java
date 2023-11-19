package com.jnu.student;

import static com.jnu.student.Coins.coins;
import static com.jnu.student.Task.taskList0;
import static com.jnu.student.Task.taskList1;
import static com.jnu.student.Task.taskList2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    static ViewPager2 viewPager;
    private TabLayout tabLayout;
    Gson gson = new Gson();

    @Override
    public void onPause() {
        super.onPause();
        // 存储数据
        SharedPreferences sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("tasklist0", gson.toJson(taskList0));
        editor.putString("tasklist1", gson.toJson(taskList1));
        editor.putString("tasklist2", gson.toJson(taskList2));

        editor.putInt("coins", coins);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 导入数据
        SharedPreferences sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE);

        String tasklist0 = sharedPreferences.getString("tasklist0",null);
        String tasklist1 = sharedPreferences.getString("tasklist1",null);
        String tasklist2 = sharedPreferences.getString("tasklist2",null);
        if (tasklist0 != null) {
            taskList0 = gson.fromJson(tasklist0, new TypeToken<List<Task>>() {}.getType());
        }
        if (tasklist1 != null) {
            taskList1 = gson.fromJson(tasklist1, new TypeToken<List<Task>>() {}.getType());
        }
        if (tasklist2 != null) {
            taskList2 = gson.fromJson(tasklist2, new TypeToken<List<Task>>() {}.getType());
        }

        coins = sharedPreferences.getInt("coins", 0);
        //导入结束

        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        PagerAdapter pagerAdapter =new PagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        // 设置预加载的页面数量为 Fragment 的总数
        viewPager.setOffscreenPageLimit(pagerAdapter.getItemCount());

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("每日任务");
                            break;
                        case 1:
                            tab.setText("每周任务");
                            break;
                        case 2:
                            tab.setText("普通任务");
                            break;
                        case 3:
                            tab.setText("副本任务");
                            break;
                    }
                }).attach();

    }
}