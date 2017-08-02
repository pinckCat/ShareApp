package com.elifen.leaf.UIActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class CreateTopicActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_save;
    private EditText et_topic;
    private CurrentUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
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
        et_topic = (EditText) findViewById(R.id.et_topic);
        currentUser =CurrentUser.getInstance();
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
                if(et_topic.getText().toString().equals("")){
                    Toast.makeText(this,"内容不能为空",Toast.LENGTH_SHORT).show();
                }else{
                     //连接服务器，插入数据
                    new createTopic().execute();
            }
                break;
        }
    }

    private class createTopic extends AsyncTask<Void,Void,String> {
        String title = et_topic.getText().toString();
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("createrIdStr", String.valueOf(currentUser.getUserId())));
            param.add(new BasicNameValuePair("title", title));
            String s = netTool.Httpconnection("CreateTopicServlet", param);
            return s;
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("InternetGG")){
                Toast.makeText(CreateTopicActivity.this,"网络出错",Toast.LENGTH_SHORT).show();
            }
            else if(!s.equals("")){
                JSONObject jsonObject ;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(result.equals("Success")) {
                    onBackPressed();
                    finish();
                }
            }
        }
    }
}
