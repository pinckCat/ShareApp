package com.elifen.leaf.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * 这个类是个单例类
 * 用来保存当前登录的用户的基本信息，这样就不用每次都去读取文件内容了
 * Created by Elifen on 2017/4/6.
 */

public class CurrentUser {
    public static String IP = "192.168.137.1:8080";         //服务器id(本地)
    private String TAG = "CurrentUser";
    private static CurrentUser currentUser = null;
    private boolean LoginState = false;              //登陆状态
    private int userId ;                          //用户id
    private String userName = null;             //用户名
    private String password = null;             //登陆密码
    private String sex = null;                   //性别
    private String phoneNum = null;             //用户手机号码
    private String headPath = null;             //本地存放的头像路径
    private String headString = null;           //数据库返回的头像String
    private String description = null;          //个性签名
    private CurrentUser() {
    }
    public static CurrentUser getInstance() {
        if (currentUser == null)
            currentUser = new CurrentUser();
        return currentUser;
    }
    public boolean isLogin() {
        return LoginState;
    }

    public void UserLogin(JSONObject jsonObject) {
        try {
            LoginState = true;
            userId = jsonObject.getInt("UserId");
            userName = jsonObject.getString("UserName");
            password = jsonObject.getString("Password");
            sex = jsonObject.getString("Sex");
            phoneNum = jsonObject.getString("PhoneNum");
            headString = jsonObject.getString("HeadString");
            description = jsonObject.getString("Description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过SharePreferences 得到用户信息
     * @param context
     */
    public void readFromFile(Context context) {
        SharedPreferences sp = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        userId = sp.getInt("UserId",0);
        userName = sp.getString("UserName", null);
        password = sp.getString("Password", null);
        sex = sp.getString("Sex",null);
        headString = sp.getString("HeadString",null);
        description = sp.getString("Description", null);
        LoginState = (userName != null);
        File head = new File(context.getFilesDir() + "/avatar/Avatar.png");
        if (head.exists())
            headPath = context.getFilesDir() + "/avatar/Avatar.png";
    }

    /**
     * 用户登陆成功将信息存储
     * @param context
     */
    public void writeToFile(Context context) {
        SharedPreferences sp = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("UserId", userId);
        editor.putString("UserName", userName);
        editor.putString("Password", password);
        editor.putString("Sex", sex);
        editor.putString("HeadString", headString);
        editor.putString("Description", description);
        editor.apply();
    }
    //退出登陆
    public void UserLogout(Context context) {

        LoginState = false;
        userName = null;
        password = null;
        sex = null;
        headString = null;
        description = null;
        File file = new File(context.getFilesDir() + "/avatar/Avatar.png");
        if (file.exists())
            file.delete();
    }

    //清除保存的数据
    public void clearFile(Context context) {
        SharedPreferences sp = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }
    //得到本地图片
    public Bitmap getHeadBitmapByLocal(Context context) throws IOException {
        return getHeadFromFile(context);
    }

    private Bitmap getHeadFromFile(Context context) {
        File file = new File(context.getFilesDir() + "/avatar/Avatar.png");
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(headPath);
            return bm;
        }
        else
            return null;
    }

    public Bitmap getHeadBitmapFromdatabase(String headString){
        byte[] bytes = Base64.decode(headString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setHeadPath(String headPath_) {
        this.headPath = headPath_;
    }
    public String getHeadString() {
        return headString;
    }
   public void setHeadString(String headString){
       this.headString = headString;
   }
   public String getSex(){
       return sex;
   }
   public void setSex(String sex){
       this.sex = sex;
   }
   public String getDescription(){
       return description;
   }
   public void setDescription(String description){
       this.description = description;
   }

   public int getUserId(){
       return userId;
   }


}
