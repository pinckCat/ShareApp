package com.myAndroidServer.entity;

public class PostPriseBean {
	private int p_id;              //帖子的id
	private int userId;             //点赞人id
	private String userName;        //点赞人的昵称
	private String headName;        //点赞人头像地址
	
	public PostPriseBean(int p_id,int userId,String userName,String headName){
	 this.p_id = p_id;
	 this.userId = userId;
	 this.userName = userName;
	 this.headName = headName;
	   }
	public PostPriseBean(String headName,String userName,int userId){
		this.headName = headName;
		this.userName = userName;
		this.userId = userId;
	}
	public PostPriseBean() {
		// TODO Auto-generated constructor stub
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
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getHeadName() {
		return headName;
	}
	
	public void setHeadName(String headName) {
		this.headName = headName;
	}

 
}
