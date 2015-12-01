package com.cong.mm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * 参数：
 * type：页面类型[list：列表页；detail：详情页;…]
 * id：标识（详情 id）
 * 通过html调用本地app
 * Created by cong on 15/12/1.
 */
public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        if (intent != null) {
            String type = intent.getStringExtra("type");
            String id = intent.getStringExtra("id");
            TextView textView = (TextView) findViewById(R.id.text_view);
            if (textView != null)
                textView.setText(type + id);
        }
    }
}
