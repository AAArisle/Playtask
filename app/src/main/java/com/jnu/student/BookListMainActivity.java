package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        RecyclerView recyclerView = findViewById(R.id.recycle_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecycleViewBookAdapter adapter = new RecycleViewBookAdapter(getListBooks());
        recyclerView.setAdapter(adapter);
    }

    public List<Book> getListBooks(){
        List<Book> bookList = new ArrayList<>();
        // 添加书籍数据到 bookList
        bookList.addAll(Arrays.asList(
                new Book("软件项目管理案例教程（第4版）", R.drawable.book_2),
                new Book("创新工程实践", R.drawable.book_no_name),
                new Book("信息安全数学基础（第2版）", R.drawable.book_1)
            ));
        return bookList;
    }

}