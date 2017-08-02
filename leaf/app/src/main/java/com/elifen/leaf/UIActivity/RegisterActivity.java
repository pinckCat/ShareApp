package com.elifen.leaf.UIActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.net.NetTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG="RegisterActivity";
    private Button btn_register;
    private ImageButton btn_back;
    private EditText edt_phoneNum;
    private EditText edt_setAccount;
    private EditText edt_setPass;
    private AlertDialog dialog = null;
    //手机号码
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListenner();

    }

    private void initListenner() {
        btn_register.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        edt_phoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    phone = edt_phoneNum.getText().toString();
                    if(!isMobileNO(phone)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("请输入正确的手机号码");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                    }
                }
            }
        });
    }

    private void initView() {
        btn_register = (Button) findViewById(R.id.register);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        edt_phoneNum = (EditText) findViewById(R.id.edt_phone);
        edt_setAccount = (EditText) findViewById(R.id.edt_setAccount);
        edt_setPass = (EditText) findViewById(R.id.edt_setPass);
    }

    @Override
    public void onClick(View v) {
         switch(v.getId()) {
             case R.id.register:
                 regist();
                 break;
             case R.id.btn_back:
                 onBackPressed();
                 finish();
                 break;
                 }
    }

    private void regist() {
        if (edt_phoneNum.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this,"手机号码不能为空！", Toast.LENGTH_SHORT).show();
        }
        else if(edt_setAccount.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this,"昵称不能为空！", Toast.LENGTH_SHORT).show();
        }
        else if(edt_setPass.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this,"密码不能为空！", Toast.LENGTH_SHORT).show();
        }
        else{
                new RegisterAsyncTask().execute();
        }
    }


    private boolean isMobileNO(String phone) {
          /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.matches(telRegex);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    private class RegisterAsyncTask extends AsyncTask<Void,Void,String>{
        String phoneNum;
        String userName;
        String userPass;
        @Override
        protected void onPreExecute() {
            phoneNum = edt_phoneNum.getText().toString();
            userName = edt_setAccount.getText().toString();
            userPass = edt_setPass.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("username", userName));
            param.add(new BasicNameValuePair("password", userPass));
            param.add(new BasicNameValuePair("phoneNum", phoneNum));
            String s = netTool.Httpconnection("RegisterServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String result) {
                if (dialog != null)
                    dialog.cancel();
                if (result == null)
                    return;
                if (result.equals("InternetGG")) {
                    Toast.makeText(RegisterActivity.this, "网络或服务器异常，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("Status").equals("Username has been registered"))
                    Toast.makeText(RegisterActivity.this, "此用户名已被注册！", Toast.LENGTH_SHORT).show();
                else if (jsonObject.getString("Status").equals("Success")) {
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide2left);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

