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
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class AddFriendServlet
 */
@WebServlet(name ="AddFriendServlet",
  urlPatterns ={"/AddFriendServlet"}
		)
public class AddFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PostDatabaseImp postDatabase;
	private Connection connection;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddFriendServlet() {
        super();
        // TODO Auto-generated constructor stub
        GetConnection connectionClass = new GetConnection();
        connection = connectionClass.getConnection();
        postDatabase = new PostDatabaseImp(connection); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String userIdStr = request.getParameter("userIdStr");
		String friendIdStr = request.getParameter("friendIdStr");
		
		int userId = Integer.parseInt(userIdStr);
		int friendId = Integer.parseInt(friendIdStr);
		
		System.out.println("userId:"+userId+" friendsId:"+friendId);
		boolean isFriend = postDatabase.getRelationship(userId, friendId);
		String message = "";
		if(isFriend){
			message = toJsonString("Already concerned");
		}else{
			boolean result = postDatabase.addFriend(userId, friendId);
			if(result){
				message = toJsonString("Success");
			}else{
				message = toJsonString("Fail");
			}
			
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
