package com.elifen.leaf.UIActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.adapter.GalleryAdapter;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.entity.TopicBean;
import com.elifen.leaf.net.NetTool;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateTopicPostActivity extends AppCompatActivity implements View.OnClickListener {
    private TopicBean bean;
    private TextView tv_title;
    private TextView tv_cancle;
    private TextView tv_certain;
    private EditText et_content;

    private CurrentUser currentUser;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<String> paths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic_post);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        bean = (TopicBean) bundle.getSerializable("topicBean");
    }

    private void initListener() {
        tv_cancle.setOnClickListener(this);
        tv_certain.setOnClickListener(this);
    }

    private void initView() {
        currentUser = CurrentUser.getInstance();
        tv_cancle= (TextView) findViewById(R.id.tv_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(bean.getTitle());
        tv_certain = (TextView) findViewById(R.id.tv_certain);
        et_content = (EditText) findViewById(R.id.et_TextContent);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //添加适配器
        mAdapter = new GalleryAdapter(this, onAddPicClickListener);
        mAdapter.setSelectMax(1);
        mRecyclerView.setAdapter(mAdapter);
        //设置图片路径初始值为空
        paths = null;
    }
    //添加和删除图片接口回掉
    private GalleryAdapter.onAddPicClickListener onAddPicClickListener =
            new GalleryAdapter.onAddPicClickListener(){

                @Override
                public void onAddPicClick(int type, int position) {
                    switch (type) {
                        //进入相册，添加图片
                        case 0:
                            // Toast.makeText(CreatePostActivity.this,"添加图片",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CreateTopicPostActivity.this, PhotoSelectorActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("limit", 1 );//设置选择图片的数量为1
                            startActivityForResult(intent, 0);
                            break;
                        //删除图片
                        case 1:
                            paths.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            break;
                    }
                }
            };

    //重写onActivityResult方法获取拍照或者图片地址
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(data != null){
                    //path是选择拍照或者图片的地址数组
                    paths = (List<String>) data.getExtras().getSerializable("photos");
                    mAdapter.setList(paths);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                onBackPressed();
                finish();
                break;
            case R.id.tv_certain:
                if(et_content.getText().toString().equals("")){
                    Toast.makeText(CreateTopicPostActivity.this,"内容不能为空！",Toast.LENGTH_LONG).show();
                }
                else {
                    tv_certain.setClickable(false);
                    new saveAsynTask().execute();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class saveAsynTask extends AsyncTask<Void,Void,String> {
        String userName ;
        String textContent;
        String imageStr;
        ProgressDialog progressDialog = null;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CreateTopicPostActivity.this,"提示","发表中，请稍后。。。");
            userName = currentUser.getUserName();
            textContent = et_content.getText().toString();
            if (paths == null){
                imageStr = null;
            }else {
                //得到图片的路径
                String imagePath = paths.get(0);
                Bitmap image = BitmapFactory.decodeFile(imagePath);    //通过路径得到图片 转成Bitmap类型
                ByteArrayOutputStream baos = new ByteArrayOutputStream();//将Bitmap转成Byte[]
                image.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
                imageStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);//加密转换成String
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("userName", userName));
            param.add(new BasicNameValuePair("t_idStr",String.valueOf(bean.getT_id())));
            param.add(new BasicNameValuePair("textContent", textContent));
            param.add(new BasicNameValuePair("imageStr", imageStr));
            String s = netTool.Httpconnection("CreateTopicPostSevlet", param);
            return s;
        }
        @Override
        protected void onPostExecute(String s) {
            if (s.equals("InternetGG")) {
                Toast.makeText(CreateTopicPostActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObject;
                String result = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("Success")) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    tv_certain.setClickable(true);
                    Toast.makeText(CreateTopicPostActivity.this, "发表成功！", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                } else if(result.equals("Fail"))
                    progressDialog.dismiss();
                    Toast.makeText(CreateTopicPostActivity.this, "上传出错！", Toast.LENGTH_SHORT).show();
                    tv_certain.setClickable(true);
            }
        }
    }
}
