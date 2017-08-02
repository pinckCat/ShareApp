package com.elifen.leaf.UIActivity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.FavoriteAdapter;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.entity.PostPriseBean;
import com.elifen.leaf.net.NetTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MyfansActivity extends AppCompatActivity {
    private ImageView iv_back;
    private ProgressBar progressBar;
    private ListView lv_fans;
    private TextView tv_noContent;
    private List<PostPriseBean> fansList;
    private FavoriteAdapter adapter;
    private CurrentUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfans);
        initView();
        initListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void initView() {
        currentUser = CurrentUser.getInstance();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_noContent = (TextView) findViewById(R.id.tv_noContent);
        lv_fans = (ListView) findViewById(R.id.lv_fans);
        fansList = new ArrayList<PostPriseBean>();
        adapter = new FavoriteAdapter(this,fansList,currentUser.getUserId(),2);
        lv_fans.setAdapter(adapter);
        new getFansData().execute();
    }
    private class getFansData extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("idStr", String.valueOf(currentUser.getUserId())));
            String s = netTool.Httpconnection("GetFansServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if(!s.equals("null")){
                Gson gson = new Gson();
                fansList = gson.fromJson(s, new TypeToken<List<PostPriseBean>>(){}.getType());
                adapter.setData(fansList);
                adapter.notifyDataSetChanged();
            }else{
                tv_noContent.setVisibility(View.VISIBLE);
            }
        }
    }
}
