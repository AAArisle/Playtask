package com.jnu.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BottomAdapter extends FragmentStateAdapter {
    public BottomAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //获得Tab对应的Fragment
        switch (position) {
            case 0:
                return new TaskListFragment(); // 任务
            case 1:
                return new RewardFragment(); // 奖励
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        //Tab数目
        return 2;
    }
}
