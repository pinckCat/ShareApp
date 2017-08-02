package com.elifen.leaf.UIActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.CurrentUser;

public class StartActivity extends AppCompatActivity {
    private CurrentUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_avtivity);
        currentUser = CurrentUser.getInstance();
        innitCurrentUser();          //初始化当前用户，得到当前用户信息
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(currentUser.isLogin())
                {
                    Intent intent = new Intent(StartActivity.this,LoginSuccessActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide2top);
                finish();
            }
        };
        handler.postDelayed(runnable, 3000);     //延迟3s执行Runabled对象
    }
    private void innitCurrentUser() {
        CurrentUser.getInstance().readFromFile(StartActivity.this);
    }
}
