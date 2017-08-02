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
import com.myAndroidServer.database.TopicDatabaseImp;
import com.myAndroidServer.entity.TopicBean;
import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class GetMyPostServlet
 */
@WebServlet(name="GetMyTopicServlet",
urlPatterns = {"/GetMyTopicServlet"})
public class GetMyTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TopicDatabaseImp topicDatabase;
	private Connection connection;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMyTopicServlet() {
        super();
        // TODO Auto-generated constructor stub
        GetConnection connectionClass = new GetConnection();
        connection = connectionClass.getConnection();
        topicDatabase = new TopicDatabaseImp(connection); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String currentUserIdStr = request.getParameter("currentUserId");
		int currentUserId = Integer.parseInt(currentUserIdStr);
		List<TopicBean> list = topicDatabase.getMyTopic(currentUserId);
		
		//利用Gson将json转换成字符串
		Gson gson = new Gson();
		String jsonString = gson.toJson(list);

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

}
