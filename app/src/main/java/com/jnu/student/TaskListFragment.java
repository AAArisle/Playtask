package com.jnu.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskListFragment extends Fragment implements RecycleViewTaskAdapter.SignalListener{
    private RecyclerView recyclerView;
    private TextView coinsTextView;
    private RecycleViewTaskAdapter adapter;
    private ActivityResultLauncher<Intent> addBookLauncher;
    private ActivityResultLauncher<Intent> editBookLauncher;
    private static List<Task> taskList;
    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = new ArrayList<>();
        // 添加任务数据到 taskList
        taskList.addAll(Arrays.asList(
                new Task("任务1", "10", 1, 0),
                new Task("任务2", "20", 1, 0),
                new Task("任务3", "30", 1, 0)
        ));
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
        addBookLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
                            String coin = data.getStringExtra("coin");
                            int times = data.getIntExtra("times",1);
                            int type = data.getIntExtra("type", 0);
                            taskList.add(new Task(title, coin, times, type));
                            adapter.notifyItemInserted(taskList.size());
                        }
                    }
                    else{
                        //取消操作
                    }
                }
        );

        //修改任务的启动器
        editBookLauncher = registerForActivityResult(
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
                    else{
                        //取消操作
                    }
                }
        );
        return rootView;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast.makeText(this.getContext(),"操作成功",Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case 0:
                // 启动另一个Activity来添加任务
                Intent addIntent = new Intent(this.getContext(), AddTaskActivity.class);
                addBookLauncher.launch(addIntent);
                break;
            case 1:
                // 编辑任务
                Intent editIntent = new Intent(this.getContext(), TaskDetailsActivity.class);
                editIntent.putExtra("id",item.getOrder());
                editIntent.putExtra("title", taskList.get(item.getOrder()).getTitle());
                editIntent.putExtra("coin", taskList.get(item.getOrder()).getCoin());
                editIntent.putExtra("times", taskList.get(item.getOrder()).getTimes());
                editIntent.putExtra("type", taskList.get(item.getOrder()).getType());
                editBookLauncher.launch(editIntent);
                break;
            case 2:
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
}