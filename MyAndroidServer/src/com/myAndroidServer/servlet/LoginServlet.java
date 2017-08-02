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
import com.myAndroidServer.util.Image;
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;

@WebServlet(name = "LoginServlet",
urlPatterns = { "/LoginServlet" }
)
public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
    private UserDatabaseImp userDatabase;
    private Connection connection;
    
    public LoginServlet(){
    	 // TODO Auto-generated constructor stub
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
		User user = userDatabase.findUserByName(username);
		String message = "";
		if (user != null&& user.getPassword().equals(password)) {
		    message = toJsonString("Success",user.getUserId(), user.getUsername(), user.getPassword(),
		    		user.getSex(), user.getPhoneNum(),Image.GetImageStr(user.getHeadName()),user.getDescription() );
		   
		} 
		else if (user != null && !user.getPassword().equals(password)) {
			message = toJsonString("Wrong password",0, "", "", "", "","","");
		} else {
			message = toJsonString("Please register your account first",0,"", "", "", "","","");
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

	//转换成json
    private static String toJsonString(String status,int userId, String userName, String password,String sex,
    		String phoneNum,String headName,String description){  
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Status", status);
        if (status.equals("Success")) {
        	jsonObject.put("UserId",userId);
        	jsonObject.put("UserName", userName);
        	jsonObject.put("Password", password);
        	jsonObject.put("Sex", sex);
        	jsonObject.put("PhoneNum", phoneNum);
        	jsonObject.put("HeadString",headName);
        	jsonObject.put("Description", description);
        }
        return jsonObject.toString();  
    }
}
