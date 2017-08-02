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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.UIActivity.PostdetailActivity;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.entity.PostBean;
import com.elifen.leaf.net.NetTool;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elifen on 2017/4/20.
 */

public class PostListviewAdapter extends BaseAdapter {
    private List<PostBean> postList;
    private LayoutInflater mInflater;
    private Context context;
    private CurrentUser currentUser;
    public PostListviewAdapter(Context context, List<PostBean> postList, CurrentUser currentUser) {
        this.mInflater = LayoutInflater.from(context);
        this.postList = postList;
        this.context = context;
        this.currentUser = currentUser;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.post_list_item, null);

            viewHolder.rl_head = (RelativeLayout) convertView.findViewById(R.id.rl_head);
            viewHolder.iv_addFriend = (ImageView) convertView.findViewById(R.id.iv_add);

            viewHolder.headImageView = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.iv_more = (ImageView) convertView.findViewById(R.id.iv_more);

            viewHolder.userNameStr = (TextView) convertView.findViewById(R.id.tv_userName);
            viewHolder.imageContent = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.textContent = (TextView) convertView.findViewById(R.id.tv_textContent);
            viewHolder.timeStr = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.delete = (TextView) convertView.findViewById(R.id.tv_delete);

            viewHolder.ll_comment = (LinearLayout) convertView.findViewById(R.id.ll_comment);

