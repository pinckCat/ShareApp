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
import com.elifen.leaf.adapter.PriseRecycleviewAdapter;
import com.elifen.leaf.entity.PostPriseBean;
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
public class PostPriseFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<PostPriseBean> priseBeanList;
    private PriseRecycleviewAdapter adapter;
    private int p_id;
    public PostPriseFragment() {
        // Required empty public constructor
    }
    public PostPriseFragment(int p_id){
        this.p_id = p_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_prise, container, false);
        mRecyclerView = (RecyclerView) view
                .findViewById(R.id.id_stickynavlayout_innerscrollview);
        priseBeanList =new ArrayList<PostPriseBean>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PriseRecycleviewAdapter(getActivity(),priseBeanList);
        mRecyclerView.setAdapter(adapter);

        new getPriseAsynTask().execute();
        return view;
    }

    private class getPriseAsynTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("p_idStr", String.valueOf(p_id)));
            String s = netTool.Httpconnection("GetPriseServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("null")){
                Gson gson = new Gson();
                priseBeanList = gson.fromJson(s, new TypeToken<List<PostPriseBean>>(){}.getType());
                adapter.setData(priseBeanList);
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(),"暂无点赞！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
