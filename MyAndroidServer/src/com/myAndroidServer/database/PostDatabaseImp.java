package com.myAndroidServer.database;

import java.util.ArrayList;
import java.util.List;

import com.myAndroidServer.entity.PostBean;
import com.myAndroidServer.entity.PostCommentBean;
import com.myAndroidServer.entity.PostPriseBean;
import com.myAndroidServer.util.Image;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class PostDatabaseImp {
	private Connection connection;
	private String sql;
	private PreparedStatement statement;
	private List<PostBean> postLists;
	private List<PostCommentBean> commentList;
	
	public PostDatabaseImp(Connection connection){
		this.connection = connection;
		sql = null;
	}
	
	//创建帖子
	public boolean createPost(PostBean post){
		try {
			sql = "insert into post (userId, userName, userHeadStr, textContent, imageContent, time) values(?,?,?,?,?,now())";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, post.getUserId());
			statement.setString(2, post.getUserNameStr());
			statement.setString(3, post.getUserHeadStr());
			statement.setString(4, post.getTextContent());
			statement.setString(5, post.getImageContent());
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	
   //遍历post表，放回全部的帖子信息
	public List<PostBean> getPostInfo(int num,int currentUserId){
		postLists = new ArrayList<PostBean>();
		try {
			if(num == 0){
				sql = "select * from post order by p_id desc limit 5";       
				statement = (PreparedStatement)connection.prepareStatement(sql);
			}
			else {
				sql = "select * from post where p_id < "+num+" order by p_id desc limit 5";    
				statement = (PreparedStatement)connection.prepareStatement(sql);
			}
		    boolean haveData = false;
			ResultSet rs = (ResultSet) statement.executeQuery();
		    while(rs.next()){
		    	String imageContent ="";
		    	int priseNum = 0;
		    	int commentNum = 0;
		    	boolean isPrised;
		    	int p_id = rs.getInt(1);
		    	int userId = rs.getInt(2);
		    	String userName = rs.getString(3);
		    	String userHeadStr = Image.GetImageStr(rs.getString(4));
		    	String textContent = rs.getString(5);
		    	if(rs.getString(6)!=null)	
		         imageContent = Image.GetImageStr(rs.getString(6));    
		    	String time = rs.getString(7);
		    	boolean relationship = getRelationship(currentUserId,userId);    //查看是否关注了用户
		    	isPrised = isPrised(p_id,currentUserId);             //查询用户是否对这个帖子点过赞
		    	priseNum = getPriseNum(p_id);                    //得到帖子的点赞数
		    	commentNum = getCommentNum(p_id);                //得到帖子的评论数
		    	PostBean postBean = new PostBean(p_id,userId,userName,userHeadStr,textContent,imageContent,
		    			time,relationship,isPrised,priseNum,commentNum);
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
	
	
	//查询帖子点赞表，查询是否对帖子点过赞
	public boolean isPrised(int p_id,int currentUserId){
		try {
			sql = "select * from p_prise where p_id = ? and userId = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, p_id);
			statement.setInt(2, currentUserId);
			ResultSet rs = (ResultSet) statement.executeQuery();
			if(rs.next()){
			   return true;
			}
			rs.close();
			statement.close();
			return false;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	//点赞,在点赞表中插入信息
	public boolean prise(PostPriseBean bean){
		try {
			sql = "insert into p_prise (p_id, userId, userName, headName) values(?,?,?,?)";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, bean.getP_id());
			statement.setInt(2, bean.getUserId());
			statement.setString(3, bean.getUserName());
			statement.setString(4, bean.getHeadName());
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	//取消点赞
	public boolean deletePrise(int p_id){
		try {
			sql = "delete from p_prise where p_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1,p_id);
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	//添加好友
	public boolean addFriend(int userId,int friendId){
		try {
			sql = "insert into userfriends (user_id, friend_id) values(?,?)";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, userId);
			statement.setInt(2, friendId);
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
   
    //查看当前用户是否关注了id为userId的用户
	public boolean getRelationship(int currentUserId,int userId){
		try {
			sql = "select * from userfriends where user_id = ? and friend_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, currentUserId);
			statement.setInt(2, userId);
			ResultSet rs = (ResultSet) statement.executeQuery();
			if(rs.next()){
				return true;
			}
			rs.close();
			statement.close();
			return false;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	//得到帖子点赞数
	public int getPriseNum(int p_id){
		int count=0;
		try {
			sql = "select * from p_prise where p_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, p_id);
			ResultSet rs = (ResultSet) statement.executeQuery();
			while(rs.next()){
				//String name = rs.getString("userName");
				count++;
			}
			rs.close();
			statement.close();
			return count;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return count;
			}
	}
	
	//得到评论数
	public int getCommentNum(int p_id){
		int count=0;
		try {
			sql = "select * from p_comment where p_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, p_id);
			ResultSet rs = (ResultSet) statement.executeQuery();
			while(rs.next()){
				//String name = rs.getString("userName");
				count++;
			}
			rs.close();
			statement.close();
			return count;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return count;
			}
	}
	//向数据库插入评论
  public boolean insertComment(PostCommentBean bean){
		try {
			sql = "insert into p_comment (p_id, userId, userHead, userName, c_content, c_time) values(?,?,?,?,?,now())";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, bean.getP_id());
			statement.setInt(2, bean.getUserId());
			statement.setString(3, bean.getUserHead());
			statement.setString(4, bean.getUserName());
			statement.setString(5, bean.getC_content());
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
  } 
	//得到帖子评论
   public List<PostCommentBean> getComments(int p_id){
		 //遍历评论表，得到指定id全部评论信息
	  commentList = new ArrayList<PostCommentBean>();
	  try {
		sql = "select * from p_comment where p_id = ?";
		statement = (PreparedStatement)connection.prepareStatement(sql);
		statement.setInt(1, p_id);
		ResultSet rs = (ResultSet) statement.executeQuery();
		boolean haveData = false;
		while(rs.next()){
			//String name = rs.getString("userName");
			int userId = rs.getInt(3);
			String userHead = Image.GetImageStr(rs.getString(4));
			String userName = rs.getString(5);
			String c_content = rs.getString(6);
			String c_time = rs.getString(7);
			PostCommentBean bean = new PostCommentBean(userId,userHead,userName,c_time,c_content);
			commentList.add(bean);
			haveData = true;
		}
		rs.close();
		statement.close();
		if(haveData){
			return commentList;
		}
		else{
		   return null;
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
   }
   
   //返回点赞信息
   public List<PostPriseBean> getPriseInfo(int p_id){
	   List<PostPriseBean> list = new ArrayList<PostPriseBean>();
	   try {
			sql = "select * from p_prise where p_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, p_id);
			ResultSet rs = (ResultSet) statement.executeQuery();
			boolean haveData = false;
			while(rs.next()){
				String userHead = Image.GetImageStr(rs.getString("headName"));
				String userName = rs.getString("userName");
				int userId = rs.getInt("userId");
				PostPriseBean bean = new PostPriseBean(userHead,userName,userId);
				list.add(bean);
				haveData = true;
			}
			rs.close();
			statement.close();
			if(haveData){
				return list;
			}else
			  return null;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
   }
   
   //删除帖子
   public boolean deletePost(int p_id){
	   try {
			sql = "delete from post where p_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1,p_id);
			statement.executeUpdate();
			statement.close();
			deleteComment(p_id);              //删除该帖子所有评论信息
			deletePriseInfo(p_id);            //删除该帖帖子所有的点赞信息
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
   }
   
 //删除p_id的评论信息
 	public void deleteComment(int p_id){
 		try {
 			sql = "delete from p_comment where p_id = ?";
 			statement = (PreparedStatement)connection.prepareStatement(sql);
 			statement.setInt(1,p_id);
 			statement.executeUpdate();
 			statement.close();
 			} catch (Exception e) {
 				// TODO: handle exception
 				e.printStackTrace();
 			}
 	}
 	//删除p_id所有的点赞信息
 	public void deletePriseInfo(int p_id){
 		try {
 			sql = "delete from p_prise where p_id = ?";
 			statement = (PreparedStatement)connection.prepareStatement(sql);
 			statement.setInt(1,p_id);
 			statement.executeUpdate();
 			statement.close();
 			} catch (Exception e) {
 				// TODO: handle exception
 				e.printStackTrace();
 			}
 	}
 	
   //根据用户id得到所有的帖子
   public List<PostBean> getMyPost(int userId){
	   postLists = new ArrayList<PostBean>();
		try {
			sql = "select * from post where userId = ?";       
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, userId);
		    boolean haveData = false;
			ResultSet rs = (ResultSet) statement.executeQuery();
		    while(rs.next()){
		    	String imageContent ="";
		    	int priseNum = 0;
		    	int commentNum = 0;
		    	boolean isPrised;
		    	int p_id = rs.getInt(1);
		    	String userName = rs.getString(3);
		    	String userHeadStr = Image.GetImageStr(rs.getString(4));
		    	String textContent = rs.getString(5);
		    	if(rs.getString(6)!=null)	
		         imageContent = Image.GetImageStr(rs.getString(6));    
		    	String time = rs.getString(7);
		    	//boolean relationship = getRelationship(currentUserId,userId);    //查看是否关注了用户
		    	isPrised = isPrised(p_id,userId);             //查询用户是否对这个帖子点过赞
		    	priseNum = getPriseNum(p_id);                    //得到帖子的点赞数
		    	commentNum = getCommentNum(p_id);                //得到帖子的评论数
		    	PostBean postBean = new PostBean(p_id,userId,userName,userHeadStr,textContent,imageContent,
		    			time,true,isPrised,priseNum,commentNum);
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
   
   //得到我点赞过的帖子
   public List<PostBean> getMyPrised(int id){
	   List<PostBean> list = new ArrayList<PostBean>();
	   try {
			sql = "select * from p_prise where userId = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = (ResultSet) statement.executeQuery();
			 boolean haveData = false;
			while(rs.next()){
				int priseNum = 0;
		    	int commentNum = 0;
				int p_id = rs.getInt("p_id");
				PostBean bean = getPost(p_id);
				priseNum = getPriseNum(p_id);                    //得到帖子的点赞数
		    	commentNum = getCommentNum(p_id);                //得到帖子的评论数
		    	PostBean postBean = new PostBean(p_id,bean.getUserId(),bean.getUserNameStr(),bean.getUserHeadStr(),bean.getTextContent(),
		    			bean.getImageContent(),bean.getTime(),true,true,priseNum,commentNum);
		    	list.add(postBean);
		    	haveData = true;
			}
			  rs.close();
				statement.close();
				if(haveData){
				  return list;
				}else{
					return null;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
   }
   
   //通过p_id得到帖子信息
   public PostBean getPost(int id){
	   try {
			sql = "select * from post where p_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = (ResultSet) statement.executeQuery();
			if(rs.next()){
				String imageContent ="";
				int userId = rs.getInt(2);
		    	String userName = rs.getString(3);
		    	String userHeadStr = Image.GetImageStr(rs.getString(4));
		    	String textContent = rs.getString(5);
		    	if(rs.getString(6)!=null)	
		         imageContent = Image.GetImageStr(rs.getString(6));    
		    	String time = rs.getString(7);
		    	PostBean bean = new PostBean(userId,userName,userHeadStr,textContent,imageContent,time);
		    	rs.close();
				statement.close();
		    	return bean;
			}else{
				rs.close();
				statement.close();
				return null;
			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
   }
}
