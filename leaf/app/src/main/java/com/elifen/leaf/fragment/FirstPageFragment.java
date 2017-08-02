package com.elifen.leaf.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.UIActivity.CreatePostActivity;
import com.elifen.leaf.UIActivity.SearchActivity;
import com.elifen.leaf.adapter.PostListviewAdapter;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.entity.PostBean;
import com.elifen.leaf.net.NetTool;
import com.elifen.leaf.view.CoustomListview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class FirstPageFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_add;
    private ImageView iv_search;
    private TextView tv_tips;
    private CoustomListview coustomListview;
    private PostListviewAdapter adapter;                         //listView适配器
    private List<PostBean> postLists = new ArrayList<PostBean>();      //用于存储服务器返回的帖子信息
    private int refreshNum;
    private CurrentUser currentUser;
    public FirstPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_first_page,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_add = (ImageView) view.findViewById(R.id.iv_add);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        tv_tips = (TextView) view.findViewById(R.id.tv_tips);
        coustomListview = (CoustomListview) view.findViewById(R.id.cl_content);
        refreshNum = 0;   //设置刷新数据初始值
        currentUser =CurrentUser.getInstance();
        initListenner();
    }
    private void initListenner() {
        iv_add.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        coustomListview.setOnRefreshListener(new CoustomListview.OnRefreshListener() {   //设置刷新监听
            @Override
            public void onRefresh() {
                new getPostDataAsynTask().execute(refreshNum);
            }
        });
        coustomListview.setOnGetMoreListener(new CoustomListview.OnGetMoreListener() {   //设置加载更多监听
            @Override
            public void onGetMore() {
                new getMoreAsynTask().execute();
            }
        });
        adapter = new PostListviewAdapter(getContext(),postLists,currentUser);
        coustomListview.setAdapter(adapter);  //为listveiw加载自定义的适配器
        coustomListview.performRefresh();      //首次刷新，调用onRefresh方法
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add:
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_search:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                break;
        }
    }

    private class getPostDataAsynTask extends AsyncTask<Integer,Void,String>{
        private int num;
        @Override
        protected String doInBackground(Integer... params) {
            num = params[0];
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("postNum", String.valueOf(0)));
            param.add(new BasicNameValuePair("currentUserId",String.valueOf(currentUser.getUserId())));
            String s = netTool.Httpconnection("GetPostDataServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("InternetGG")){
                Toast.makeText(getContext(),"网络出错！",Toast.LENGTH_SHORT).show();
                coustomListview.refreshComplete();
            } else if(!s.equals("null")) {
                adapter.clear();          //先将adapter里面的数据清空
                Gson gson = new Gson();
                postLists = gson.fromJson(s, new TypeToken<List<PostBean>>() {
                }.getType());
                adapter.setData(postLists);
                adapter.notifyDataSetChanged();
                coustomListview.refreshComplete();
                coustomListview.getMoreComplete();
            }else{
                //数据库中没有帖子的数据
                tv_tips.setVisibility(View.VISIBLE);
            }
        }
    }

    private class getMoreAsynTask extends AsyncTask<Void,Void,String> {
        int last_id;
        @Override
        protected void onPreExecute() {
            last_id = postLists.get(postLists.size()-1).getP_id();  //得到已经刷新到帖子的最后一个帖子的id
        }
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("postNum", String.valueOf(last_id)));
            param.add(new BasicNameValuePair("currentUserId",String.valueOf(currentUser.getUserId())));
            String s = netTool.Httpconnection("GetPostDataServlet", param);
            return s;
        }
        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("null")) {
                Gson gson = new Gson();
                List<PostBean> newPost = gson.fromJson(s, new TypeToken<List<PostBean>>(){}.getType());
                for(int i=0;i<newPost.size();i++){
                    postLists.add(newPost.get(i));
                }

                adapter.setData(postLists);
                adapter.notifyDataSetChanged();
                coustomListview.refreshComplete();
                coustomListview.getMoreComplete();
            }else{
                //没有更多的数据了
                Toast.makeText(getActivity(),"没有更多的数据了",Toast.LENGTH_LONG).show();
                coustomListview.getMoreComplete();
            }
        }
    }
}
