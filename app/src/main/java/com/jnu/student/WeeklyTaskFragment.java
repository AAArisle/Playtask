package com.jnu.student;

import static com.jnu.student.Task.taskList0;
import static com.jnu.student.Task.taskList1;
import static com.jnu.student.Task.taskList2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WeeklyTaskFragment extends Fragment
        implements RecycleViewTaskAdapter.SignalListener, RecycleViewTaskAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    static TextView emptyTextView;
    static TextView coinsTextView;
    static RecycleViewTaskAdapter adapter;
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ActivityResultLauncher<Intent> editTaskLauncher;

    public WeeklyTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(int position) {
        // 处理 Item 的点击事件
        // 根据 position 获取相应的数据或执行相应的操作
        // 编辑任务
        Intent editIntent = new Intent(this.getContext(), EditTaskActivity.class);
        editIntent.putExtra("id",position);
        editIntent.putExtra("title", taskList1.get(position).getTitle());
        editIntent.putExtra("coin", taskList1.get(position).getCoin());
        editIntent.putExtra("times", taskList1.get(position).getTimes());
        editIntent.putExtra("type", taskList1.get(position).getType());
        editTaskLauncher.launch(editIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycle_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setLongClickable(true);

        adapter = new RecycleViewTaskAdapter(taskList1);
        recyclerView.setAdapter(adapter);
        adapter.setSignalListener(this);

        emptyTextView = rootView.findViewById(R.id.textView_task_empty);
        // 初始化 Empty View 的可见性
        if (taskList1.size() == 0){
            WeeklyTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
        }
        else{
            WeeklyTaskFragment.emptyTextView.setVisibility(View.GONE);
        }

        registerForContextMenu(recyclerView);       //注册ContextMenu

        coinsTextView = rootView.findViewById(R.id.textView_coins);
        coinsTextView.setText(String.valueOf(Coins.coins));

        //添加任务的启动器
        addTask();

        //修改任务的启动器
        editTask();

        // 设置Item的点击事件监听器
        adapter.setOnItemClickListener(this);

        return rootView;
    }

    public void updateEmptyViewVisibility() {
        if (taskList0.size() == 0) {
            DailyTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
        }
        else{
            DailyTaskFragment.emptyTextView.setVisibility(View.GONE);
        }

        if (taskList1.size() == 0){
            WeeklyTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
        }
        else{
            WeeklyTaskFragment.emptyTextView.setVisibility(View.GONE);
        }

        if (taskList2.size() == 0){
            NormalTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
        }
        else{
            NormalTaskFragment.emptyTextView.setVisibility(View.GONE);
        }
    }

    public void addTask() {
        addTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
                            String coin = data.getStringExtra("coin");
                            int times = data.getIntExtra("times", 1);
                            int type = data.getIntExtra("type", 0);
                            if (type == 0) {
                                MainActivity.viewPager.setCurrentItem(0);
                                taskList0.add(new Task(title, coin, times, type));
                                DailyTaskFragment.adapter.notifyItemInserted(taskList0.size());
                            } else if (type == 1) {
                                MainActivity.viewPager.setCurrentItem(1);
                                taskList1.add(new Task(title, coin, times, type));
                                WeeklyTaskFragment.adapter.notifyItemInserted(taskList1.size());
                            } else if (type == 2) {
                                MainActivity.viewPager.setCurrentItem(2);
                                taskList2.add(new Task(title, coin, times, type));
                                NormalTaskFragment.adapter.notifyItemInserted(taskList2.size());
                            }
                            // 设置 Empty View 的可见性
                            updateEmptyViewVisibility();
                        }
                    }
                });
    }

    public void editTask(){
        editTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
                            String coin = data.getStringExtra("coin");
                            int type = data.getIntExtra("type",0);
                            int times = data.getIntExtra("times",1);
                            int id = data.getIntExtra("id",0);
                            if (type == 0) {
                                MainActivity.viewPager.setCurrentItem(0);
                                taskList1.remove(id);
                                WeeklyTaskFragment.adapter.notifyItemRemoved(id);
                                taskList0.add(new Task(title, coin, times, type));
                                DailyTaskFragment.adapter.notifyItemInserted(taskList0.size());
                            }
                            else if (type == 1) {
                                MainActivity.viewPager.setCurrentItem(1);
                                taskList1.set(id, new Task(title, coin, times, type));
                                WeeklyTaskFragment.adapter.notifyItemChanged(id);
                            }
                            else if (type == 2) {
                                MainActivity.viewPager.setCurrentItem(2);
                                taskList1.remove(id);
                                WeeklyTaskFragment.adapter.notifyItemRemoved(id);
                                taskList2.add(new Task(title, coin, times, type));
                                NormalTaskFragment.adapter.notifyItemInserted(taskList2.size());
                            }
                            // 设置 Empty View 的可见性
                            updateEmptyViewVisibility();
                        }
                    }
                });
    }

    @Override
    public void onSignalReceived() {
        DailyTaskFragment.coinsTextView.setText(String.valueOf(Coins.coins));
        WeeklyTaskFragment.coinsTextView.setText(String.valueOf(Coins.coins));
        NormalTaskFragment.coinsTextView.setText(String.valueOf(Coins.coins));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //使用菜单填充器获取menu下的菜单资源文件
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear(); // 清空菜单项
        inflater.inflate(R.menu.tool_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_task) {
            // 启动另一个Activity来添加任务
            Intent addIntent = new Intent(this.getContext(), AddTaskActivity.class);
            addTaskLauncher.launch(addIntent);
        }
        else if (item.getItemId() == R.id.action_sort) {
            //排序
        }

        return super.onOptionsItemSelected(item);
    }
}