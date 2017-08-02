package com.elifen.leaf.UIActivity;

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

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private ProgressBar progressBar;
    private ListView lv_favorite;
    private TextView tv_noContent;
    private List<PostPriseBean> favoriteList;
    private FavoriteAdapter adapter;
    private CurrentUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initView();
        initListener();
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        currentUser = CurrentUser.getInstance();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_noContent = (TextView) findViewById(R.id.tv_noContent);
        lv_favorite = (ListView) findViewById(R.id.lv_favorite);
        favoriteList = new ArrayList<PostPriseBean>();
        adapter = new FavoriteAdapter(this,favoriteList,currentUser.getUserId(),1);
        lv_favorite.setAdapter(adapter);
        new getData().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
        }
    }

    private class getData extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("idStr", String.valueOf(currentUser.getUserId())));
            String s = netTool.Httpconnection("GetFavoritesServlet", param);
            return s;
        }
        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if(s.equals("InternetGG")){
                Toast.makeText(FavoriteActivity.this,"网络出错！",Toast.LENGTH_SHORT).show();
            }else if(!s.equals("null")){
                Gson gson = new Gson();
                favoriteList = gson.fromJson(s, new TypeToken<List<PostPriseBean>>(){}.getType());
                adapter.setData(favoriteList);
                adapter.notifyDataSetChanged();
            }else{
                 tv_noContent.setVisibility(View.VISIBLE);
            }
        }
    }
}
