package com.myAndroidServer.entity;

public class PostCommentBean {
    private int c_id;
    private int p_id;
    private int userId;
    private String userHead;
    private String userName;
    private String c_time;
    private String c_content;
    //插入评论表时用
    public PostCommentBean(int p_id,int userId,String userHead,String userName,
            String c_content){
         this.p_id = p_id;
         this.userId = userId;
         this.userHead = userHead;
         this.userName = userName;
         this.c_content = c_content;
   }
    
    //返回评论信息时候用
    public PostCommentBean(int userId,String userHead,String userName,
                           String c_time,String c_content){
        this.userId = userId;
        this.userHead = userHead;
        this.userName = userName;
        this.c_time = c_time;
        this.c_content = c_content;
    }
    
    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getC_time() {
        return c_time;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }

    public String getC_content() {
        return c_content;
    }

    public void setC_content(String c_content) {
        this.c_content = c_content;
    }
}