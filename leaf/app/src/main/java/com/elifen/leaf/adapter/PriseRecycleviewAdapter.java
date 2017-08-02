package com.elifen.leaf.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.PostPriseBean;
import com.elifen.leaf.UIActivity.PersonDetailActivity;

import java.util.List;

/**
 * Created by Elifen on 2017/4/27.
 */

public class PriseRecycleviewAdapter extends RecyclerView.Adapter {
    private List<PostPriseBean> priseLists;
    private LayoutInflater mInflater;
    private Context context;

    public PriseRecycleviewAdapter(Context context, List<PostPriseBean> priseLists){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.priseLists = priseLists;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView headImageView;
        public TextView userName;
        public TextView userId;

        public ViewHolder(View itemView) {
            super(itemView);
            headImageView = (ImageView) itemView.findViewById(R.id.head);
            userName = (TextView) itemView.findViewById(R.id.tv_name);
            userId = (TextView) itemView.findViewById(R.id.tv_id);

        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PriseRecycleviewAdapter.ViewHolder(mInflater.inflate(R.layout.prise_list_item,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       ViewHolder viewHolder = (ViewHolder) holder;
        final PostPriseBean bean = priseLists.get(position);
        viewHolder.headImageView.setImageBitmap(strToBitmap(bean.getHeadName()));
        viewHolder.userName.setText(bean.getUserName());
        viewHolder.userId.setText("ID:"+bean.getUserId());
        viewHolder.headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("priseBean",bean);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return priseLists.size();
    }

    public void setData(List<PostPriseBean> priseLists){
        this.priseLists =priseLists;
    }

    public void clear(){
        priseLists.clear();
    }
    //将图片字符串转换成Bitmap
    public Bitmap strToBitmap(String headString) {
        byte[] bytes = Base64.decode(headString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
