package com.elifen.leaf.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.PostPriseBean;
import com.elifen.leaf.UIActivity.PersonDetailActivity;
import com.elifen.leaf.net.NetTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elifen on 2017/4/28.
 */

public class FavoriteAdapter extends BaseAdapter {
    private List<PostPriseBean> list;
    private LayoutInflater mInflater;
    private Context context;
    private int mId;
    private int type;         //区分是我的关注（1）还是我的粉丝（2）
    public FavoriteAdapter(Context context,List<PostPriseBean> list,int mId,int type){
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.mId = mId;
        this.type = type;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = mInflater.inflate(R.layout.favorite_list_item,null);
            viewHolder.headImageView = (ImageView) convertView.findViewById(R.id.head);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.userId = (TextView) convertView.findViewById(R.id.tv_id);
            viewHolder.tv_noLike = (TextView) convertView.findViewById(R.id.tv_noLike);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PostPriseBean bean = list.get(position);
        viewHolder.headImageView.setImageBitmap(strToBitmap(bean.getHeadName()));
        viewHolder.userName.setText(bean.getUserName());
        viewHolder.userId.setText("ID:"+bean.getUserId());
        if (type ==2){   //如果是我的粉丝则取消关注文本不可见
            viewHolder.tv_noLike.setVisibility(View.GONE);
        }
        viewHolder.tv_noLike.setOnClickListener(new mListener(viewHolder,bean));
        viewHolder.headImageView.setOnClickListener(new myListener(viewHolder,bean));
        return convertView;
    }

    class ViewHolder {
        public ImageView headImageView;
        public TextView userName;
        public TextView userId;
        public TextView tv_noLike;
    }
    //将图片字符串转换成Bitmap
    public Bitmap strToBitmap(String headString) {
        byte[] bytes = Base64.decode(headString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void clear(){
        this.list.clear();
    }
    public void setData(List<PostPriseBean> list){
        this.list = list;
    }

    private class mListener implements View.OnClickListener {
        private ViewHolder viewHolder;
        private PostPriseBean bean;
        public mListener(ViewHolder viewHolder,PostPriseBean bean){
            this.viewHolder = viewHolder;
            this.bean = bean;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_noLike:
                    new deleteLike().execute();
                    break;
            }
        }
        private class deleteLike extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... params) {
                NetTool netTool = new NetTool();
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("idStr", String.valueOf(bean.getUserId())));
                String s = netTool.Httpconnection("DeleteLikeServlet", param);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                if(s.equals("InternetGG")){
                    Toast.makeText(context,"网络出错！",Toast.LENGTH_SHORT).show();
                }else if (!s.equals("")) {
                    JSONObject jsonObject ;
                    String result = null;
                    try {
                        jsonObject = new JSONObject(s);
                        result = jsonObject.getString("Status");
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    if(result.equals("Success")) {
                        list.remove(bean);
                        notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context, "取消关注失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private class myListener implements View.OnClickListener {
        private ViewHolder viewHolder;
        private PostPriseBean bean;
        public myListener(ViewHolder viewHolder, PostPriseBean bean) {
            this.viewHolder = viewHolder;
            this.bean = bean;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.head:
                    Intent intent = new Intent(context, PersonDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("priseBean",bean);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
