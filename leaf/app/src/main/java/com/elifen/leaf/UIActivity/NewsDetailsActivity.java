package com.elifen.leaf.UIActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.elifen.leaf.R;

import java.util.ArrayList;

public class NewsDetailsActivity extends AppCompatActivity {
    private ImageView iv_back;
    private WebView web_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        initView();
        initData();
        initListener();
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

    private void initData() {
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        final ArrayList<String> data = bundle.getStringArrayList("data");
        Log.d("url", data.get(0));
        web_text.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web_text.loadUrl(data.get(1));
//        Glide.with(this)
//                .load(data.get(0)).error(R.mipmap.ic_launcher)
//                .fitCenter().into(ivImage);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        web_text = (WebView) findViewById(R.id.web_text);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
