package com.elifen.leaf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Elifen on 2017/4/25.
 */
/*
*帖子详情activity中的viewpager的fragemnt适配器
 */
public class myFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmetnmanager;  //创建FragmentManager
    private List<Fragment> listfragment; //创建一个List<Fragment>

    public myFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public myFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fragmetnmanager=fm;
        this.listfragment=list;
    }
    @Override
    public Fragment getItem(int position) {
        return listfragment.get(position); //返回第几个fragment
    }
    @Override
    public int getCount() {
        return listfragment.size();
    }
}
