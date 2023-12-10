package com.jnu.student.tasks;

import static com.jnu.student.tasks.Task.taskList0;
import static com.jnu.student.tasks.Task.taskList1;
import static com.jnu.student.tasks.Task.taskList2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.student.Coins;
import com.jnu.student.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NormalTaskFragment extends Fragment
        implements RecycleViewTaskAdapter.OnItemClickListener {
    private static RecyclerView recyclerView;
    static TextView emptyTextView;
    public static TextView coinsTextView;
    static ImageButton okImageButton;
    static RecycleViewTaskAdapter adapter;
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ActivityResultLauncher<Intent> editTaskLauncher;

    public NormalTaskFragment() {
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
            editIntent.putExtra("title", taskList2.get(position).getTitle());
            editIntent.putExtra("coin", taskList2.get(position).getCoin());
            editIntent.putExtra("times", taskList2.get(position).getTimes());
            editIntent.putExtra("type", taskList2.get(position).getType());
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
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        recyclerView = rootView.findViewById(R.id.recycle_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setLongClickable(true);

        adapter = new RecycleViewTaskAdapter(taskList2, this.getContext());
        recyclerView.setAdapter(adapter);

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
        if (taskList2.size() == 0){
            NormalTaskFragment.emptyTextView.setVisibility(View.VISIBLE);
        }
        else{
            NormalTaskFragment.emptyTextView.setVisibility(View.GONE);
        }

        registerForContextMenu(recyclerView);       //注册ContextMenu

        coinsTextView = rootView.findViewById(R.id.textView_coins);
        if (Coins.coins < 0) {
            coinsTextView.setTextColor(getResources().getColor(R.color.light_red, requireContext().getTheme()));
        }
        else {
            coinsTextView.setTextColor(getResources().getColor(R.color.black, requireContext().getTheme()));
        }
        coinsTextView.setText(String.valueOf(Coins.coins));

        okImageButton = rootView.findViewById(R.id.imageButton_ok);
        okImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (okImageButton.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ok, getContext().getTheme()).getConstantState())){
                    List<Task> taskList_ok = new ArrayList<>();
                    for (Task task: taskList2) {
                        if (task.getComplete() > 0){
                            taskList_ok.add(task);
                        }
                    }
                    emptyTextView.setText(R.string.completed_empty);
                    // 初始化 Empty View 的可见性
                    if (taskList_ok.size() == 0) {
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                    else{
                        emptyTextView.setVisibility(View.GONE);
                    }
                    RecycleViewOKTaskAdapter adapter_ok = new RecycleViewOKTaskAdapter(taskList_ok, getContext());
                    recyclerView.setAdapter(adapter_ok);
                    okImageButton.setImageResource(R.drawable.ok_checked);
                }
                else {
                    resetOK();
                }
            }
        });

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

    static void resetOK(){
        emptyTextView.setText(R.string.task_empty);
        // 初始化 Empty View 的可见性
        if (taskList2.size() == 0) {
            emptyTextView.setVisibility(View.VISIBLE);
        }
        else{
            emptyTextView.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(adapter);
        okImageButton.setImageResource(R.drawable.ok);
    }

    public void addTask(){
        addTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
                            int coin = data.getIntExtra("coin", 0);
                            int times = data.getIntExtra("times", 1);
                            int type = data.getIntExtra("type", 0);
                            if (type == 0) {
                                TaskListFragment.viewPager.setCurrentItem(0);
                                taskList0.add(new Task(title, coin, times, type));
                                DailyTaskFragment.adapter.notifyItemInserted(taskList0.size());
                                DailyTaskFragment.resetOK();
                            }
                            else if (type == 1) {
                                TaskListFragment.viewPager.setCurrentItem(1);
                                taskList1.add(new Task(title, coin, times, type));
                                WeeklyTaskFragment.adapter.notifyItemInserted(taskList1.size());
                                WeeklyTaskFragment.resetOK();
                            }
                            else if (type == 2) {
                                TaskListFragment.viewPager.setCurrentItem(2);
                                taskList2.add(new Task(title, coin, times, type));
                                NormalTaskFragment.adapter.notifyItemInserted(taskList2.size());
                                NormalTaskFragment.resetOK();
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
                            int coin = data.getIntExtra("coin", 0);
                            int type = data.getIntExtra("type",0);
                            int times = data.getIntExtra("times",1);
                            int id = data.getIntExtra("id",0);
                            if (type == 0) {
                                TaskListFragment.viewPager.setCurrentItem(0);
                                taskList2.remove(id);
                                NormalTaskFragment.adapter.notifyItemRemoved(id);
                                taskList0.add(new Task(title, coin, times, type));
                                DailyTaskFragment.adapter.notifyItemInserted(taskList0.size());
                                DailyTaskFragment.resetOK();
                            }
                            else if (type == 1) {
                                TaskListFragment.viewPager.setCurrentItem(1);
                                taskList2.remove(id);
                                NormalTaskFragment.adapter.notifyItemRemoved(id);
                                taskList1.add(new Task(title, coin, times, type));
                                WeeklyTaskFragment.adapter.notifyItemInserted(taskList1.size());
                                WeeklyTaskFragment.resetOK();
                            }
                            else if (type == 2) {
                                TaskListFragment.viewPager.setCurrentItem(2);
                                taskList2.set(id, new Task(title, coin, times, type));
                                NormalTaskFragment.adapter.notifyItemChanged(id);
                                NormalTaskFragment.resetOK();
                            }
                            // 设置 Empty View 的可见性
                            updateEmptyViewVisibility();
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //使用菜单填充器获取menu下的菜单资源文件
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear(); // 清空菜单项
        inflater.inflate(R.menu.task_menu, menu);
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