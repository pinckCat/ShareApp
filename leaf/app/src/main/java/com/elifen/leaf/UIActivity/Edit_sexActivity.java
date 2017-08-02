package com.elifen.leaf.UIActivity;

import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Edit_sexActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private ImageView iv_back;
    private RadioGroup radioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private CurrentUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sex);
        initView();
        initListenner();
    }

    private void initListenner() {
        radioGroup.setOnCheckedChangeListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void initView() {
        currentUser = CurrentUser.getInstance();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupID);
        maleRadioButton = (RadioButton) findViewById(R.id.maleGroupID);
        femaleRadioButton = (RadioButton) findViewById(R.id.femaleGroupID);
        if (currentUser.getSex().equals("男")){
            maleRadioButton.setChecked(true);
        }
        else{
           femaleRadioButton.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.maleGroupID:
                 new setSexAsynTask().execute("男");
                break;
            case R.id.femaleGroupID:
                new setSexAsynTask().execute("女");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class setSexAsynTask extends AsyncTask<String,Void,String> {
        String sex;
        @Override
        protected String doInBackground(String... params) {
             sex = params[0];
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("userName", currentUser.getUserName()));
            param.add(new BasicNameValuePair("sex", sex));
            String s = netTool.Httpconnection("ChangeSexServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                JSONObject jsonObject = null;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(result.equals("Success")) {
                    Toast.makeText(Edit_sexActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    currentUser.setSex(sex);
                    currentUser.writeToFile(Edit_sexActivity.this);
                    onBackPressed();
                    finish();
                    //Toast.makeText(Person_informationActivity.this, currentUser.getHeadString(), Toast.LENGTH_SHORT).show();
                }
                else if(result.equals("Fail"))
                    Toast.makeText(Edit_sexActivity.this, "保存出错！", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Edit_sexActivity.this, "请求服务器出错！", Toast.LENGTH_SHORT).show();
            onBackPressed();
            finish();
             }
        }
    }
}
