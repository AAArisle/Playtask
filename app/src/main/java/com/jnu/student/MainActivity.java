package com.jnu.student;

import static com.jnu.student.Coins.coins;
import static com.jnu.student.Reward.rewardList;
import static com.jnu.student.Task.taskList0;
import static com.jnu.student.Task.taskList1;
import static com.jnu.student.Task.taskList2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    static ViewPager2 bottomViewPager;
    private BottomNavigationView bottomMenu;
    Gson gson = new Gson();
    private AlarmManager alarmMgr;
    private PendingIntent dailyAlarmIntent;
    private PendingIntent weeklyAlarmIntent;

    @Override
    public void onPause() {
        super.onPause();

        // 存储数据
        SharedPreferences sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("tasklist0", gson.toJson(taskList0));
        editor.putString("tasklist1", gson.toJson(taskList1));
        editor.putString("tasklist2", gson.toJson(taskList2));

        editor.putString("rewardlist", gson.toJson(rewardList));

        editor.putInt("coins", coins);
        editor.apply();
        // 存储结束

        // 计算钉住任务数
        int Pinned_Tasks = 0;
        for (Task task: taskList0){
            if (task.isPinned() && !task.isCompleted()){
                Pinned_Tasks++;
            }
        }
        for (Task task: taskList1){
            if (task.isPinned() && !task.isCompleted()){
                Pinned_Tasks++;
            }
        }
        for (Task task: taskList2){
            if (task.isPinned() && !task.isCompleted()){
                Pinned_Tasks++;
            }
        }
        // 如果有被钉住的未完成的任务，就显示一条通知
        if (Pinned_Tasks > 0) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(MainActivity.this, "pinned_task_tip_channel")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("PlayTask")
                    .setContentText(Pinned_Tasks + "个钉住的任务待完成")
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true) // 设置通知为持久通知
                    .setContentIntent(PendingIntent.getActivity(
                            this,0,new Intent(
                                    this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE))
                    .build();
            manager.notify(1, notification);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 取消通知
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1); // 根据通知的ID取消对应的通知
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 底部导航栏
        bottomViewPager = findViewById(R.id.bottom_viewPager);
        bottomMenu = findViewById(R.id.bottom_menu);

        BottomAdapter bottomAdapter =new BottomAdapter(getSupportFragmentManager(), getLifecycle());
        bottomViewPager.setAdapter(bottomAdapter);
        // 设置预加载的页面数量为 Fragment 的总数
        bottomViewPager.setOffscreenPageLimit(bottomAdapter.getItemCount());
        // 禁用用户输入（左右滑动）
        bottomViewPager.setUserInputEnabled(false);
        // 刷新菜单
        bottomViewPager.setCurrentItem(1);
        bottomViewPager.setCurrentItem(0);

        bottomMenu.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.task) {
                    bottomViewPager.setCurrentItem(0);
                    return true;
                }
                if (item.getItemId() == R.id.reward) {
                    bottomViewPager.setCurrentItem(1);
                    return true;
                }
                return false;
            }
        });

        // 导入数据
        SharedPreferences sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE);

        String tasklist0 = sharedPreferences.getString("tasklist0",null);
        String tasklist1 = sharedPreferences.getString("tasklist1",null);
        String tasklist2 = sharedPreferences.getString("tasklist2",null);
        String rewardlist = sharedPreferences.getString("rewardlist",null);
        if (tasklist0 != null) {
            taskList0 = gson.fromJson(tasklist0, new TypeToken<List<Task>>() {}.getType());
        }
        if (tasklist1 != null) {
            taskList1 = gson.fromJson(tasklist1, new TypeToken<List<Task>>() {}.getType());
        }
        if (tasklist2 != null) {
            taskList2 = gson.fromJson(tasklist2, new TypeToken<List<Task>>() {}.getType());
        }
        if (rewardlist != null) {
            rewardList = gson.fromJson(rewardlist, new TypeToken<List<Reward>>() {}.getType());
        }

        coins = sharedPreferences.getInt("coins", 0);
        //导入结束

        // 创建通知通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "pinned_task_tip_channel",
                    "钉住任务提示",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(false);
            channel.setSound(null,null); //静音和不震动

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 定时任务
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent dailyIntent = new Intent(this, AlarmReceiver.class);
        Intent weeklyIntent = new Intent(this, AlarmReceiver.class);
        // 添加额外的标识来区分两种闹钟
        dailyIntent.putExtra("alarmType", "daily");
        weeklyIntent.putExtra("alarmType", "weekly");

        dailyAlarmIntent = PendingIntent.getBroadcast(this, 0, dailyIntent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);
        weeklyAlarmIntent = PendingIntent.getBroadcast(this, 1, weeklyIntent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);

        // 每天凌晨2点刷新每日任务
        Calendar dailyCalendar = Calendar.getInstance();
        dailyCalendar.setTimeInMillis(System.currentTimeMillis());
        dailyCalendar.set(Calendar.HOUR_OF_DAY, 2);
        // 设置每隔一天执行一次
        alarmMgr.setInexactRepeating(AlarmManager.RTC, dailyCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, dailyAlarmIntent);

        // 每周一凌晨2点刷新每周任务
        dailyCalendar.setTimeInMillis(System.currentTimeMillis());
        dailyCalendar.set(Calendar.DAY_OF_WEEK, 2);
        dailyCalendar.set(Calendar.HOUR_OF_DAY, 2);
        // 设置每周一执行一次
        alarmMgr.setInexactRepeating(AlarmManager.RTC, dailyCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, weeklyAlarmIntent);
    }
}