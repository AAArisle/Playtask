package com.jnu.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskListFragment extends Fragment
        implements RecycleViewTaskAdapter.SignalListener, RecycleViewTaskAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private TextView coinsTextView;
    private RecycleViewTaskAdapter adapter;
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ActivityResultLauncher<Intent> editTaskLauncher;
    private static List<Task> taskList = new ArrayList<>();
    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(int position) {
        // 处理 Item 的点击事件
        // 根据 position 获取相应的数据或执行相应的操作
        // 编辑任务
        Intent editIntent = new Intent(this.getContext(), TaskDetailsActivity.class);
        editIntent.putExtra("id",position);
        editIntent.putExtra("title", taskList.get(position).getTitle());
        editIntent.putExtra("coin", taskList.get(position).getCoin());
        editIntent.putExtra("times", taskList.get(position).getTimes());
        editIntent.putExtra("type", taskList.get(position).getType());
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

        adapter = new RecycleViewTaskAdapter(taskList);
        recyclerView.setAdapter(adapter);
        adapter.setSignalListener(this);

        registerForContextMenu(recyclerView);       //注册ContextMenu

        coinsTextView = rootView.findViewById(R.id.textView_coins);
        coinsTextView.setText(String.valueOf(Coins.coins));

        //添加任务的启动器
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
                            taskList.add(new Task(title, coin, times, type));
                            adapter.notifyItemInserted(taskList.size());
                        }
                    }
                });

        //修改任务的启动器
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
                            taskList.set(id, new Task(title, coin, times, type));
                            adapter.notifyItemChanged(id);
                        }
                    }
                });

        // 设置Item的点击事件监听器
        adapter.setOnItemClickListener(this);

        return rootView;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // 添加提醒
                break;
            case 1:
                // 删除任务
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(this.getContext())
                        .setTitle("你正在删除任务")
                        .setMessage("是否确定删除？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                taskList.remove(item.getOrder());
                                adapter.notifyItemRemoved(item.getOrder());
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                alertDialog.show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    
    @Override
    public void onSignalReceived() {
        coinsTextView.setText(String.valueOf(Coins.coins));
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //使用菜单填充器获取menu下的菜单资源文件
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tool_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_task) {
            // 启动另一个Activity来添加任务
            Intent addIntent = new Intent(this.getContext(), AddTaskActivity.class);
            addTaskLauncher.launch(addIntent);
        }
        else if (item.getItemId() == R.id.action_join_duty) {
            // 加入副本
        }
        else if (item.getItemId() == R.id.action_sort) {
            //排序
        }

        return super.onOptionsItemSelected(item);
    }
}