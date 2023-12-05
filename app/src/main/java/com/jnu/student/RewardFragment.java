package com.jnu.student;

import static com.jnu.student.Reward.rewardList;

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

public class RewardFragment extends Fragment
        implements RecycleViewRewardAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    static TextView emptyTextView;
    static TextView coinsTextView;
    static RecycleViewRewardAdapter adapter;
    private ActivityResultLauncher<Intent> addRewardLauncher;
    private ActivityResultLauncher<Intent> editRewardLauncher;

    public RewardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(int position) {
        // 处理 Item 的点击事件
        // 根据 position 获取相应的数据或执行相应的操作
        // 编辑奖励
        if (!adapter.isSortVisible) {
            Intent editIntent = new Intent(this.getContext(), EditRewardActivity.class);
            editIntent.putExtra("id", position);
            editIntent.putExtra("title", rewardList.get(position).getTitle());
            editIntent.putExtra("coin", rewardList.get(position).getCoin());
            editIntent.putExtra("type", rewardList.get(position).getType());
            editRewardLauncher.launch(editIntent);
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
        View rootView = inflater.inflate(R.layout.fragment_reward, container, false);

        recyclerView = rootView.findViewById(R.id.recycle_view_rewards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setLongClickable(true);

        adapter = new RecycleViewRewardAdapter(rewardList, this.getContext());
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
                        Collections.swap(adapter.rewardList, i , i+1);
                    }
                }
                else {
                    for (int i = fromPosition; i > toPosition; i--)
                    {
                        Collections.swap(adapter.rewardList, i , i-1);
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

        emptyTextView = rootView.findViewById(R.id.textView_reward_empty);
        // 初始化 Empty View 的可见性
        if (rewardList.size() == 0) {
            RewardFragment.emptyTextView.setVisibility(View.VISIBLE);
        }
        else{
            RewardFragment.emptyTextView.setVisibility(View.GONE);
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

        //添加奖励的启动器
        addReward();

        //修改奖励的启动器
        editReward();

        // 设置Item的点击事件监听器
        adapter.setOnItemClickListener(this);

        return rootView;
    }

    public void addReward(){
        addRewardLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
                            String coin = data.getStringExtra("coin");
                            int type = data.getIntExtra("type", 0);

                            rewardList.add(new Reward(title, coin, type));
                            adapter.notifyItemInserted(rewardList.size());

                            // 设置 Empty View 的可见性
                            if (rewardList.size() == 0) {
                                RewardFragment.emptyTextView.setVisibility(View.VISIBLE);
                            } else {
                                RewardFragment.emptyTextView.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    public void editReward(){
        editRewardLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
                            String coin = data.getStringExtra("coin");
                            int type = data.getIntExtra("type", 0);
                            int id = data.getIntExtra("id", 0);

                            rewardList.set(id, new Reward(title, coin, type));
                            adapter.notifyItemChanged(id);

                            // 设置 Empty View 的可见性
                            if (rewardList.size() == 0) {
                                RewardFragment.emptyTextView.setVisibility(View.VISIBLE);
                            } else {
                                RewardFragment.emptyTextView.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // 删除奖励
        // 获取当前Fragment的类型
        int fragmentType = MainActivity.bottomViewPager.getCurrentItem();
        if (fragmentType == 1) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this.getContext())
                    .setTitle("你正在删除奖励")
                    .setMessage("是否确定删除？")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rewardList.remove(item.getOrder());
                            RewardFragment.adapter.notifyItemRemoved(item.getOrder());
                            // 设置 Empty View 的可见性
                            if (rewardList.size() == 0) {
                                RewardFragment.emptyTextView.setVisibility(View.VISIBLE);
                            } else {
                                RewardFragment.emptyTextView.setVisibility(View.GONE);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //使用菜单填充器获取menu下的菜单资源文件
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear(); // 清空菜单项
        inflater.inflate(R.menu.reward_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_reward) {
            // 启动另一个Activity来添加奖励
            Intent addIntent = new Intent(this.getContext(), AddRewardActivity.class);
            addRewardLauncher.launch(addIntent);
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