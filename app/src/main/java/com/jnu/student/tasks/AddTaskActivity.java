package com.jnu.student.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jnu.student.R;

public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // 新任务信息的EditText
    private EditText titleEditText;
    private EditText coinEditText;
    private EditText timesEditText;
    // 下拉栏
    private Spinner taskTypeSpinner;
    private final static String[] taskTypeArray = {"每日任务","每周任务","普通任务"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // 初始化新任务信息的EditText
        coinEditText = findViewById(R.id.editText_Coin);
        titleEditText = findViewById(R.id.edit_text_title);
        timesEditText = findViewById(R.id.editText_Number);
        // 下拉栏
        taskTypeSpinner = findViewById(R.id.spinner_taskType);

        // 设置添加按钮的点击事件
        Button addButton = findViewById(R.id.button_ok);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(titleEditText.getText())){
                    Toast.makeText(AddTaskActivity.this,"请输入标题",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(coinEditText.getText())){
                    Toast.makeText(AddTaskActivity.this,"请输入成就点数",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 创建一个包含新任务信息的Intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", titleEditText.getText().toString());
                resultIntent.putExtra("coin", coinEditText.getText().toString());
                if (!(timesEditText.getText().toString().isEmpty())){
                    resultIntent.putExtra("times", Integer.parseInt(timesEditText.getText().toString()));
                }
                resultIntent.putExtra("type", taskTypeSpinner.getSelectedItemPosition());

                // 设置结果码为RESULT_OK，表示成功添加
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
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_select,taskTypeArray);
        taskTypeSpinner.setAdapter(startAdapter);
        // 设置默认显示第一项
        taskTypeSpinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}