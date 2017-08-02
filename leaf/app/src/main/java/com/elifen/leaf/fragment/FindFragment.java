package com.elifen.leaf.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.myFragmentPagerAdapter;
import com.elifen.leaf.view.SimpleViewPagerIndicator;

import java.util.ArrayList;


public class FindFragment extends Fragment {
    private ArrayList<Fragment> fragments;
    private FragmentManager fm;
    private SimpleViewPagerIndicator indicator;    //指示器
    private ViewPager viewPager;
    private NewsFragment fragment1;
    private MobileFragment fragment2;
    private String[] mTitles = new String[] { "微信精选", "科技前沿" };
    public FindFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find,container,false);
        initView(view);
        initListener();
        initChildFragment();
        return view;
    }

    private void initListener() {
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

    private void initChildFragment() {
        fm = getChildFragmentManager();
        fragments = new ArrayList<Fragment>();
        fragment1 = new NewsFragment();          //微信精选Fragment
        fragment2 = new MobileFragment();        //科技前沿Fragment
        fragments.add(fragment1);
        fragments.add(fragment2);
        myFragmentPagerAdapter mfpa=new myFragmentPagerAdapter(fm, fragments); //new myFragmentPagerAdater记得带上两个参数
        viewPager.setAdapter(mfpa);
        viewPager.setCurrentItem(0); //设置当前页是第一页
    }

    private void initView(View view) {
        indicator = (SimpleViewPagerIndicator) view.findViewById(R.id.indicator);
        indicator.setTitles(mTitles);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
    }
}
