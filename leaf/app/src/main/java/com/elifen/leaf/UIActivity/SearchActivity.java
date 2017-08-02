package com.elifen.leaf.UIActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.PriseRecycleviewAdapter;
import com.elifen.leaf.adapter.TopicListviewAdapter;
import com.elifen.leaf.entity.PostPriseBean;
import com.elifen.leaf.entity.TopicBean;
import com.elifen.leaf.net.NetTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView cancel;
    private EditText et_search;

    private ListView topicListView;
    private RecyclerView userRecyclerView;
    private TopicListviewAdapter topicAdapter;   //话题适配器和TopicFragment中的话题共用Adapter
    private PriseRecycleviewAdapter userAdapter;  //用户适配器和帖子详情界面中的点赞人列表共用
    private TextView tv_userTips;
    private TextView tv_topicTips;
    private List<TopicBean> topicBeanList;
    private List<PostPriseBean> userBeanList;
    private TopicBean topicBean;
    private int count;     //记录话题浏览次数
    private int t_id;      //记录话题的id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();
        initView();
        initListenner();
    }

    private void initData() {
        topicBeanList = new ArrayList<TopicBean>();
        userBeanList = new ArrayList<PostPriseBean>();
    }
    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        cancel = (TextView) findViewById(R.id.cancel);
        tv_topicTips = (TextView) findViewById(R.id.tv_topicTips);
        tv_userTips = (TextView) findViewById(R.id.tv_userTips);

        userRecyclerView = (RecyclerView) findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new PriseRecycleviewAdapter(this,userBeanList);
        userRecyclerView.setAdapter(userAdapter);

        topicListView = (ListView) findViewById(R.id.topicList);
        topicAdapter = new TopicListviewAdapter(this,topicBeanList);
        topicListView.setAdapter(topicAdapter);
    }
    //监听搜索输入框内容变化
   class EditChangedListener implements TextWatcher{
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       }
       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {

       }
        /**
         *  编辑框的内容改变以后,用户没有继续输入时 的回调方法
         */
        @Override
       public void afterTextChanged(Editable s) {
            String string = et_search.getText().toString();
            if(!string.equals("")) {
                new searchUser().execute(string);
                new serchTopic().execute(string);
            }else{
                userAdapter.clear();
                topicAdapter.clear();
                userAdapter.notifyDataSetChanged();
                topicAdapter.notifyDataSetChanged();
                tv_userTips.setVisibility(View.GONE);
                tv_topicTips.setVisibility(View.GONE);
            }
       }
   }
    private void initListenner() {
        cancel.setOnClickListener(this);
        et_search.addTextChangedListener(new EditChangedListener());
        topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                topicBean = topicBeanList.get(position);
                count = topicBean.getCount()+1;
                t_id = topicBean.getT_id();
                new updataCount().execute();   //更新浏览量并跳转
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                onBackPressed();
                break;
        }
    }

    private class searchUser extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            String str = params[0];
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("str", str));
            String s = netTool.Httpconnection("SearchUserServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("InternetGG")) {
                Toast.makeText(SearchActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
            } else {
                if (!s.equals("null")) {
                    tv_userTips.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    userBeanList = gson.fromJson(s, new TypeToken<List<PostPriseBean>>(){}.getType());
                    userAdapter.clear();
                    userAdapter.setData(userBeanList);
                    userAdapter.notifyDataSetChanged();
                }else{
                    userAdapter.clear();
                    userAdapter.notifyDataSetChanged();
                    tv_userTips.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class serchTopic extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            String str = params[0];
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("str", str));
            String s = netTool.Httpconnection("SearchTopicServlet", param);
            return s;
        }
        @Override
        protected void onPostExecute(String s) {
            if (s.equals("InternetGG")) {
                Toast.makeText(SearchActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
            } else {
                if (!s.equals("null")) {
                    tv_topicTips.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    topicBeanList = gson.fromJson(s, new TypeToken<List<TopicBean>>(){}.getType());
                    topicAdapter.clear();
                    topicAdapter.setData(topicBeanList);
                    topicAdapter.notifyDataSetChanged();
                }
                else{
                    topicAdapter.clear();
                    topicAdapter.notifyDataSetChanged();
                    tv_topicTips.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private class updataCount extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("countStr", String.valueOf(count)));
            param.add(new BasicNameValuePair("t_idStr", String.valueOf(t_id)));
            String s = netTool.Httpconnection("UpdateCountServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("InternetGG")) {
                Toast.makeText(SearchActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObject;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("Success")) {
                    topicBean.setCount(count);
                    Intent intent = new Intent(SearchActivity.this, PostOfTopicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("topicBean", topicBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    topicAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
