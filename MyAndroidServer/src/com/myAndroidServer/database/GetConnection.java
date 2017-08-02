package com.myAndroidServer.database;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class GetConnection {
	public Connection getConnection() {
    	Connection con = null;
    	try {
    	Class.forName("com.mysql.jdbc.Driver");
        // 连接数据库
    	
        con = (Connection)DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/android?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8", "root", "123456"); // 创建语句对象

    } catch (Exception e) {
    	
        e.printStackTrace();
    }
    	
    return con;
    }
}
