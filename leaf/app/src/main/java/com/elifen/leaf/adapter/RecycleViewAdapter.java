package com.elifen.leaf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.PostCommentBean;

import java.util.List;

/**
 * Created by Elifen on 2017/4/25.
 */

@SuppressWarnings("ALL")
public class RecycleViewAdapter extends RecyclerView.Adapter {
    private List<PostCommentBean> commentLists;
    private LayoutInflater mInflater;
    private Context context;

    public RecycleViewAdapter(Context context, List<PostCommentBean> commentLists){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.commentLists = commentLists;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView headImageView;
        public TextView userName;
        public TextView time;
        public TextView comment;

        public ViewHolder(View itemView) {
            super(itemView);
            headImageView = (ImageView) itemView.findViewById(R.id.iv_head);
            userName = (TextView) itemView.findViewById(R.id.userName);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            comment = (TextView) itemView.findViewById(R.id.commentText);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.comments_list_item,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        PostCommentBean bean = commentLists.get(position);
        vh.headImageView.setImageBitmap(strToBitmap(bean.getUserHead()));
        vh.userName.setText(bean.getUserName());
        vh.time.setText(bean.getC_time().subSequence(2, 16));
        vh.comment.setText(bean.getC_content());
    }

    @Override
    public int getItemCount() {
        return commentLists.size();
    }

    public void clear() {
        commentLists.clear();
    }

    public void setData(List<PostCommentBean> postList) {
        this.commentLists = postList;
    }
    //将图片字符串转换成Bitmap
    public Bitmap strToBitmap(String headString) {
        byte[] bytes = Base64.decode(headString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
