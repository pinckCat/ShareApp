package com.elifen.leaf.UIActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.CurrentUser;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private Button btn_exit;
    private RelativeLayout item_changePass;
    private RelativeLayout item_version;
    private RelativeLayout item_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListenner();
    }

    private void initListenner() {
        iv_back.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        item_changePass.setOnClickListener(this);
        item_version.setOnClickListener(this);
        item_about.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        item_changePass = (RelativeLayout) findViewById(R.id.item_changePass);
        item_version = (RelativeLayout) findViewById(R.id.item_version);
        item_about = (RelativeLayout) findViewById(R.id.item_about);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
            case R.id.btn_exit:
                CurrentUser.getInstance().UserLogout(this);
                CurrentUser.getInstance().clearFile(this);
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.item_changePass:
                Intent intent1 = new Intent(SettingActivity.this,ChangePassActivity.class);
                startActivity(intent1);
                break;
            case R.id.item_version:
                Toast.makeText(SettingActivity.this,"已经是最新版本",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_about:
                Intent intent2 = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intent2);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
