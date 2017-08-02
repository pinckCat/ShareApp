package com.elifen.leaf.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.PostListviewAdapter;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.entity.PostBean;
import com.elifen.leaf.net.NetTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyprisedFragment extends Fragment {
    private ProgressBar progressBar;
    private ListView listView;
    private TextView tv_tips;
    private PostListviewAdapter adapter;
    private List<PostBean> mPostBean;
    private CurrentUser currentUser;
    public MyprisedFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myprised, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        currentUser = CurrentUser.getInstance();
        mPostBean = new ArrayList<PostBean>();
        adapter = new PostListviewAdapter(getContext(),mPostBean,currentUser);
        listView.setAdapter(adapter);
        new getMyPrised().execute();
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tv_tips = (TextView) view.findViewById(R.id.tv_tips);
    }

    private class getMyPrised extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("currentUserId",String.valueOf(currentUser.getUserId())));
            String s = netTool.Httpconnection("GetMyPrisedServlet", param);
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
                mPostBean = gson.fromJson(s, new TypeToken<List<PostBean>>(){}.getType());
                adapter.setData(mPostBean);
                adapter.notifyDataSetChanged();
            }else{
                //数据库中没有帖子的数据
                tv_tips.setVisibility(View.VISIBLE);
            }
        }
    }
}
