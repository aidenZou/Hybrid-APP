package com.cong.mm;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromHtmlCall();
    }

    private void fromHtmlCall() {
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri != null) {
                //uri的参数
                String type = uri.getQueryParameter("type");
                String id = uri.getQueryParameter("id");
                if (!TextUtils.isEmpty(type)) {
                    if ("detail".equalsIgnoreCase(type)) {
                        gotoInfoActivity(type, id);
                    }
                } else {
                    //首页
                }
            }
        }
    }

    private void gotoInfoActivity(String type, String id) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void gotoShareActivity() {
        Intent intent = new Intent(this, ShareActivity.class);
        startActivity(intent);
    }

    public void shareClick(View v) {
        gotoShareActivity();
    }

}
