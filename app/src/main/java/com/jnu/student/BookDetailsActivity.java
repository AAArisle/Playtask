package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class BookDetailsActivity extends AppCompatActivity {
    private ImageView coverImageView;
    private EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        coverImageView = findViewById(R.id.image_cover);
        titleEditText = findViewById(R.id.edit_text_title);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        String title = intent.getStringExtra("title");
        int coverResourceId = intent.getIntExtra("cover", R.drawable.book_2);

        titleEditText.setText(title);
        coverImageView.setImageResource(coverResourceId);

        // 设置修改按钮的点击事件
        Button addButton = findViewById(R.id.button_edit);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个包含书籍信息的Intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", titleEditText.getText().toString());
                resultIntent.putExtra("cover",coverResourceId);
                resultIntent.putExtra("id",id);

                // 设置结果码为RESULT_OK，表示成功修改书籍
                setResult(Activity.RESULT_OK, resultIntent);

                // 结束当前Activity，返回到上一个Activity
                finish();
            }
        });

        // 设置返回按钮的点击事件
        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}