package com.jnu.student;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class RecycleViewTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Task> taskList;
    private static SignalListener signalListener;
    private OnItemClickListener onItemClickListener;

    public void setSignalListener(SignalListener listener) {
        signalListener = listener;
    }

    public RecycleViewTaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BookViewHolder bookViewHolder = (BookViewHolder) holder;
        Task task = taskList.get(position);
        bookViewHolder.bind(task);

        // 设置Item的点击事件监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{
        private final CheckBox checkBox;
        private final TextView textViewCoin;
        private final TextView textViewTaskTitle;
        private final ImageButton pinImageButton;
        private final TextView textViewTimes;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textViewCoin = itemView.findViewById(R.id.text_view_coin);
            textViewTaskTitle = itemView.findViewById(R.id.text_view_task_title);
            pinImageButton = itemView.findViewById(R.id.imageButton_pin);
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
                    // 切换任务的置顶状态
                    task.changePinned(task.isPinned());

                    // 根据任务的置顶状态设置相应的图标
                    if (task.isPinned()) {
                        pinImageButton.setImageResource(R.drawable.pin_checked);
                    } else {
                        pinImageButton.setImageResource(R.drawable.pin_unchecked);
                    }
                }
            });

            checkBox.setChecked(task.isCompleted());
            // 设置 checkBox 的点击监听器，用于处理任务完成状态的变化
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        Coins.coins = Coins.coins + Integer.parseInt(task.getCoin());
                    }
                    else {
                        Coins.coins = Coins.coins - Integer.parseInt(task.getCoin());
                    }
                    // 发送信号刷新textview
                    if (signalListener != null) {
                        signalListener.onSignalReceived();
                    }
                    // 更新任务的完成状态
                    task.setCompleted(checkBox.isChecked());
                    checkBox.setChecked(task.isCompleted());
                    textViewTimes.setText(task.getComplete() +"/"+ task.getTimes());
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 1, this.getAdapterPosition(), "删除");
        }

    }

    public interface SignalListener {
        void onSignalReceived();
    }
}
