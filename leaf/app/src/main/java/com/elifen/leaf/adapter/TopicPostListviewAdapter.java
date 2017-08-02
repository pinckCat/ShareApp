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
import com.elifen.leaf.entity.PostBean;

import java.util.List;

/**
 * Created by Elifen on 2017/5/1.
 */

public class TopicPostListviewAdapter extends BaseAdapter {
    private List<PostBean> postList;
    private LayoutInflater mInflater;
    private Context context;

    public TopicPostListviewAdapter(Context context, List<PostBean> postList){
        this.mInflater = LayoutInflater.from(context);
        this.postList = postList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.topicpost_item_layout, null);
            viewHolder.headImageView = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.userNameStr = (TextView) convertView.findViewById(R.id.tv_userName);
            viewHolder.imageContent = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.textContent = (TextView) convertView.findViewById(R.id.tv_textContent);
            viewHolder.timeStr = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostBean bean = postList.get(position);
        viewHolder.headImageView.setImageBitmap(strToBitmap(bean.getUserHeadStr()));
        viewHolder.userNameStr.setText(bean.getUserNameStr());
        viewHolder.imageContent.setTag(bean.getImageContent());       //用来解决图片显示错乱的问题
        if (viewHolder.imageContent.getTag() != null) {
            if (bean.getImageContent() != null) {
                viewHolder.imageContent.setImageBitmap(strToBitmap(bean.getImageContent()));
            } else {
                viewHolder.imageContent.setVisibility(View.GONE);
            }
        }
        viewHolder.textContent.setText(bean.getTextContent());
        viewHolder.timeStr.setText(bean.getTime().subSequence(2, 16));
        return convertView;
    }

    class ViewHolder {
        public ImageView headImageView;       //头像
        public TextView userNameStr;          //昵称
        public ImageView imageContent;        //发表的图片内容
        public TextView textContent;          //文字内容
        public TextView timeStr;              //时间
    }

    //将图片字符串转换成Bitmap
    public Bitmap strToBitmap(String headString) {
        byte[] bytes = Base64.decode(headString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        postList.clear();
    }

    public void setData(List<PostBean> postList) {
        this.postList = postList;
    }

}
