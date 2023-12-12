package com.jnu.student.rewards;

import static com.jnu.student.Coins.coins;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.student.Coins;
import com.jnu.student.R;
import com.jnu.student.tasks.DailyTaskFragment;
import com.jnu.student.tasks.NormalTaskFragment;
import com.jnu.student.tasks.WeeklyTaskFragment;

import java.util.List;

public class RecycleViewRewardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Reward> rewardList;
    private Context mcontext;
    private OnItemClickListener onItemClickListener;
    boolean isSortVisible = false;

    public RecycleViewRewardAdapter(List<Reward> rewardList, Context context) {
        this.rewardList = rewardList;
        this.mcontext = context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_item_layout, parent, false);

        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RewardViewHolder rewardViewHolder = (RewardViewHolder) holder;
        Reward reward = rewardList.get(position);
        rewardViewHolder.bind(reward);

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
        return rewardList.size();
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{
        private final CheckBox checkBox;
        private final TextView textViewCoin;
        private final TextView textViewRewardTitle;
        private final ImageView sortImageView;
        private final TextView textViewTimes;
        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textViewCoin = itemView.findViewById(R.id.text_view_coin);
            textViewRewardTitle = itemView.findViewById(R.id.text_view_reward_title);
            sortImageView = itemView.findViewById(R.id.imageView_sort);
            textViewTimes = itemView.findViewById(R.id.textView_times);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(Reward reward) {
            textViewCoin.setText("-" + reward.getCoin());
            textViewRewardTitle.setText(reward.getTitle());
            if (reward.getType() == 0) {
                textViewTimes.setText(reward.getComplete() + "/1");
            } else {
                textViewTimes.setText(reward.getComplete() + "/∞");
            }

            checkBox.setChecked(false);
            // 设置 checkBox 的点击监听器，用于处理奖励完成状态的变化
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(checkBox.getContext())
                            .setTitle("满足奖励")
                            .setMessage("确定花费 "+reward.getCoin()+" 点成就来满足你的奖励？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    coins = coins - reward.getCoin();
                                    reward.setComplete(reward.getComplete()+1);
                                    // 刷新textview
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
                                    // 处理单次奖励的删除
                                    if (reward.getType() == 0) {
                                        textViewTimes.setText(reward.getComplete() + "/1");
                                        int rewardPosition = getAdapterPosition();
                                        rewardList.remove(rewardPosition);
                                        notifyItemRemoved(rewardPosition);
                                        notifyItemRangeChanged(rewardPosition, rewardList.size() - rewardPosition);
                                        // 设置 Empty View 的可见性
                                        if (rewardList.size() == 0) {
                                            RewardFragment.emptyTextView.setVisibility(View.VISIBLE);
                                        } else {
                                            RewardFragment.emptyTextView.setVisibility(View.GONE);
                                        }
                                    }
                                    // 更新无限奖励的完成状态
                                    else if (reward.getType() == 1) {
                                        checkBox.setChecked(false);
                                        textViewTimes.setText(reward.getComplete() + "/∞");
                                    }
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkBox.setChecked(false);
                                }
                            })
                            .setCancelable(false) // 设置对话框不可以通过点击区域外被取消
                            .create();
                    alertDialog.show();
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
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
            if (!isSortVisible) {
                menu.add(0, 1, this.getAdapterPosition(), "删除");
            }
        }

    }
}
