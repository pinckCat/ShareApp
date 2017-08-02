package com.myAndroidServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myAndroidServer.database.GetConnection;
import com.myAndroidServer.database.UserDatabaseImp;
import com.myAndroidServer.entity.User;
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;
@WebServlet(name = "RegisterServlet",
urlPatterns = { "/RegisterServlet" }
)
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDatabaseImp userDatabase;
    private Connection connection;
	
   public RegisterServlet(){
	   GetConnection connectionClass = new GetConnection();
	   connection = connectionClass.getConnection();
	   userDatabase = new UserDatabaseImp(connection);
   }

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	req.setCharacterEncoding("UTF-8");
	resp.setContentType("text/html");
	resp.setCharacterEncoding("UTF-8");
	String username = req.getParameter("username");
	String password = req.getParameter("password");
	String phoneNum = req.getParameter("phoneNum");
//	username = URLDecoder.decode(username, "UTF-8");
//	password = URLDecoder.decode(password, "UTF-8");
//	phoneNum = URLDecoder.decode(phoneNum, "UTF-8");
	User user = userDatabase.findUserByName(username);
	String message = "";
	
	if(user == null){
		User newUser = new User(username,password,phoneNum);
		 userDatabase.createUser(newUser);
		 message = toJsonString("Success");
	}
	else{
		message = toJsonString("Username has been registered");
	}
	
	PrintWriter out = resp.getWriter();
	out.println(message);
	out.flush();
	out.close();
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	doGet(req, resp);
}

private String toJsonString(String status) {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("Status", status);
	return jsonObject.toString();
}
   
}
