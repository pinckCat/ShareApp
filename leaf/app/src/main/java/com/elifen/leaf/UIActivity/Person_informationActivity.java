package com.elifen.leaf.UIActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elifen.leaf.R;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.net.NetTool;
import com.elifen.leaf.view.SelectPicPopupWindow;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Person_informationActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Person_infomatinoActivity";
    public static final int REQUESTCODE_PICK = 2;
    public static final String IMAGE_FILE_NAME = "headMap.jpg";
    public static final int REQUESTCODE_TAKE = 1;
    public static final String imageType = "image/*";
    public static final int REQUESTCODE_CUTTING = 3;
    private ImageView iv_back;
    private RelativeLayout item_head;
    private RelativeLayout item_name;
    private RelativeLayout item_sex;
    private RelativeLayout item_description;

    private ImageView iv_head;
    private TextView tv_nickName;
    private TextView tv_sex;
    private TextView tv_description;

    public CurrentUser currentUser;
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    private String imageFilePath;

    private String userName;
    private String sex;
    private String description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information);
        currentUser = CurrentUser.getInstance();
        initView();
        initListenner();
    }

    private void initListenner() {
        iv_back.setOnClickListener(this);
        item_head.setOnClickListener(this);
        item_name.setOnClickListener(this);
        item_sex.setOnClickListener(this);
        item_description.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        item_head = (RelativeLayout) findViewById(R.id.item_headName);
        item_name = (RelativeLayout) findViewById(R.id.item_name);
        item_sex = (RelativeLayout) findViewById(R.id.item_sex);
        item_description = (RelativeLayout) findViewById(R.id.item_descripton);
        iv_head = (ImageView) findViewById(R.id.iv_headName);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_description = (TextView) findViewById(R.id.tv_description);

        //得到用户信息
        this.userName = currentUser.getUserName();
        this.sex = currentUser.getSex();
        this.description = currentUser.getDescription();
        //显示用户信息
        iv_head.setImageBitmap(currentUser.getHeadBitmapFromdatabase(currentUser.getHeadString()));
        tv_nickName.setText(userName);
        tv_sex.setText(sex);
        tv_description.setText(description);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = getIntent();
                setResult(0, intent);
                finish();
                break;
            case R.id.item_headName:
                menuWindow = new SelectPicPopupWindow(Person_informationActivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(Person_informationActivity.this.findViewById(R.id.ll_myInfo),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.item_name:
                Intent intent1 = new Intent(Person_informationActivity.this, Edit_nickNameActivity.class);
                startActivityForResult(intent1, 4);
                break;
            case R.id.item_sex:
                Intent intent2 = new Intent(Person_informationActivity.this,Edit_sexActivity.class);
              //  startActivity(intent2);
                startActivityForResult(intent2,5);
                break;
            case R.id.item_descripton:
                Intent intent3 = new Intent(Person_informationActivity.this,Edit_descriptionActivity.class);
                //startActivity(intent3);
                startActivityForResult(intent3,6);
                break;
        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {


        @SuppressLint("LongLogTag")
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    //设置图片的保存路径,作为全局变量
                    imageFilePath = Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME;
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                case R.id.btn_pick_photo:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageType);
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                //调用相机拍照
                File temp = new File(imageFilePath);
                startPhotoZoom(Uri.fromFile(temp));

                break;
            case 2:// 直接从相册获取
                try {
                    Uri originalUri = data.getData(); // 获得图片的uri

                    String[] proj = {MediaStore.Images.Media.DATA};
                    @SuppressWarnings("deprecation")
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    // 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    // 最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
                    startPhotoZoom(originalUri);

                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case 3:
                if (data != null) {
                    setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                }
                break;
            case 4:
                tv_nickName.setText(currentUser.getUserName());
                break;
            case 5:
                tv_sex.setText(currentUser.getSex());
                break;
            case 6:
                tv_description.setText(currentUser.getDescription());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * 剪切图片的方法
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, imageType);
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /*
       保存裁剪之后的图片数据并保存到当地文件夹
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        String result;
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            iv_head.setImageBitmap(photo);
            // 发给服务器
            new sendHeadImg().execute(photo);
        }
    }

    private void saveToFile(Bitmap photo) {
        // 保存到文件
        File file = new File(getFilesDir() + "/avatar");
        if (!file.exists()) {
            file.mkdirs();
        }

        File avatar = new File(getFilesDir() + "/avatar/Avatar.png");
        if (avatar.exists()) {
            avatar.delete();
        }
        try {
            OutputStream stream = new FileOutputStream(avatar);
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            CurrentUser.getInstance().setHeadPath(getFilesDir() + "/avatar/Avatar.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class sendHeadImg extends AsyncTask<Bitmap, Void, String>{
        Bitmap image;
        String headString;
        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(Bitmap... params) {
            image = params[0];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//将Bitmap转成Byte[]
            image.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
            headString = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);//加密转换成String

            NetTool netTool = new NetTool();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("username", currentUser.getUserName()));
            param.add(new BasicNameValuePair("imageString", headString));
            String s = netTool.Httpconnection("UploadImageServlet", param);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                JSONObject jsonObject = null;
                String result = null;
                String headStr = null;
                try {
                    jsonObject = new JSONObject(s);
                    result = jsonObject.getString("Status");
                    headStr = jsonObject.getString("HeadString");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               if(result.equals("Success")) {
                   Toast.makeText(Person_informationActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                   currentUser.setHeadString(headStr);
                   currentUser.writeToFile(Person_informationActivity.this);
                   //Toast.makeText(Person_informationActivity.this, currentUser.getHeadString(), Toast.LENGTH_SHORT).show();
               }
                else if(result.equals("Fail"))
                   Toast.makeText(Person_informationActivity.this, "上传出错！", Toast.LENGTH_SHORT).show();
            }
            else  if(s == null){
                Toast.makeText(Person_informationActivity.this, "请求数据出错！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
