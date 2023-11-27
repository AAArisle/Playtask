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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Objects;

public class DailyTaskFragment extends Fragment
        implements RecycleViewTaskAdapter.SignalListener, RecycleViewTaskAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    static TextView emptyTextView;
    static TextView coinsTextView;
    static RecycleViewTaskAdapter adapter;
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ActivityResultLauncher<Intent> editTaskLauncher;

    public DailyTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(int position) {
        // 处理 Item 的点击事件
        // 根据 position 获取相应的数据或执行相应的操作
        // 编辑任务
        if (!adapter.isSortVisible) {
            Intent editIntent = new Intent(this.getContext(), EditTaskActivity.class);
            editIntent.putExtra("id", position);
            editIntent.putExtra("title", taskList0.get(position).getTitle());
            editIntent.putExtra("coin", taskList0.get(position).getCoin());
            editIntent.putExtra("times", taskList0.get(position).getTimes());
            editIntent.putExtra("type", taskList0.get(position).getType());
            editTaskLauncher.launch(editIntent);
        }
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

        adapter = new RecycleViewTaskAdapter(taskList0);
        recyclerView.setAdapter(adapter);
        adapter.setSignalListener(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags;
                if (adapter.isSortVisible) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; //拖动
                }
                else {
                    dragFlags = 0; //禁用
                }
                int swipeFlags = 0; //滑动
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition){
                    for (int i = fromPosition; i < toPosition; i++)
                    {
                        Collections.swap(adapter.taskList, i , i+1);
                    }
                }
                else {
                    for (int i = fromPosition; i > toPosition; i--)
                    {
                        Collections.swap(adapter.taskList, i , i-1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        emptyTextView = rootView.findViewById(R.id.textView_task_empty);
        // 初始化 Empty View 的可见性
        if (taskList0.size() == 0) {
            DailyTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
        }
        else{
            DailyTaskFragment.emptyTextView.setVisibility(View.GONE);
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

    public void addTask(){
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
                            }
                            else if (type == 1) {
                                MainActivity.viewPager.setCurrentItem(1);
                                taskList1.add(new Task(title, coin, times, type));
                                WeeklyTaskFragment.adapter.notifyItemInserted(taskList1.size());
                            }
                            else if (type == 2) {
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
                                taskList0.set(id, new Task(title, coin, times, type));
                                DailyTaskFragment.adapter.notifyItemChanged(id);
                            }
                            else if (type == 1) {
                                MainActivity.viewPager.setCurrentItem(1);
                                taskList0.remove(id);
                                DailyTaskFragment.adapter.notifyItemRemoved(id);
                                taskList1.add(new Task(title, coin, times, type));
                                WeeklyTaskFragment.adapter.notifyItemInserted(taskList1.size());
                            }
                            else if (type == 2) {
                                MainActivity.viewPager.setCurrentItem(2);
                                taskList0.remove(id);
                                DailyTaskFragment.adapter.notifyItemRemoved(id);
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
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // 删除任务
        // 获取当前Fragment的类型
        int fragmentType = MainActivity.viewPager.getCurrentItem();
        AlertDialog alertDialog;
        if (fragmentType == 0) {
            alertDialog = new AlertDialog.Builder(this.getContext())
                    .setTitle("你正在删除每日任务")
                    .setMessage("是否确定删除？")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            taskList0.remove(item.getOrder());
                            DailyTaskFragment.adapter.notifyItemRemoved(item.getOrder());
                            // 设置 Empty View 的可见性
                            if (taskList0.size() == 0) {
                                DailyTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
                            }
                            else{
                                DailyTaskFragment.emptyTextView.setVisibility(View.GONE);
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create();
            alertDialog.show();
        }
        else if (fragmentType == 1) {
            alertDialog = new AlertDialog.Builder(this.getContext())
                    .setTitle("你正在删除每周任务")
                    .setMessage("是否确定删除？")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            taskList1.remove(item.getOrder());
                            WeeklyTaskFragment.adapter.notifyItemRemoved(item.getOrder());
                            // 设置 Empty View 的可见性
                            if (taskList1.size() == 0){
                                WeeklyTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
                            }
                            else{
                                WeeklyTaskFragment.emptyTextView.setVisibility(View.GONE);
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create();
            alertDialog.show();
        }
        else if (fragmentType == 2) {
            alertDialog = new AlertDialog.Builder(this.getContext())
                    .setTitle("你正在删除普通任务")
                    .setMessage("是否确定删除？")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            taskList2.remove(item.getOrder());
                            NormalTaskFragment.adapter.notifyItemRemoved(item.getOrder());
                            // 设置 Empty View 的可见性
                            if (taskList2.size() == 0){
                                NormalTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
                            }
                            else{
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
        return super.onContextItemSelected(item);
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
            adapter.isSortVisible = true;
            adapter.notifyDataSetChanged();
            requireActivity().invalidateOptionsMenu(); // 触发 onPrepareOptionsMenu() 调用
        }
        else if (item.getItemId() == R.id.action_sort_finish) {
            //排序完成
            adapter.isSortVisible = false;
            adapter.notifyDataSetChanged();
            requireActivity().invalidateOptionsMenu(); // 触发 onPrepareOptionsMenu() 调用
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem sortFinishMenuItem = menu.findItem(R.id.action_sort_finish);
        MenuItem addMenuItem = menu.findItem(R.id.action_add_menu);
        sortFinishMenuItem.setVisible(adapter.isSortVisible);
        addMenuItem.setVisible(!adapter.isSortVisible);
    }
}