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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookListFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecycleViewBookAdapter adapter;
    private ActivityResultLauncher<Intent> addBookLauncher;
    private ActivityResultLauncher<Intent> editBookLauncher;
    private List<Book> bookList = getListBooks();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public List<Book> getListBooks() {
        List<Book> bookList = new ArrayList<>();
        // 添加书籍数据到 bookList
        bookList.addAll(Arrays.asList(
                new Book("软件项目管理案例教程（第4版）", R.drawable.book_2),
                new Book("创新工程实践", R.drawable.book_no_name),
                new Book("信息安全数学基础（第2版）", R.drawable.book_1)
        ));
        return bookList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_list_main);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycle_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setLongClickable(true);

        adapter = new RecycleViewBookAdapter(bookList);
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);       //注册ContextMenu

        //添加书籍的启动器
        addBookLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
//                            Toast.makeText(this,title,Toast.LENGTH_SHORT).show();
                            bookList.add(new Book(title, R.drawable.book_no_name));
                            adapter.notifyItemInserted(bookList.size());
                        }
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        //取消操作
                    }
                }
        );

        //修改书籍的启动器
        editBookLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null) {
                            String title = data.getStringExtra("title");
                            int coverResourceId = data.getIntExtra("cover",R.drawable.book_2);
                            int id = data.getIntExtra("id",0);
                            bookList.set(id, new Book(title, coverResourceId));
                            adapter.notifyItemChanged(id);
                        }
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        //取消操作
                    }
                }
        );
        return rootView;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast.makeText(this.getContext(),"操作成功"+item.getOrder(),Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case 0:
                // 启动另一个Activity来添加书籍
                Intent addIntent = new Intent(this.getContext(), AddBookActivity.class);
                addBookLauncher.launch(addIntent);
                break;
            case 1:
                // 编辑书籍
                Intent editIntent = new Intent(this.getContext(), BookDetailsActivity.class);
                editIntent.putExtra("id",item.getOrder());
                editIntent.putExtra("title", bookList.get(item.getOrder()).getTitle());
                editIntent.putExtra("cover", bookList.get(item.getOrder()).getCoverResourceId());
                editBookLauncher.launch(editIntent);
                break;
            case 2:
                // 删除书籍
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(this.getContext())
                        .setTitle("你正在删除书籍")
                        .setMessage("是否确定删除？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bookList.remove(item.getOrder());
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
}