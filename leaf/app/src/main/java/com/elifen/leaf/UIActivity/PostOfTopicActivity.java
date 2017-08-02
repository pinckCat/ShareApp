package com.elifen.leaf.UIActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.TopicPostListviewAdapter;
import com.elifen.leaf.entity.PostBean;
import com.elifen.leaf.entity.TopicBean;
import com.elifen.leaf.net.NetTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PostOfTopicActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_tips;
    private ListView listView;
    private ProgressBar progressBar;
    private ImageView iv_add;
    private TopicBean bean;
    private List<PostBean> postLists;
    private TopicPostListviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_of_topic);
        initData();
        initView();
        initListener();

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        bean = (TopicBean) bundle.getSerializable("topicBean");
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }

    private void initView() {
        postLists = new ArrayList<PostBean>();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(bean.getTitle());
        iv_add = (ImageView) findViewById(R.id.iv_add);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new TopicPostListviewAdapter(this,postLists);
        listView.setAdapter(adapter);
        new getPostAsynTask().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new getPostAsynTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
            case R.id.iv_add:
                Intent intent = new Intent(this,CreateTopicPostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("topicBean",bean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private class getPostAsynTask extends AsyncTask<Void,Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("t_idStr",String.valueOf(bean.getT_id())));
            String s = netTool.Httpconnection("GetTopicPostServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("InternetGG")){
                Toast.makeText(PostOfTopicActivity.this,"网络出错！",Toast.LENGTH_SHORT).show();
            }
           else if(!s.equals("null")) {
                progressBar.setVisibility(View.GONE);
                tv_tips.setVisibility(View.GONE);
                adapter.clear();          //先将adapter里面的数据清空
                Gson gson = new Gson();
                postLists = gson.fromJson(s, new TypeToken<List<PostBean>>(){}.getType());
                adapter.setData(postLists);
                adapter.notifyDataSetChanged();
            }else{
                progressBar.setVisibility(View.GONE);
                tv_tips.setVisibility(View.VISIBLE);
            }
        }
    }
}
