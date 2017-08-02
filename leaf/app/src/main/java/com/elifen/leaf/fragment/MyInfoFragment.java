package com.elifen.leaf.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.elifen.leaf.R;
import com.elifen.leaf.UIActivity.SettingActivity;

import java.util.ArrayList;
/*
* 我的fragment,包括四个子fragment
* */
public class MyInfoFragment extends Fragment implements View.OnClickListener {
    private ArrayList<Fragment> fragments;
    FragmentManager fm;
    private Button mInfo;
    private Button mTopic;
    private Button mComments;
    private Button mPrised;
    private Button btn_set;
    //作为指示标签的按钮
    private ImageView cursor;
    public MyInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        initView(view);
        initFragment();
        showFragment(0);
        return view;
    }

    private void initView(View view) {
        mInfo = (Button) view.findViewById(R.id.info);
        mTopic = (Button) view.findViewById(R.id.myTopic);
        mComments = (Button) view.findViewById(R.id.myComments);
        mPrised = (Button) view.findViewById(R.id.prised);
        cursor = (ImageView) view.findViewById(R.id.cursor_btn);
        btn_set = (Button) view.findViewById(R.id.btn_set);
        initListenner();
        resetButtonColor();
        mInfo.setTextColor(Color.BLUE);
    }

    private void resetButtonColor() {
        mInfo.setTextColor(Color.BLACK);
        mTopic.setTextColor(Color.BLACK);
        mComments.setTextColor(Color.BLACK);
        mPrised.setTextColor(Color.BLACK);
    }

    private void initListenner() {
        mInfo.setOnClickListener(this);
        mTopic.setOnClickListener(this);
        mComments.setOnClickListener(this);
        mPrised.setOnClickListener(this);
        btn_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.info:
                resetButtonColor();
                mInfo.setTextColor(Color.BLUE);
                showFragment(0);
                break;
            case R.id.myTopic:
                resetButtonColor();
                mTopic.setTextColor(Color.BLUE);
                showFragment(1);
                break;
            case R.id.myComments:
                resetButtonColor();
                mComments.setTextColor(Color.BLUE);
                showFragment(2);
                break;
            case R.id.prised:
                resetButtonColor();
                mPrised.setTextColor(Color.BLUE);
                showFragment(3);
                break;
            case R.id.btn_set:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void initFragment(){
        fm = getChildFragmentManager();
        fragments = new ArrayList<Fragment>();
        fragments.add(new DetailInfoFragment());      //个人详情
        fragments.add(new MytopicFragment());         //我的话题
        fragments.add(new MypostFragment());          //我的帖子
        fragments.add(new MyprisedFragment());        //我点赞过的

        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment fragment : fragments) {
            ft.add(R.id.myInfo_frameLayout, fragment);
        }
        ft.commit();
    }

    public void showFragment(int position) {
        hideFragments();
        Fragment fragment = fragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();

        ft.show(fragment);
        ft.commit();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment fragment : fragments) {
            if(fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

}
