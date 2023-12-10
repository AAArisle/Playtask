package com.jnu.student.rewards;

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

public class AddRewardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // 新奖励信息的EditText
    private EditText titleEditText;
    private EditText coinEditText;
    // 下拉栏
    private Spinner rewardTypeSpinner;
    private final static String[] rewardTypeArray = {"单次","不限"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);

        // 初始化新奖励信息的EditText
        coinEditText = findViewById(R.id.editText_Coin);
        titleEditText = findViewById(R.id.edit_text_title);
        // 下拉栏
        rewardTypeSpinner = findViewById(R.id.spinner_rewardType);

        // 设置添加按钮的点击事件
        Button addButton = findViewById(R.id.button_ok);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(titleEditText.getText())){
                    Toast.makeText(AddRewardActivity.this,"请输入标题",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(coinEditText.getText())){
                    Toast.makeText(AddRewardActivity.this,"请输入成就点数",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 创建一个包含新奖励信息的Intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", titleEditText.getText().toString());
                resultIntent.putExtra("coin", coinEditText.getText().toString());
                resultIntent.putExtra("type", rewardTypeSpinner.getSelectedItemPosition());

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
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_select,rewardTypeArray);
        rewardTypeSpinner.setAdapter(startAdapter);
        // 设置默认显示第一项
        rewardTypeSpinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}