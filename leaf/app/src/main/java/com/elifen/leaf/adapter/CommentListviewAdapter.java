package com.elifen.leaf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.PostCommentBean;

import java.util.List;

/**
 * Created by Elifen on 2017/4/25.
 */

public class CommentListviewAdapter extends BaseAdapter{
    private List<PostCommentBean> commentLists;
    private LayoutInflater mInflater;
    private Context context;

    public CommentListviewAdapter(Context context, List<PostCommentBean> commentLists){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.commentLists = commentLists;
    }

    @Override
    public int getCount() {
        return commentLists.size();
    }

    @Override
    public Object getItem(int position) {
        return commentLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.comments_list_item, null);
            viewHolder.headImageView = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.commentText);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PostCommentBean bean = commentLists.get(position);
        viewHolder.headImageView.setImageBitmap(strToBitmap(bean.getUserHead()));
        viewHolder.userName.setText(bean.getUserName());
        viewHolder.time.setText(bean.getC_time());
        viewHolder.comment.setText(bean.getC_content());
        return convertView;
    }

    class ViewHolder{
        public ImageView headImageView;
        public TextView userName;
        public TextView time;
        public TextView comment;
    }
    //将图片字符串转换成Bitmap
    public Bitmap strToBitmap(String headString) {
        byte[] bytes = Base64.decode(headString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void clear() {
        commentLists.clear();
    }

    public void setData(List<PostCommentBean> postList) {
        this.commentLists = postList;
    }
}
