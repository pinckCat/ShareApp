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
import com.elifen.leaf.UIActivity.CreateTopicActivity;
import com.elifen.leaf.UIActivity.PostOfTopicActivity;
import com.elifen.leaf.UIActivity.SearchActivity;
import com.elifen.leaf.adapter.TopicListviewAdapter;
import com.elifen.leaf.entity.TopicBean;
import com.elifen.leaf.net.NetTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.recker.flybanner.FlyBanner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TopicFragment extends Fragment implements View.OnClickListener {
    private FlyBanner flyBanner;
    private TextView tv_search;
    private TextView tv_createTopic;
    private ProgressBar progressBar;
    private ListView lv_topic;
    private TopicListviewAdapter adapter;
    private List<TopicBean> topicList;
    private TopicBean topicBean;
    private int count;     //记录话题浏览次数
    private int t_id;      //记录话题的id
    public TopicFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new getTopicAsynTask().execute();
    }

    private void initView(View view) {
        flyBanner = (FlyBanner) view.findViewById(R.id.banner);
        tv_search = (TextView) view.findViewById(R.id.tv_search);
        tv_createTopic = (TextView) view.findViewById(R.id.tv_createTopic);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        lv_topic = (ListView) view.findViewById(R.id.lv_topic);
        topicList = new ArrayList<TopicBean>();
        adapter = new TopicListviewAdapter(getContext(),topicList);
        lv_topic.setAdapter(adapter);
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.image_1);
        images.add(R.drawable.image_2);
        images.add(R.drawable.image_3);
        images.add(R.drawable.image_4);
        flyBanner.setImages(images);
        initListenner();
        new getTopicAsynTask().execute();
    }

    private void initListenner() {
        tv_search.setOnClickListener(this);
        flyBanner.setOnItemClickListener(new FlyBanner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(),"点击了第："+position+"张照片",Toast.LENGTH_SHORT).show();
            }
        });
        tv_createTopic.setOnClickListener(this);
        lv_topic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                topicBean= topicList.get(position);
                count = topicBean.getCount()+1;
                t_id = topicBean.getT_id();
                new updataCount().execute();   //更新浏览量并跳转
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_createTopic:
                 Intent intent1 = new Intent(getActivity(), CreateTopicActivity.class);
                 startActivity(intent1);
                break;
        }
    }

    private class getTopicAsynTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("textContent", ""));
            String s = netTool.Httpconnection("GetTopicServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if (s.equals("InternetGG")) {
                Toast.makeText(getContext(), "网络出错", Toast.LENGTH_SHORT).show();
            } else {
                if (!s.equals("null")) {
                    adapter.clear();
                    Gson gson = new Gson();
                    topicList = gson.fromJson(s, new TypeToken<List<TopicBean>>() {
                    }.getType());
                    adapter.setData(topicList);
                    adapter.notifyDataSetChanged();
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

