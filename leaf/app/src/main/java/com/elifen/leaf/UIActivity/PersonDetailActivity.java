package com.elifen.leaf.UIActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.myFragmentPagerAdapter;
import com.elifen.leaf.entity.PostPriseBean;
import com.elifen.leaf.fragment.PersonPostFragment;
import com.elifen.leaf.fragment.PersonTopicFragment;
import com.elifen.leaf.view.CoustomImageView;
import com.elifen.leaf.view.SimpleViewPagerIndicator;

import java.util.ArrayList;

public class PersonDetailActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView tv_titleName;
    private CoustomImageView image_head;
    private TextView tv_userame;
    private TextView tv_userId;
    private SimpleViewPagerIndicator indicator;
    private ViewPager viewPager;
    private PersonPostFragment postFragment;
    private PersonTopicFragment topicFragment;
    private String[] mTitles = new String[] { "话题", "帖子" };
    private ArrayList<Fragment> fragments;
    private FragmentManager fm;
    private String userIdStr;
    private PostPriseBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        initView();
        initData();
        initListener();
    }
    private void initData() {

        fm = getSupportFragmentManager();
        fragments = new ArrayList<Fragment>();
        postFragment = new PersonPostFragment(userIdStr);
        topicFragment = new PersonTopicFragment(userIdStr);
        fragments.add(topicFragment);
        fragments.add(postFragment);
        myFragmentPagerAdapter mfpa=new myFragmentPagerAdapter(fm, fragments); //new myFragmentPagerAdater记得带上两个参数
        viewPager.setAdapter(mfpa);
        viewPager.setCurrentItem(0); //设置当前页是第一页
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.scroll(position, positionOffset);
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
        Intent intent = getIntent();
        bean = (PostPriseBean) intent.getSerializableExtra("priseBean");
        userIdStr = bean.getUserId()+"";
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_titleName = (TextView) findViewById(R.id.tv_titelName);
        tv_titleName.setText(bean.getUserName());
        image_head = (CoustomImageView) findViewById(R.id.image_head);
        image_head.setImageBitmap(strToBitmap(bean.getHeadName()));
        tv_userame = (TextView) findViewById(R.id.tv_userName);
        tv_userame.setText(bean.getUserName());
        tv_userId = (TextView) findViewById(R.id.tv_userId);
        tv_userId.setText("ID:"+bean.getUserId());
        indicator = (SimpleViewPagerIndicator) findViewById(R.id.indicator);
        indicator.setTitles(mTitles);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
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

}
