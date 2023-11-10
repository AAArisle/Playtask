package com.jnu.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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
                return new TaskListFragment(); // 图书Tab
            case 1:
                return new WebViewFragment(); // 新闻Tab
            case 2:
                return new TencentMapFragment(); // 地图Tab
            case 3:
                return new TencentMapFragment(); // 地图Tab
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        //Tab数目
        return 4;
    }
}
