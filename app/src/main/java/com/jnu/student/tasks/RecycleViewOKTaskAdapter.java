package com.jnu.student.tasks;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.student.Coins;
import com.jnu.student.R;
import com.jnu.student.rewards.RewardFragment;

import java.util.List;


public class RecycleViewOKTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Task> taskList;
    private Context mcontext;
    boolean isSortVisible = false;

    public RecycleViewOKTaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
        Task task = taskList.get(position);
        taskViewHolder.bind(task);

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{
        private final CheckBox checkBox;
        private final TextView textViewCoin;
        private final TextView textViewTaskTitle;
        private final ImageButton pinImageButton;
        private final ImageView sortImageView;
        private final TextView textViewTimes;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textViewCoin = itemView.findViewById(R.id.text_view_coin);
            textViewTaskTitle = itemView.findViewById(R.id.text_view_reward_title);
            pinImageButton = itemView.findViewById(R.id.imageButton_pin);
            sortImageView = itemView.findViewById(R.id.imageView_sort);
            textViewTimes = itemView.findViewById(R.id.textView_times);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(Task task) {
            textViewCoin.setText("+"+task.getCoin());
            textViewTaskTitle.setText(task.getTitle());
            textViewTimes.setText(task.getComplete() +"/"+ task.getTimes());

            // 根据任务的置顶状态设置相应的图标
            if (task.isPinned()) {
                pinImageButton.setImageResource(R.drawable.pin_checked);
            } else {
                pinImageButton.setImageResource(R.drawable.pin_unchecked);
            }

            // 设置 pinImageButton 的点击监听器，用于处理任务置顶状态的变化
            pinImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSortVisible) {
                        // 切换任务的置顶状态
                        task.changePinned(task.isPinned());

                        // 根据任务的置顶状态设置相应的图标
                        if (task.isPinned()) {
                            pinImageButton.setImageResource(R.drawable.pin_checked);
                        } else {
                            pinImageButton.setImageResource(R.drawable.pin_unchecked);
                        }
                    }
                }
            });

            textViewTaskTitle.setTextColor(mcontext.getResources().getColor(R.color.light_grey, mcontext.getTheme()));
            checkBox.setChecked(task.getComplete() > 0);
            // 设置 checkBox 的点击监听器，用于处理任务完成状态的变化
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        Coins.coins = Coins.coins + task.getCoin();
                        task.setComplete(task.getComplete()+1);
                    }
                    else {
                        Coins.coins = Coins.coins - task.getCoin();
                        task.setComplete(task.getComplete()-1);
                    }
                    // 刷新textview
                    if (task.getComplete() > 0) {
                        textViewTaskTitle.setTextColor(mcontext.getResources().getColor(R.color.light_grey, mcontext.getTheme()));
                    }
                    else {
                        textViewTaskTitle.setTextColor(mcontext.getResources().getColor(R.color.dark_grey, mcontext.getTheme()));
                    }
                    if (Coins.coins < 0) {
                        DailyTaskFragment.coinsTextView.setTextColor(mcontext.getResources().getColor(R.color.light_red, mcontext.getTheme()));
                        WeeklyTaskFragment.coinsTextView.setTextColor(mcontext.getResources().getColor(R.color.light_red, mcontext.getTheme()));
                        NormalTaskFragment.coinsTextView.setTextColor(mcontext.getResources().getColor(R.color.light_red, mcontext.getTheme()));
                        RewardFragment.coinsTextView.setTextColor(mcontext.getResources().getColor(R.color.light_red, mcontext.getTheme()));
                    }
                    else {
                        DailyTaskFragment.coinsTextView.setTextColor(mcontext.getResources().getColor(R.color.black, mcontext.getTheme()));
                        WeeklyTaskFragment.coinsTextView.setTextColor(mcontext.getResources().getColor(R.color.black, mcontext.getTheme()));
                        NormalTaskFragment.coinsTextView.setTextColor(mcontext.getResources().getColor(R.color.black, mcontext.getTheme()));
                        RewardFragment.coinsTextView.setTextColor(mcontext.getResources().getColor(R.color.black, mcontext.getTheme()));
                    }
                    DailyTaskFragment.coinsTextView.setText(String.valueOf(Coins.coins));
                    WeeklyTaskFragment.coinsTextView.setText(String.valueOf(Coins.coins));
                    NormalTaskFragment.coinsTextView.setText(String.valueOf(Coins.coins));
                    RewardFragment.coinsTextView.setText(String.valueOf(Coins.coins));
                    // 更新任务的完成状态
                    checkBox.setChecked(task.getComplete() > 0);
                    textViewTimes.setText(task.getComplete() +"/"+ task.getTimes());
                }
            });

            // 设置排序图标的可见性
            if (isSortVisible) {
                sortImageView.setVisibility(View.VISIBLE);
            } else {
                sortImageView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        }
    }
}
