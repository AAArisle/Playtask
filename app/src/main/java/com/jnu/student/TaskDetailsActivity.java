package com.jnu.student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TaskDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText titleEditText;
    private EditText coinEditText;
    private EditText timesEditText;
    private Spinner taskTpyeSpinner;
    private final static String[] taskTpyeArray = {"每日任务","每周任务","普通任务"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        coinEditText = findViewById(R.id.editText_Coin);
        titleEditText = findViewById(R.id.edit_text_title);
        timesEditText = findViewById(R.id.editText_Number);
        // 下拉栏
        taskTpyeSpinner = findViewById(R.id.spinner_taskType);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        String title = intent.getStringExtra("title");
        String coin = intent.getStringExtra("coin");
        int times = intent.getIntExtra("times",1);
        int type = intent.getIntExtra("type",0);

        // 把原来的信息显示出来
        titleEditText.setText(title);
        coinEditText.setText(coin);
        timesEditText.setText(String.valueOf(times));

        // 设置修改按钮的点击事件
        Button addButton = findViewById(R.id.button_ok);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个包含修改后信息的Intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", titleEditText.getText().toString());
                resultIntent.putExtra("coin",coinEditText.getText().toString());
                resultIntent.putExtra("times", Integer.parseInt(timesEditText.getText().toString()));
                resultIntent.putExtra("id",id);

                // 设置结果码为RESULT_OK，表示成功修改
                setResult(Activity.RESULT_OK, resultIntent);

                // 结束当前Activity，返回到上一个Activity
                finish();
            }
        });

        // 标签页左边的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 声明数组适配器
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_select,taskTpyeArray);
        taskTpyeSpinner.setAdapter(startAdapter);
        // 设置显示原来的选项
        taskTpyeSpinner.setSelection(type);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}