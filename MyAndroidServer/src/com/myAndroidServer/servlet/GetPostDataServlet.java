package com.myAndroidServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.myAndroidServer.database.GetConnection;
import com.myAndroidServer.database.PostDatabaseImp;
import com.myAndroidServer.database.UserDatabaseImp;
import com.myAndroidServer.entity.PostBean;
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class GetPostDataServlet
 */
@WebServlet(name = "GetPostDataServlet",
   urlPatterns ={"/GetPostDataServlet"}
)
public class GetPostDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PostDatabaseImp postDatabase;
	private Connection connection;
	private List<PostBean> postLists; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPostDataServlet() {
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
		
		String postNum = request.getParameter("postNum");
		String currentUserId = request.getParameter("currentUserId");
		
		int num = Integer.parseInt(postNum);
		int userId = Integer.parseInt(currentUserId);
	
		postLists = postDatabase.getPostInfo(num,userId);
	    
		//利用Gson将json转换成字符串
		Gson gson = new Gson();
		String jsonString = gson.toJson(postLists);
	
		//System.out.println(jsonString);
		PrintWriter out = response.getWriter();
		out.println(jsonString);
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
	
	private String toJsonString(String string) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Status",string);
		return jsonObject.toString();
	}
	
}
