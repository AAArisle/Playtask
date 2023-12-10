package com.jnu.student.tasks;

import static com.jnu.student.tasks.Task.taskList0;
import static com.jnu.student.tasks.Task.taskList1;
import static com.jnu.student.tasks.Task.taskList2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.MainActivity;
import com.jnu.student.PagerAdapter;
import com.jnu.student.R;

public class TaskListFragment extends Fragment {
    static ViewPager2 viewPager;

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        viewPager = rootView.findViewById(R.id.viewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);

        PagerAdapter pagerAdapter = new PagerAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        // 设置预加载的页面数量为 Fragment 的总数
        viewPager.setOffscreenPageLimit(pagerAdapter.getItemCount());
        // 刷新菜单
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);

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

        return rootView;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // 删除任务
        // 获取当前Fragment的类型
        int taskType = TaskListFragment.viewPager.getCurrentItem();
        int fragmentType = MainActivity.bottomViewPager.getCurrentItem();
        AlertDialog alertDialog;
        if (fragmentType == 0) {
            if (taskType == 0) {
                alertDialog = new AlertDialog.Builder(this.getContext())
                        .setTitle("你正在删除每日任务")
                        .setMessage("是否确定删除？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int taskPosition = item.getOrder();
                                taskList0.remove(taskPosition);
                                DailyTaskFragment.adapter.notifyItemRemoved(taskPosition);
                                DailyTaskFragment.adapter.notifyItemRangeChanged(taskPosition, taskList0.size() - taskPosition);
                                // 设置 Empty View 的可见性
                                if (taskList0.size() == 0) {
                                    DailyTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
                                } else {
                                    DailyTaskFragment.emptyTextView.setVisibility(View.GONE);
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                alertDialog.show();
            } else if (taskType == 1) {
                alertDialog = new AlertDialog.Builder(this.getContext())
                        .setTitle("你正在删除每周任务")
                        .setMessage("是否确定删除？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int taskPosition = item.getOrder();
                                taskList1.remove(taskPosition);
                                WeeklyTaskFragment.adapter.notifyItemRemoved(taskPosition);
                                WeeklyTaskFragment.adapter.notifyItemRangeChanged(taskPosition, taskList1.size() - taskPosition);
                                // 设置 Empty View 的可见性
                                if (taskList1.size() == 0) {
                                    WeeklyTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
                                } else {
                                    WeeklyTaskFragment.emptyTextView.setVisibility(View.GONE);
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                alertDialog.show();
            } else if (taskType == 2) {
                alertDialog = new AlertDialog.Builder(this.getContext())
                        .setTitle("你正在删除普通任务")
                        .setMessage("是否确定删除？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int taskPosition = item.getOrder();
                                taskList2.remove(taskPosition);
                                NormalTaskFragment.adapter.notifyItemRemoved(taskPosition);
                                NormalTaskFragment.adapter.notifyItemRangeChanged(taskPosition, taskList2.size() - taskPosition);
                                // 设置 Empty View 的可见性
                                if (taskList2.size() == 0) {
                                    NormalTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
                                } else {
                                    NormalTaskFragment.emptyTextView.setVisibility(View.GONE);
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                alertDialog.show();
            }
        }
        return super.onContextItemSelected(item);
    }
}
