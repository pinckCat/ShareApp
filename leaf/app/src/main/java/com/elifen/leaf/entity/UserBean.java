package com.elifen.leaf.entity;

/**
 * Created by Elifen on 2017/4/12.
 */

public class UserBean {
    private String TAG = "UserBean";
    private int userId ;                        //用户id
    private String userName = null;             //昵称
    private String password = null;             //登陆密码
    private String sex;                         //性别
    private String phoneNum = null;             //用户手机号码
    private String headPath = null;             //头像
    private String description = null;          //个性签名

    public UserBean(int id,String userName,String password,String sex,String phoneNum,String headPath){
    }

}
