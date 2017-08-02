package com.elifen.leaf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.TopicBean;

import java.util.List;

/**
 * Created by Elifen on 2017/4/27.
 */

public class TopicListviewAdapter extends BaseAdapter {
    private List<TopicBean> topicBeanList;
    private LayoutInflater mInflater;
    private Context context;

    public TopicListviewAdapter(Context context, List<TopicBean> postList){
        this.mInflater = LayoutInflater.from(context);
        this.topicBeanList = postList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return topicBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicBeanList.get(position);
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
            convertView = mInflater.inflate(R.layout.topic_list_item, null);
            viewHolder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TopicBean bean = topicBeanList.get(position);
        viewHolder.title.setText(bean.getTitle());
        viewHolder.count.setText(bean.getCount()+"次浏览");
        return convertView;
    }
    class ViewHolder {
        public LinearLayout ll_item;
        public TextView title;
        public TextView count;
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        topicBeanList.clear();
    }

    public void setData(List<TopicBean> postList) {
        this.topicBeanList = postList;
    }

}
