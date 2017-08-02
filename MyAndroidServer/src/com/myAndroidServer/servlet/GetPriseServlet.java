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
import com.myAndroidServer.entity.PostPriseBean;
import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class GetPriseServlet
 */
@WebServlet(name = "GetPriseServlet",
		urlPatterns = {"/GetPriseServlet"}
		)
public class GetPriseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PostDatabaseImp postDatabase;
	private Connection connection;   
	private PostPriseBean bean;
	private List<PostPriseBean> priseLists; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPriseServlet() {
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
		
		String p_idStr = request.getParameter("p_idStr");
		int p_id = Integer.parseInt(p_idStr);
		
		priseLists = postDatabase.getPriseInfo(p_id);
		 
		Gson gson = new Gson();
		String jsonString = gson.toJson(priseLists);
		
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
