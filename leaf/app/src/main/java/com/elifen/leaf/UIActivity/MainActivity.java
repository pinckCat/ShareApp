package com.elifen.leaf.UIActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "MainActivity";
    private Button register;
    private Button login;
    private ImageButton wechat_login;
    private ImageButton blog_login;
    private ImageButton qq_login;
    private EditText account;
    private EditText password;
    private TextView getPass;
    private AlertDialog dialog = null;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListenner();
    }

    private void initListenner() {
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        wechat_login.setOnClickListener(this);
        blog_login.setOnClickListener(this);
        qq_login.setOnClickListener(this);
        getPass.setOnClickListener(this);
    }

    private void initView() {
        register = (Button) findViewById(R.id.btn_register);
        login = (Button) findViewById(R.id.login_btn);
        wechat_login = (ImageButton) findViewById(R.id.btn_wechat);
        blog_login = (ImageButton) findViewById(R.id.btn_blog);
        qq_login = (ImageButton) findViewById(R.id.btn_qq);
        account = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.userPass);
        getPass = (TextView) findViewById(R.id.getPass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        register.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn:
                login();
                break;
            case R.id.btn_wechat:
                break;
            case R.id.btn_blog:
                break;
            case R.id.btn_qq:
                break;
            case R.id.getPass:
                break;
        }
    }

    private void login() {
        if (account.getText().toString().equals(""))
            Toast.makeText(MainActivity.this, getResources().getString(R.string.LRActivity_UserNameNull), Toast.LENGTH_SHORT).show();
        else if (password.getText().toString().equals(""))
            Toast.makeText(MainActivity.this, getResources().getString(R.string.LRActivity_PasswordNull), Toast.LENGTH_SHORT).show();
        else {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            new LoginAsyncTask().execute();
        }
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, String> {
        private String nameStr;
        private String passwordStr;

        @Override
        protected void onPreExecute() {
            nameStr = account.getText().toString();
            passwordStr = password.getText().toString();
        }
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("username", nameStr));
            param.add(new BasicNameValuePair("password", passwordStr));
            String s = netTool.Httpconnection("LoginServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            if (result == null)
                return;
            if (result.equals("InternetGG")) {
                Toast.makeText(MainActivity.this, "网络或服务器异常，请稍后再试", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("Status").equals("Please register your account first"))
                    Toast.makeText(MainActivity.this, "未找到此用户，请先注册！", Toast.LENGTH_SHORT).show();
                else if (jsonObject.getString("Status").equals("Wrong password"))
                    Toast.makeText(MainActivity.this, "用户名密码不匹配，请检查用户名和密码！", Toast.LENGTH_SHORT).show();
                else if (jsonObject.getString("Status").equals("Success")) {
                    Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, LoginSuccessActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide2left);
                    CurrentUser.getInstance().UserLogin(jsonObject);
                    CurrentUser.getInstance().writeToFile(MainActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
