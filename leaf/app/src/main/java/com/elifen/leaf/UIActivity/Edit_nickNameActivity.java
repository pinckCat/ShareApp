package com.elifen.leaf.UIActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

@SuppressWarnings("ALL")
public class Edit_nickNameActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_save;
    private EditText et_nickName;
    private CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nick_name);
        initView();
        initListenner();
    }

    private void initListenner() {
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        et_nickName.addTextChangedListener(textWatcher);

    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_nickName = (EditText) findViewById(R.id.et_name);
        currentUser = CurrentUser.getInstance();
        et_nickName.setText(currentUser.getUserName());
        et_nickName.setSelection(currentUser.getUserName().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
            case R.id.tv_save:
                new saveNicknameAsynTask().execute();   //保存至服务器
                break;
        }
    }

    //输入框内容监听,在输入框内容没有变之前保存按钮不可点击
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() >= 2) {
                tv_save.setClickable(true);
                tv_save.setTextColor(0xff232629);
            } else {
                tv_save.setTextColor(0xffA8A8A8);
                tv_save.setClickable(false);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class saveNicknameAsynTask extends AsyncTask<Void, Void, String> {
        String name;

        @Override
        protected void onPreExecute() {
            name = et_nickName.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("userName", currentUser.getUserName()));
            param.add(new BasicNameValuePair("nickName", name));
            String s = netTool.Httpconnection("ChangeNicknameServlet", param);
            return s;
        }
        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                JSONObject jsonObject = null;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("Success")) {
                    Toast.makeText(Edit_nickNameActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    currentUser.setUserName(name);
                    currentUser.writeToFile(Edit_nickNameActivity.this);
                    //onBackPressed();
                    Intent intent = getIntent();
                    setResult(4,intent);
                    finish();
                    //Toast.makeText(Person_informationActivity.this, currentUser.getHeadString(), Toast.LENGTH_SHORT).show();
                } else if (result.equals("Fail"))
                    Toast.makeText(Edit_nickNameActivity.this, "保存出错！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Edit_nickNameActivity.this, "请求服务器出错！", Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();
            }

        }
    }
}

