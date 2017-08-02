package com.elifen.leaf.UIActivity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.net.NetTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChangePassActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_save;
    private EditText et_oldPass;
    private EditText et_newPass;
    private EditText et_checkNewPass;
    private String oldPass;
    private String newPass;
    private String checkPass;
    private CurrentUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        initView();
        initListener();
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_oldPass = (EditText) findViewById(R.id.et_oldPass);
        et_newPass = (EditText) findViewById(R.id.et_newPass);
        et_checkNewPass = (EditText) findViewById(R.id.et_checkNewPass);
        currentUser = CurrentUser.getInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
            case R.id.tv_save:
                oldPass = et_oldPass.getText().toString();
                newPass = et_newPass.getText().toString();
                checkPass = et_checkNewPass.getText().toString();
                if(oldPass.equals("")||newPass.equals("")||checkPass.equals("")){
                    Toast.makeText(ChangePassActivity.this,"请确定信息输入完整",Toast.LENGTH_SHORT).show();
                }
                else if(!newPass.equals(checkPass)){
                    Toast.makeText(ChangePassActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                }else {
                    new changePass().execute();       //访问服务器跟新数据
                }
                break;
        }
    }

    private class changePass extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("userName",currentUser.getUserName()));
            param.add(new BasicNameValuePair("oldPass", oldPass));
            param.add(new BasicNameValuePair("newPass", newPass));
            String s = netTool.Httpconnection("ChangePassServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("InternetGG")){
                Toast.makeText(ChangePassActivity.this,"网络出错",Toast.LENGTH_SHORT).show();
            }else {
                JSONObject jsonObject;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(result.equals("Success")){
                    Toast.makeText(ChangePassActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }else if(result.equals("WrongOldPass")){
                    Toast.makeText(ChangePassActivity.this,"原密码不正确",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChangePassActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
