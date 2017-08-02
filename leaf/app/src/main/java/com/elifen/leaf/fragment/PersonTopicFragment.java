package com.elifen.leaf.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.UIActivity.PostOfTopicActivity;
import com.elifen.leaf.adapter.TopicListviewAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class PersonTopicFragment extends Fragment {
    private String userIdStr;
    private ProgressBar progressBar;
    private TextView tv_tips;
    private ListView listView;
    private TopicListviewAdapter adapter;
    private List<TopicBean> topicList;
    private TopicBean topicBean;
    private int count;     //记录话题浏览次数
    private int t_id;      //记录话题的id
    public PersonTopicFragment() {
        // Required empty public constructor
    }
    public PersonTopicFragment(String userIdStr){
        this.userIdStr = userIdStr;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_person_topic, container, false);
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                topicBean= topicList.get(position);
                count = topicBean.getCount()+1;
                t_id = topicBean.getT_id();
                new updataCount().execute();   //更新浏览量并跳转
            }
        });
        new getMyTopicAsynTask().execute();     //访问数据得到数据
    }

    private void initView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listView = (ListView) view.findViewById(R.id.listView);
        tv_tips = (TextView) view.findViewById(R.id.tv_tips);
        topicList = new ArrayList<TopicBean>();
        adapter = new TopicListviewAdapter(getContext(),topicList);
        listView.setAdapter(adapter);
    }

    private class getMyTopicAsynTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("currentUserId",userIdStr));
            String s = netTool.Httpconnection("GetMyTopicServlet", param);
            return s;
        }
        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if(s.equals("InternetGG")){
                Toast.makeText(getContext(),"网络出错！",Toast.LENGTH_SHORT).show();
            } else if(!s.equals("null")) {
                adapter.clear();          //先将adapter里面的数据清空
                Gson gson = new Gson();
                topicList = gson.fromJson(s, new TypeToken<List<TopicBean>>(){}.getType());
                adapter.setData(topicList);
                adapter.notifyDataSetChanged();
            }else{
                //数据库中没有帖子的数据
                tv_tips.setVisibility(View.VISIBLE);
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
                Toast.makeText(getContext(), "网络出错", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(getActivity(), PostOfTopicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("topicBean", topicBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
