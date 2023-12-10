package com.jnu.student;

import static com.jnu.student.tasks.Task.taskList0;
import static com.jnu.student.tasks.Task.taskList1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jnu.student.tasks.Task;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 处理每日闹钟
        if (intent.getStringExtra("alarmType").equals("daily")) {
            for (Task task : taskList0) {
                task.setComplete(0);
            }
        }
        // 处理每周闹钟
        if (intent.getStringExtra("alarmType").equals("weekly")) {
            for (Task task : taskList1) {
                task.setComplete(0);
            }
        }
    }
}