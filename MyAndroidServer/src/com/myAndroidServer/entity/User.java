package com.myAndroidServer.entity;

public class User {
	private String username,password,phoneNum,headName,description,sex;
	private int userId;
	
	 public User(String username,String password,String phoneNum) {
		 this.username = username;
		 this.password = password;
		 this.phoneNum = phoneNum;
		 this.headName = "d:/AdminImages/a.jpg";
		 this.description = "未填写";
		 this.sex= "男";
	 }
	 public User(int userId,String username,String password,String sex,String phoneNum,String headName,String description) {
		 this.userId = userId;
		 this.username = username;
		 this.password = password;
		 this.sex = sex;
		 this.headName = headName;
		 this.description = description;
		 this.phoneNum = phoneNum;
	 }
	 
	 
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getHeadName() {
		return headName;
	}
	public void setHeadName(String headName) {
		this.headName = headName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public void setSex(String sex){
		this.sex = sex;
	}
	
	public String getSex(){
	   return sex;
	}
	 
}