            viewHolder.commentNum = (TextView) convertView.findViewById(R.id.tv_commentNum);
            viewHolder.iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);

            viewHolder.ll_prise = (LinearLayout) convertView.findViewById(R.id.ll_prise);

            viewHolder.priseNum = (TextView) convertView.findViewById(R.id.tv_priseNum);
            viewHolder.iv_prise = (ImageView) convertView.findViewById(R.id.iv_prise);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostBean bean = postList.get(position);
        if (bean.getUserNameStr().equals(currentUser.getUserName()) ||
                bean.getRelationship()) {
            viewHolder.iv_addFriend.setVisibility(View.GONE);
        } else {
            viewHolder.iv_addFriend.setVisibility(View.VISIBLE);
        }
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
        //若是当前用户发过的帖子则显示删除文本
        if(bean.getUserId() == currentUser.getUserId()){
            viewHolder.delete.setVisibility(View.VISIBLE);
        }else{
            viewHolder.delete.setVisibility(View.GONE);
        }
        //显示点赞数
        viewHolder.priseNum.setText(bean.getPriseNum()+"");
        viewHolder.commentNum.setText(bean.getCommentNum()+"");
        //判断是否点过赞,点过赞的话设置深色图标
        if (bean.getIsPrised()) {
            viewHolder.iv_prise.setImageResource(R.drawable.prise_pressed_icon);
        } else {
            viewHolder.iv_prise.setImageResource(R.drawable.prise_icon);
        }
        //设置监听器

        viewHolder.rl_head.setOnClickListener(new MyAdapterListener(bean, viewHolder));
        viewHolder.ll_comment.setOnClickListener(new MyAdapterListener(bean, viewHolder));
        viewHolder.ll_prise.setOnClickListener(new MyAdapterListener(bean, viewHolder));
        viewHolder.iv_addFriend.setOnClickListener(new MyAdapterListener(bean, viewHolder));
        viewHolder.iv_more.setOnClickListener(new MyAdapterListener(bean, viewHolder));
        viewHolder.delete.setOnClickListener(new MyAdapterListener(bean, viewHolder));
        return convertView;
    }


    class ViewHolder {
        public RelativeLayout rl_head;        //包含头像、关注图标的relativeLayout
        public ImageView headImageView;       //头像
        public TextView userNameStr;          //昵称
        public ImageView imageContent;        //发表的图片内容
        public TextView textContent;          //文字内容
        public TextView timeStr;              //时间
        public TextView delete;               //删除

        public ImageView iv_addFriend;        //关注图标
        public ImageView iv_more;             //更多图标
        public LinearLayout ll_prise;
        public TextView priseNum;             //点赞数
        public ImageView iv_prise;            //点赞图标

        public LinearLayout ll_comment;
        public TextView commentNum;           //评论数
        public ImageView iv_comment;          //评论图标
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

    class MyAdapterListener implements View.OnClickListener {
        private PostBean bean;
        private ViewHolder viewHolder;

        public MyAdapterListener(PostBean bean, ViewHolder viewHolder) {
            this.bean = bean;
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.rl_head:
                    Intent intent = new Intent(context, PostdetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PostBean", bean);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
                case R.id.ll_comment:
                    Intent intent1 = new Intent(context, PostdetailActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("PostBean", bean);
                    intent1.putExtras(bundle1);
                    context.startActivity(intent1);
                    break;
                case R.id.ll_prise:
                    if (bean.getIsPrised()) {
                        //已经点过赞
                        bean.setIsPrised(false);
                        viewHolder.iv_prise.setImageResource(R.drawable.prise_icon);
                        bean.setPriseNum(bean.getPriseNum()-1);
                        viewHolder.priseNum.setText(bean.getPriseNum()+"");
                        //访问后台，操作数据库
                        new deletePriseAsynTask().execute();
                    } else {
                        //没有点过赞
                        bean.setIsPrised(true);
                        viewHolder.iv_prise.setImageResource(R.drawable.prise_pressed_icon);
                        bean.setPriseNum(bean.getPriseNum()+1);
                        viewHolder.priseNum.setText(bean.getPriseNum()+"");
                        //操作数据库
                        new priseAsynTask().execute();
                    }
                    break;
                case R.id.iv_add:
                     new addFriendAsynTask().execute();
                    break;
                case R.id.iv_more:
                    Toast.makeText(context, "更多敬请期待", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_delete:
                    new deleteAsynTask().execute();
                    break;
                default:
                    break;
            }
        }
        private class priseAsynTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {
                NetTool netTool = new NetTool();
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("p_idStr", String.valueOf(bean.getP_id())));
                param.add(new BasicNameValuePair("userNameStr", currentUser.getUserName()));
                String s = netTool.Httpconnection("PriseServlet", param);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    JSONObject jsonObject;
                    String result = null;
                    try {
                        jsonObject = new JSONObject(s);
                        result = jsonObject.getString("Status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (result.equals("Success")) {
                        Toast.makeText(context, "点赞成功！", Toast.LENGTH_SHORT).show();
                    } else if (result.equals("Fail")) {
                        Toast.makeText(context, "点赞失败！", Toast.LENGTH_SHORT).show();
                    } else if (s == null) {
                        Toast.makeText(context, "请求数据库出错！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        private class deletePriseAsynTask extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Void... params) {
                NetTool netTool = new NetTool();
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("p_idStr", bean.getP_id() + ""));
                String s = netTool.Httpconnection("DeletePriseServlet", param);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    JSONObject jsonObject;
                    String result = null;
                    try {
                        jsonObject = new JSONObject(s);
                        result = jsonObject.getString("Status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (result.equals("Success")) {
                        Toast.makeText(context, "成功取消点赞！", Toast.LENGTH_SHORT).show();
                    } else if (result.equals("Fail")) {
                        Toast.makeText(context, "取消点赞失败！", Toast.LENGTH_SHORT).show();
                    } else if (s == null) {
                        Toast.makeText(context, "请求数据库出错！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        private class addFriendAsynTask extends AsyncTask<Void,Void,String>{
            @Override
            protected void onPreExecute() {
                viewHolder.iv_addFriend.setVisibility(View.GONE);
            }
            @Override
            protected String doInBackground(Void... params) {
                NetTool netTool = new NetTool();
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("userIdStr", currentUser.getUserId()+""));
                param.add(new BasicNameValuePair("friendIdStr",bean.getUserId()+""));
                String s = netTool.Httpconnection("AddFriendServlet", param);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                JSONObject jsonObject;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(result.equals("Already concerned")){
                    Toast.makeText(context,"已经是好友",Toast.LENGTH_SHORT).show();
                }else if(result.equals("Success")){
                    Toast.makeText(context,"成功添加好友！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"添加好友失败！",Toast.LENGTH_SHORT).show();
                }
            }

        }

        private class deleteAsynTask extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... params) {
                NetTool netTool = new NetTool();
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("p_idStr", bean.getP_id()+""));
                String s = netTool.Httpconnection("DeletePostServlet", param);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                JSONObject jsonObject;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(result.equals("Success")) {
                    postList.remove(bean);
                    setData(postList);
                    notifyDataSetChanged();
                    Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"删除失败！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
