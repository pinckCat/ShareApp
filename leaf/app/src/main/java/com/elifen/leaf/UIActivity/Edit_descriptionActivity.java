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

public class Edit_descriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_textNum;
    private EditText et_description;
    private TextView tv_save;
    private CurrentUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);
        initView();
        initListenner();
    }

    private void initListenner() {
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);

        et_description.addTextChangedListener(textWatcher);

    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_description = (EditText) findViewById(R.id.et_description);
        tv_textNum = (TextView) findViewById(R.id.tv_textNum);
        tv_save = (TextView) findViewById(R.id.tv_save);
        currentUser = CurrentUser.getInstance();
        et_description.setText(currentUser.getDescription());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int num = s.length();
            num = 30 - num;
            tv_textNum.setText(""+num);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
            case R.id.tv_save:
                new descriptionAsynTask().execute();
                break;
        }
    }

    private class descriptionAsynTask extends AsyncTask<Void,Void,String> {
        String description;
        @Override
        protected void onPreExecute() {
            description = et_description.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("userName", currentUser.getUserName()));
            param.add(new BasicNameValuePair("description", description));
            String s = netTool.Httpconnection("ChangeDescriptionServlet", param);
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
                    Toast.makeText(Edit_descriptionActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    currentUser.setDescription(description);
                    currentUser.writeToFile(Edit_descriptionActivity.this);
                    //onBackPressed();
                    Intent intent = getIntent();
                    setResult(6,intent);
                    finish();
                    //Toast.makeText(Person_informationActivity.this, currentUser.getHeadString(), Toast.LENGTH_SHORT).show();
                } else if (result.equals("Fail"))
                    Toast.makeText(Edit_descriptionActivity.this, "保存出错！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Edit_descriptionActivity.this, "请求服务器出错！", Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();
            }

        }
    }
}
