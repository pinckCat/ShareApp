package com.elifen.leaf.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.RecycleViewAdapter;
import com.elifen.leaf.entity.PostCommentBean;
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
public class PostCommentFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<PostCommentBean> commentList;
    private RecycleViewAdapter adapter;
    private int p_id;
    public PostCommentFragment() {
        // Required empty public constructor
    }
   public PostCommentFragment(int p_id){
       this.p_id = p_id;
   }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_post_comment, container, false);
        mRecyclerView = (RecyclerView) view
                .findViewById(R.id.id_stickynavlayout_innerscrollview);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentList = new ArrayList<PostCommentBean>();
        adapter = new RecycleViewAdapter(getContext(),commentList);
        mRecyclerView.setAdapter(adapter);

        new getCommentAsynTask().execute();  //访问服务器，得到评论信息
        return view;
    }

    public void refreshData(PostCommentBean bean){
        commentList.add(bean);
        adapter.setData(commentList);
        adapter.notifyDataSetChanged();
    }
    private class getCommentAsynTask extends AsyncTask<Void, Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("p_idStr", String.valueOf(p_id)));
            String s = netTool.Httpconnection("GetCommentServlet", param);
            return s;
        }
        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("null")){
                Gson gson = new Gson();
                commentList = gson.fromJson(s, new TypeToken<List<PostCommentBean>>(){}.getType());
                adapter.clear();
                adapter.setData(commentList);
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(),"暂无评论！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
