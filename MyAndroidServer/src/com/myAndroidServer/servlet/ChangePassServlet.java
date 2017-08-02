package com.myAndroidServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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

/**
 * Servlet implementation class ChangePassServlet
 */
@WebServlet(name = "ChangePassServlet",
urlPatterns ={"/ChangePassServlet"})
public class ChangePassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDatabaseImp userDatabase;
	private Connection connection;    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassServlet() {
        super();
        // TODO Auto-generated constructor stub
        GetConnection connectionClass = new GetConnection();
        connection = connectionClass.getConnection();
        userDatabase = new UserDatabaseImp(connection);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		String userName = request.getParameter("userName");
		String oldPass = request.getParameter("oldPass");
		String newPass = request.getParameter("newPass");
		
		String message = "";
		User user = userDatabase.findUserByName(userName);
		if(!oldPass.equals(user.getPassword())){
			message = toJsonString("WrongOldPass");
		}else{
			boolean result = userDatabase.updatePass(newPass, userName);
			if(result){
				message = toJsonString("Success");
			}else{
				message = toJsonString("Fail");
			}	
		}
		
		PrintWriter out = response.getWriter();
		out.write(message);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String toJsonString(String status) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Status",status);
		return jsonObject.toString();
	}
}
