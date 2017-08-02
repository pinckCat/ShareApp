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
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ChangeSexServlet
 */
@WebServlet(name = "ChangeSexServlet",
  urlPatterns = { "/ChangeSexServlet" })
public class ChangeSexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDatabaseImp userDatabase;
	private Connection connection;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeSexServlet() {
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
		String sex = request.getParameter("sex");
//		userName = URLDecoder.decode(userName, "UTF-8");
//		sex = URLDecoder.decode(sex, "UTF-8");
		String message = "";
		if(userDatabase.changeSex(userName, sex)){
			message = toJsonString("Success");
		}else{
			message = toJsonString("Fail");
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
