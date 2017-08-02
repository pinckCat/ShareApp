package com.elifen.leaf.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elifen.leaf.R;
import com.elifen.leaf.entity.News;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

public class NewsViewHolder extends BaseViewHolder<News> {

    private TextView mTv_name;
    private ImageView mImg_face;
    private TextView mTv_sign;

    public NewsViewHolder(ViewGroup parent) {
        super(parent, R.layout.news_recycler_item);
        mTv_name = $(R.id.person_name);
        mTv_sign = $(R.id.person_sign);
        mImg_face = $(R.id.person_face);    }

    @Override
    public void setData(final News data) {
        mTv_name.setText(data.getTitle());
        mTv_sign.setText(data.getCtime());
        Glide.with(getContext())
                .load(data.getPicUrl())                     //加载图片的网络地址
                .placeholder(R.mipmap.ic_launcher)          //正在加载中显示的图片
                .centerCrop()
                .into(mImg_face);                           //将图片显示到view中
    }


}
