package com.elifen.leaf.UIActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elifen.leaf.R;
import com.elifen.leaf.fragment.FindFragment;
import com.elifen.leaf.fragment.FirstPageFragment;
import com.elifen.leaf.fragment.MyInfoFragment;
import com.elifen.leaf.fragment.TopicFragment;

public class LoginSuccessActivity extends FragmentActivity implements View.OnClickListener{

    //底部四个LinearLayout
    private LinearLayout ll_home;
    private LinearLayout ll_topic;
    private LinearLayout ll_find;
    private LinearLayout ll_myInfo;
    //底部四个ImageView
    private ImageView iv_home,iv_topic;
    private ImageView iv_find,iv_myInfo;
    //底部四个文本
    private TextView tv_home,tv_topic;
    private TextView tv_find,tv_myInfo;
    //四个Fragment
    private Fragment homeFragment,topicFragment;
    private Fragment findFragment,myInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        //初始化控件
        initView();
        //设置监听器
        initEvent();
        //初始化并设置当前fragmemt
        initFragment(0);
    }

    private void initFragment(int i) {
        //得到一个FragmentManager对象，用来管理多个Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (i){
            case 0:
                if(homeFragment == null){
                    homeFragment = new FirstPageFragment();
                    transaction.add(R.id.homePage_content, homeFragment);
                }else{
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                if(topicFragment == null){
                    topicFragment = new TopicFragment();
                    transaction.add(R.id.homePage_content, topicFragment);
                }else{
                    transaction.show(topicFragment);
                }
                break;
            case 2:
                if(findFragment == null){
                    findFragment = new FindFragment();
                    transaction.add(R.id.homePage_content, findFragment);
                }else{
                    transaction.show(findFragment);
                }
                break;
            case 3:
                if(myInfoFragment == null){
                    myInfoFragment = new MyInfoFragment();
                    transaction.add(R.id.homePage_content, myInfoFragment);
                }else{
                    transaction.show(myInfoFragment);
                }
                break;
        }
        transaction.commit();            //提交
    }

    //隐藏frgment
    private void hideFragment(FragmentTransaction transaction) {
        if(homeFragment != null){
            transaction.hide(homeFragment);
        }
        if(topicFragment != null){
            transaction.hide(topicFragment);
        }
        if(findFragment != null){
            transaction.hide(findFragment);
        }
        if(myInfoFragment != null){
            transaction.hide(myInfoFragment);
        }
    }

    private void initEvent() {
        ll_home.setOnClickListener(this);
        ll_topic.setOnClickListener(this);
        ll_find.setOnClickListener(this);
        ll_myInfo.setOnClickListener(this);
    }

    private void initView() {
        ll_home = (LinearLayout) findViewById(R.id.home);
        ll_topic = (LinearLayout) findViewById(R.id.topic);
        ll_find = (LinearLayout) findViewById(R.id.find);
        ll_myInfo = (LinearLayout) findViewById(R.id.myInfo);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_topic = (ImageView) findViewById(R.id.iv_topic);
        iv_find = (ImageView) findViewById(R.id.iv_find);
        iv_myInfo = (ImageView) findViewById(R.id.iv_myInof);

        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_topic = (TextView) findViewById(R.id.tv_topic);
        tv_find = (TextView) findViewById(R.id.tv_find);
        tv_myInfo = (TextView) findViewById(R.id.tv_myInfo);

    }

    @Override
    public void onClick(View v) {
        //每次点击将底部图片变成原色
        restartBotton();
        switch(v.getId()){
             case R.id.home:
                 iv_home.setImageResource(R.drawable.home_pressed);
                 tv_home.setTextColor(0xff686464);
                 initFragment(0);
                 break;
             case R.id.topic:
                 iv_topic.setImageResource(R.drawable.topic_pressed);
                 tv_topic.setTextColor(0xff686464);
                 initFragment(1);
                 break;
             case R.id.find:
                 iv_find.setImageResource(R.drawable.find_pressed);
                 tv_find.setTextColor(0xff686464);
                 initFragment(2);
                 break;
             case R.id.myInfo:
                 iv_myInfo.setImageResource(R.drawable.myself_pressed);
                 tv_myInfo.setTextColor(0xff686464);
                 initFragment(3);
                 break;
             default:
                 break;
         }
    }

    private void restartBotton() {

        //底部imageView变成原来的颜色
        iv_home.setImageResource(R.drawable.home);
        iv_topic.setImageResource(R.drawable.topic);
        iv_find.setImageResource(R.drawable.find);
        iv_myInfo.setImageResource(R.drawable.myself);

        //底部文字变成原来的颜色
        tv_home.setTextColor(0xffA8A8A8);
        tv_topic.setTextColor(0xffA8A8A8);
        tv_find.setTextColor(0xffA8A8A8);
        tv_myInfo.setTextColor(0xffA8A8A8);

    }

}
