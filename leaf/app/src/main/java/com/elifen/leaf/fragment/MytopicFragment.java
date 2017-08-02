package com.elifen.leaf.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.elifen.leaf.R;
import com.elifen.leaf.UIActivity.PostOfTopicActivity;
import com.elifen.leaf.adapter.TopicListviewAdapter;
import com.elifen.leaf.entity.CurrentUser;
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
 * 我的话题
 */
public class MytopicFragment extends Fragment {
    private ProgressBar progressBar;
    private SwipeMenuListView listView;
    private TextView tv_tips;
    private List<TopicBean> mTopic;
    private TopicListviewAdapter adapter;
    private CurrentUser currentUser;
    private TopicBean topicBean;
    private int count;     //记录话题浏览次数
    private int t_id;      //记录话题的id
    public MytopicFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mytopic, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                topicBean = mTopic.get(position);
                t_id = topicBean.getT_id();
                switch (index){
                    case 0:
                        //删除
                        new deleteAsynTask().execute();
                        break;
                }
                return false;
            }
        });
        //item点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                topicBean = mTopic.get(position);
                count = topicBean.getCount()+1;
                t_id = topicBean.getT_id();
                new updataCount().execute();   //更新浏览量并跳转
            }
        });
        new getMyTopicAsynTask().execute();     //访问数据得到数据
    }

    private void initData() {
       currentUser = CurrentUser.getInstance();
        mTopic = new ArrayList<TopicBean>();
        adapter = new TopicListviewAdapter(getContext(),mTopic);
        listView.setAdapter(adapter);
        //增加侧滑的item
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                       SwipeMenuItem deleteItem = new SwipeMenuItem(
                              getContext());
                       // set item background
                       deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                               0x3F, 0x25)));
                       // set item width
                       deleteItem.setWidth(dp2px(60));
                       // set a icon
                       deleteItem.setIcon(R.drawable.ic_delete);
                       // add to menu
                       menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
    }

    private void initView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tv_tips = (TextView) view.findViewById(R.id.tv_tips);
        listView = (SwipeMenuListView) view.findViewById(R.id.listView);
    }

    private class getMyTopicAsynTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("currentUserId",String.valueOf(currentUser.getUserId())));
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
                mTopic = gson.fromJson(s, new TypeToken<List<TopicBean>>(){}.getType());
                adapter.setData(mTopic);
                adapter.notifyDataSetChanged();
            }else{
                //数据库中没有帖子的数据
                tv_tips.setVisibility(View.VISIBLE);
            }
        }
    }
    //px转dp
    private int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getResources().getDisplayMetrics());
    }

    private class updataCount extends AsyncTask<Void,Integer,String> {
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
            JSONObject jsonObject ;
            String result = null;
            try {
                jsonObject = new JSONObject(s);
                result = jsonObject.getString("Status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result.equals("Success")) {
                topicBean.setCount(count);
                Intent intent = new Intent(getActivity(), PostOfTopicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("topicBean",topicBean);
                intent.putExtras(bundle);
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class deleteAsynTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("t_idStr", String.valueOf(t_id)));
            String s = netTool.Httpconnection("DeleteMyTopicServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject jsonObject ;
            String result = null;
            try {
                jsonObject = new JSONObject(s);
                result = jsonObject.getString("Status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result.equals("Success")) {
                mTopic.remove(topicBean);
                adapter.notifyDataSetChanged();
            }
        }

    }
}
