package com.myAndroidServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myAndroidServer.database.GetConnection;
import com.myAndroidServer.database.PostDatabaseImp;
import com.myAndroidServer.database.UserDatabaseImp;
import com.myAndroidServer.entity.PostCommentBean;
import com.myAndroidServer.entity.User;
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class SendCommentServlet
 */
@WebServlet(name = "SendCommentServlet",
	 urlPatterns ={"/SendCommentServlet"}     
		)
public class SendCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PostDatabaseImp postDatabase;
	private UserDatabaseImp userDatabase;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
        GetConnection connectionClass = new GetConnection();
        connection = connectionClass.getConnection();
        postDatabase = new PostDatabaseImp(connection);
        userDatabase = new UserDatabaseImp(connection);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String userName = request.getParameter("userNameStr");
		String p_idStr = request.getParameter("p_idStr");
		String c_content = request.getParameter("c_content");
		int p_id  = Integer.parseInt(p_idStr);
		
		User user = userDatabase.findUserByName(userName);
		int userId = user.getUserId();
		String userHead = user.getHeadName();
		PostCommentBean bean = new PostCommentBean(p_id,userId,userHead,userName,c_content);
		boolean result = postDatabase.insertComment(bean);
		String message = "";
		if(result){
			message = toJsonString("Success");
		}else{
			message = toJsonString("Fail");
		}
		PrintWriter responseOut=response.getWriter();
		responseOut.write(message);
		responseOut.flush();
		responseOut.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String toJsonString(String string) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Status",string);
		return jsonObject.toString();
	}
 
}
