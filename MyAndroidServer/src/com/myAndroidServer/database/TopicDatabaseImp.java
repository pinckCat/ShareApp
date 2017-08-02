package com.myAndroidServer.database;

import java.util.ArrayList;
import java.util.List;

import com.myAndroidServer.entity.PostBean;
import com.myAndroidServer.entity.TopicBean;
import com.myAndroidServer.util.Image;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class TopicDatabaseImp {
	private Connection connection;
	private String sql;
	private PreparedStatement statement;
	private List<PostBean> postLists;
	
	public TopicDatabaseImp(Connection connection){
		this.connection = connection;
		this.sql = null;
	}
	
	//创造话题
	public boolean createTopic(TopicBean bean){
		try {
			sql = "insert into topic (createrId, title, time) values(?,?,now())";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, bean.getCreaterId());
			statement.setString(2, bean.getTitle());
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	public List<TopicBean> getTopics(){
		List<TopicBean> topicLists = new ArrayList<TopicBean>();
		try {
			sql = "select * from topic";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			boolean haveData = false;
			ResultSet rs = (ResultSet) statement.executeQuery();
			while(rs.next()){
				int t_id = rs.getInt("t_id");
				int createrId = rs.getInt("createrId");
				int count = rs.getInt("count");
				String title = rs.getString("title");
				String time = rs.getString("time");
				TopicBean bean = new TopicBean(t_id,createrId,title,count,time);
				topicLists.add(bean);
				haveData = true;
			}
			if(haveData){
				return topicLists;
			}else{
				return null;
			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
	}
	//查询包含某一字段的话题
	public List<TopicBean> searchTopics(String str){
		List<TopicBean> topicLists = new ArrayList<TopicBean>();
		try {
			sql = "select * from topic where title like '%"+str+"%'";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			boolean haveData = false;
			ResultSet rs = (ResultSet) statement.executeQuery();
			while(rs.next()){
				int t_id = rs.getInt("t_id");
				int createrId = rs.getInt("createrId");
				int count = rs.getInt("count");
				String title = rs.getString("title");
				String time = rs.getString("time");
				TopicBean bean = new TopicBean(t_id,createrId,title,count,time);
				topicLists.add(bean);
				haveData = true;
			}
			if(haveData){
				return topicLists;
			}else{
				return null;
			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
	}
	//更新话题浏览数量
	public boolean updataCount(int count,int t_id){
		try {
			sql = "update topic set count = ? where t_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, count);
			statement.setInt(2, t_id);
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	  //创建话题的帖子
		public boolean createTopicPost(PostBean post){
			try {
				sql = "insert into topic_post (t_id, userId, userName, userHeadStr, textContent, imageContent, time) values(?,?,?,?,?,?,now())";
				statement = (PreparedStatement)connection.prepareStatement(sql);
				statement.setInt(1, post.getT_id());
				statement.setInt(2, post.getUserId());
				statement.setString(3, post.getUserNameStr());
				statement.setString(4, post.getUserHeadStr());
				statement.setString(5, post.getTextContent());
				statement.setString(6, post.getImageContent());
				statement.executeUpdate();
				statement.close();
				return true;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return false;
				}
		}
		
		//获取所有话题下的所有帖子
		public List<PostBean> getPost(int t_id){
			postLists = new ArrayList<PostBean>();
			try {
	
				sql = "select * from topic_post where t_id = ?";       
				statement = (PreparedStatement)connection.prepareStatement(sql);
				statement.setInt(1, t_id);
			    boolean haveData = false;
				ResultSet rs = (ResultSet) statement.executeQuery();
			    while(rs.next()){
			    	String imageContent ="";
			    	int p_id = rs.getInt(1);
			    	int userId = rs.getInt(3);
			    	String userName = rs.getString(4);
			    	String userHeadStr = Image.GetImageStr(rs.getString(5));
			    	String textContent = rs.getString(6);
			    	if(rs.getString(7)!=null)	
			         imageContent = Image.GetImageStr(rs.getString(7));    
			    	String time = rs.getString(8);	    	
			    	PostBean postBean = new PostBean(t_id,p_id,userId,userName,userHeadStr,textContent,imageContent,
			    			time);
			    	postLists.add(postBean);
			    	haveData = true;
			    }
			    rs.close();
				statement.close();
				if(haveData){
				  return postLists;
				}else{
					return null;
				}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return null;
				}
		}
		
	//得到我发过的所有帖子
	public List<TopicBean> getMyTopic(int currentUserId) {
		List<TopicBean> topicLists = new ArrayList<TopicBean>();
		try {
			sql = "select * from topic where createrId = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, currentUserId);
			boolean haveData = false;
			ResultSet rs = (ResultSet) statement.executeQuery();
			while (rs.next()) {
				int t_id = rs.getInt("t_id");
				int createrId = rs.getInt("createrId");
				int count = rs.getInt("count");
				String title = rs.getString("title");
				String time = rs.getString("time");
				TopicBean bean = new TopicBean(t_id,createrId,title,count,time);
				topicLists.add(bean);
				haveData = true;
			}
			if (haveData) {
				return topicLists;
			}else{
				return null;
			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
	}
	
	//删除话题
	public boolean delectMyTopic(int t_id){
		try {
			sql = "delete from topic where t_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1,t_id);
			statement.executeUpdate();
			statement.close();
			deleteAllPost(t_id);             	//删除话题下的所有帖子
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}

 	//删除话题下的所有帖子
	public void deleteAllPost(int t_id){
		try {
			sql = "delete from topic_post where t_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1,t_id);
			statement.executeUpdate();
			statement.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
}
