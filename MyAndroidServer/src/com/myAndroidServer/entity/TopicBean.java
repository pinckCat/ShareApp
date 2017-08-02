package com.myAndroidServer.entity;

public class TopicBean {
	private int createrId;
	private String title;
	private int count;
	private String time;
	private int t_id;
	public TopicBean(int t_id,int createrId,String title,int count,String time){
		this.t_id = t_id;
		this.createrId = createrId;
		this.title = title;
		this.count = count;
		this.time = time;
	}
	
	public TopicBean(int createrId,String title){
	 this.createrId= createrId;
	 this.title = title;
	}
	public int getCreaterId() {
		return createrId;
	}

	public void setCreaterId(int createrId) {
		this.createrId = createrId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getT_id(){
		return t_id;
	}
	public void setT_id(int t_id){
		this.t_id = t_id;
	}
}
