package com.myAndroidServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myAndroidServer.database.GetConnection;
import com.myAndroidServer.database.TopicDatabaseImp;
import com.myAndroidServer.entity.TopicBean;
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class CreateTopicServlet
 */
@WebServlet(name = "CreateTopicServlet",
    urlPatterns = { "/CreateTopicServlet" }
		)
public class CreateTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TopicDatabaseImp topicDatabaseImp;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateTopicServlet() {
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
		
		String createrIdStr = request.getParameter("createrIdStr");
		String title = request.getParameter("title");
		
		int id = Integer.parseInt(createrIdStr);
		TopicBean bean = new TopicBean(id,title);
		boolean result = topicDatabaseImp.createTopic(bean);
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
