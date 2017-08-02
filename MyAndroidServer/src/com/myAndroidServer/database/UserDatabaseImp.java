package com.myAndroidServer.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.myAndroidServer.entity.PostPriseBean;
import com.myAndroidServer.entity.User;
import com.myAndroidServer.util.Image;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class UserDatabaseImp {
	private Connection connection;
	private String sql;
	private PreparedStatement statement;
	
	public UserDatabaseImp(Connection connection){
		this.connection = connection;
		sql = null;
	}
	
	//增加用户
	public void createUser(User user){
		try {
			sql = "insert into users (userName, password, sex, phoneNum, headName, description) values(?,?,?,?,?,?)";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getSex());
			statement.setString(4, user.getPhoneNum());
			statement.setString(5, user.getHeadName());
			statement.setString(6, user.getDescription());
			statement.executeUpdate();
			statement.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
	
	//更改昵称
	public boolean changeNickname(String userName,String nickName){
		try {
			sql = "update users set userName= ? where userName = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setString(1, nickName);
			statement.setString(2, userName);
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	//更改性别
   public boolean changeSex(String userName,String sex){
	   try {
			sql = "update users set sex= ? where userName = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setString(1, sex);
			statement.setString(2, userName);
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
   }
   public boolean changeDescription(String userName,String description){
	   try {
				sql = "update users set description= ? where userName = ?";
				statement = (PreparedStatement)connection.prepareStatement(sql);
				statement.setString(1, description);
				statement.setString(2, userName);
				statement.executeUpdate();
				statement.close();
				return true;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return false;
				}
   }
	//以用户名为条件添加头像
	public boolean uploadHeadImage(String userName,String headPath){
		try {
			sql = "update users set headName= ? where userName = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setString(1, headPath);
			statement.setString(2, userName);
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	
	//根据用户名查找用户
	public User findUserByName(String userName){
		User user = null;
		try {
			sql = "select * from users where userName=?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setString(1, userName);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
			user = new User(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
					resultSet.getString(5),resultSet.getString(6),resultSet.getString(7));
			return user;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
	}
	
	
	//根据id查找用户
	public User findUserById(int userId){
		User user = null;
		try {
			sql = "select * from users where userId=?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1, userId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()){
			user = new User(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
					resultSet.getString(5),resultSet.getString(6),resultSet.getString(7));
			return user;
			}else{
				return null;
			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
	}
	//根据昵称模糊查询
	public List<PostPriseBean> findUser(String str) {
		List<PostPriseBean> list = new ArrayList<PostPriseBean>(); 
		PostPriseBean user = null;
		try {
			sql = "select * from users where userName like '%"+str+"%'";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			boolean haveData = false;
			while(resultSet.next()){
			   user = new PostPriseBean(Image.GetImageStr(resultSet.getString("headName")),resultSet.getString("userName"), resultSet.getInt("userId"));
			   list.add(user);
			   haveData = true;
			}
		     if (haveData) {
		    	 return list;
		     } else
		    	 return null;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
	}
	//通过手机查找用户
	public User findUserByPhone(String phoneNum){
		User user = null;
		try {
			sql = "select * from users where phoneNum=?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setString(1, phoneNum);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
			user = new User(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
					resultSet.getString(5),resultSet.getString(6),resultSet.getString(7));
			return user;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
	}
	
	//查询我关注的人
	public List<PostPriseBean> getfavorite(int id){
		 List<PostPriseBean> list = new ArrayList<PostPriseBean>();
		   try {
				sql = "select * from userfriends where user_id = ?";
				statement = (PreparedStatement)connection.prepareStatement(sql);
				statement.setInt(1, id);
				ResultSet rs = (ResultSet) statement.executeQuery();
				boolean haveData = false;
				while(rs.next()){
					//String name = rs.getString("userName");
					int friend_id = rs.getInt("friend_id");
				    User user = findUserById(friend_id);
					String userHead = Image.GetImageStr(user.getHeadName());
					String userName = user.getUsername();
					PostPriseBean bean = new PostPriseBean(userHead,userName,friend_id);
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
	
	//得到粉丝信息
	public List<PostPriseBean> getFans(int id){
		List<PostPriseBean> list = new ArrayList<PostPriseBean>();
		   try {
				sql = "select * from userfriends where friend_id = ?";
				statement = (PreparedStatement)connection.prepareStatement(sql);
				statement.setInt(1, id);
				ResultSet rs = (ResultSet) statement.executeQuery();
				boolean haveData = false;
				while(rs.next()){
					//String name = rs.getString("userName");
					int user_id = rs.getInt("user_id");
				    User user = findUserById(user_id);
					String userHead = Image.GetImageStr(user.getHeadName());
					String userName = user.getUsername();
					PostPriseBean bean = new PostPriseBean(userHead,userName,user_id);
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
	
	//删除关注的人
	public boolean deleteLike(int id){
		try {
			sql = "delete from userfriends where friend_id = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setInt(1,id);
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
	//更改密码
	public boolean updatePass(String newPass,String userName){
		try {
			sql = "update users set password= ? where userName = ?";
			statement = (PreparedStatement)connection.prepareStatement(sql);
			statement.setString(1, newPass);
			statement.setString(2, userName);
			statement.executeUpdate();
			statement.close();
			return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
	}
}
