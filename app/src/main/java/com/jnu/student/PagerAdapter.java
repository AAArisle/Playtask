package com.jnu.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jnu.student.tasks.DailyTaskFragment;
import com.jnu.student.tasks.NormalTaskFragment;
import com.jnu.student.tasks.WeeklyTaskFragment;

public class PagerAdapter extends FragmentStateAdapter {
    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //获得Tab对应的Fragment
        switch (position) {
            case 0:
                return new DailyTaskFragment(); // 每日任务
            case 1:
                return new WeeklyTaskFragment(); // 每周任务
            case 2:
                return new NormalTaskFragment(); // 普通任务
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        //Tab数目
        return 3;
    }
}
