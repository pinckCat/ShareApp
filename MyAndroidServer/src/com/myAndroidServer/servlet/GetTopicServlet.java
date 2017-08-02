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
 * Servlet implementation class GetTopicServlet
 */
@WebServlet(name = "GetTopicServlet",
urlPatterns = {"/GetTopicServlet"}
		)
public class GetTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TopicDatabaseImp topicDatabaseImp;   
	private List<TopicBean> beanList;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTopicServlet() {
        super();
        // TODO Auto-generated constructor stub
        GetConnection connectionClass = new GetConnection();
        connection = connectionClass.getConnection();
        topicDatabaseImp = new TopicDatabaseImp(connection);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		beanList = topicDatabaseImp.getTopics();
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(beanList);
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

}
