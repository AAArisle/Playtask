package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView textViewJnu;
    private TextView textViewHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        RelativeLayout relativeLayout = new RelativeLayout(this);
//        RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);                                            //设置布局中的控件居中显示
//        TextView textView = findViewById(R.id.text_vciew_hellow_world);                             //创建TextView控件
//        String helloString = getResources().getString(R.string.hello_android);
//        textView.setText(helloString);                                                              //设置TextView的文字内容
//        textView.setTextColor(Color.RED);                                                           //设置TextView的文字颜色
//        textView.setTextSize(36);                                                                   //设置TextView的文字大小
//        relativeLayout.addView(textView, params);                                                   //添加TextView对象和TextView的布局属性
//        setContentView(relativeLayout);                                                             //设置在Activity中显示RelativeLayout

        textViewJnu = findViewById(R.id.text_view_jnu);
        textViewHello = findViewById(R.id.text_view_hello);
        findViewById(R.id.button_change_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textJnu = textViewJnu.getText().toString();
                String textHello = textViewHello.getText().toString();
                textViewHello.setText(textJnu);
                textViewJnu.setText(textHello);

                Toast.makeText(MainActivity.this, "交换成功", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("交换成功")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}