package com.elifen.leaf.UIActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.myFragmentPagerAdapter;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.entity.PostBean;
import com.elifen.leaf.entity.PostCommentBean;
import com.elifen.leaf.fragment.PostCommentFragment;
import com.elifen.leaf.fragment.PostPriseFragment;
import com.elifen.leaf.net.NetTool;
import com.elifen.leaf.view.SimpleViewPagerIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostdetailActivity extends AppCompatActivity implements View.OnClickListener {
    private PostBean bean;
    private ImageView iv_back;
    private ImageView iv_head;
    private TextView tv_name;
    private ImageView iv_imageContent;
    private TextView tv_textContent;
    private EditText et_comment;
    private TextView tv_send;
    private List<PostCommentBean> postCommentLists = new ArrayList<PostCommentBean>();
    private CurrentUser currentUser;

    private String[] mTitles = new String[] { "评价", "点赞" };
    private List<Fragment> listfragment;
    private SimpleViewPagerIndicator mIndicator;    //指示器
    private ViewPager mViewPager;
    private PostCommentFragment fragment1;
    private PostPriseFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        Intent intent = getIntent();
        bean =(PostBean)intent.getSerializableExtra("PostBean");
        initView();
        initData();
        initListener();
    }

    private void initData() {
        mIndicator.setTitles(mTitles);
        listfragment = new ArrayList<Fragment>();
        fragment1 = new PostCommentFragment(bean.getP_id());
        fragment2 = new PostPriseFragment(bean.getP_id());
        listfragment.add(fragment1);
        listfragment.add(fragment2);
        FragmentManager fm = getSupportFragmentManager();
        myFragmentPagerAdapter mfpa=new myFragmentPagerAdapter(fm, listfragment); //new myFragmentPagerAdater记得带上两个参数
        mViewPager.setAdapter(mfpa);
        mViewPager.setCurrentItem(0); //设置默认显示的fragment
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = et_comment.getText().toString();
               if(!str.equals("")){
                   tv_send.setVisibility(View.VISIBLE);
               }
            }
        });
        tv_send.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        currentUser = CurrentUser.getInstance();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_head.setImageBitmap(strToBitmap(bean.getUserHeadStr()));
        tv_name = (TextView) findViewById(R.id.tv_userName);
        tv_name.setText(bean.getUserNameStr());
        iv_imageContent = (ImageView) findViewById(R.id.iv_imageContent);
       if(bean.getImageContent() != null){
           iv_imageContent.setImageBitmap(strToBitmap(bean.getImageContent()));
       }else{
           iv_imageContent.setVisibility(View.GONE);
       }
        tv_textContent = (TextView) findViewById(R.id.tv_textContent);
        tv_textContent.setText(bean.getTextContent());
        et_comment = (EditText) findViewById(R.id.et_comment);
        tv_send = (TextView) findViewById(R.id.tv_send);
        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
            case R.id.tv_send:
                new sendCommentAsynTask().execute();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //将图片字符串转换成Bitmap
    public Bitmap strToBitmap(String headString) {
        byte[] bytes = Base64.decode(headString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private class sendCommentAsynTask extends AsyncTask<Void,Void,String>{
        String c_content = "";
        String time= "";
        @Override
        protected void onPreExecute() {
            c_content = et_comment.getText().toString();
            if(et_comment.getText().toString().equals("")){
                Toast.makeText(PostdetailActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
            }else {
                //隐藏软件键盘
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                tv_send.setVisibility(View.GONE);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("userNameStr", currentUser.getUserName()));
            param.add(new BasicNameValuePair("p_idStr", String.valueOf(bean.getP_id())));
            param.add(new BasicNameValuePair("c_content", c_content));
            String s = netTool.Httpconnection("SendCommentServlet", param);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            time = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            int userId= currentUser.getUserId();
            String userHead = currentUser.getHeadString();
            String userName = currentUser.getUserName();
            PostCommentBean newBean = new PostCommentBean(userId,userHead,userName,time,c_content);
            if (s != null) {
                JSONObject jsonObject;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("Success")) {
                    fragment1.refreshData(newBean);
                    Toast.makeText(PostdetailActivity.this, "成功成功！", Toast.LENGTH_SHORT).show();
                    et_comment.setText("");
                } else if (result.equals("Fail")) {
                    Toast.makeText(PostdetailActivity.this, "评论失败！", Toast.LENGTH_SHORT).show();
                } else if (s == null) {
                    Toast.makeText(PostdetailActivity.this, "请求数据库出错！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
