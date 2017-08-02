package com.myAndroidServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.myAndroidServer.database.GetConnection;
import com.myAndroidServer.database.PostDatabaseImp;
import com.myAndroidServer.database.UserDatabaseImp;
import com.myAndroidServer.entity.PostPriseBean;
import com.myAndroidServer.entity.User;
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class PriseServlet
 */
@WebServlet(name = "PriseServlet",
  urlPatterns = { "/PriseServlet" } 
)
public class PriseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PostDatabaseImp postDatabase;
	private UserDatabaseImp userDatabase;
	private Connection connection;   
	private PostPriseBean bean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PriseServlet() {
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
		
		String p_idStr = request.getParameter("p_idStr");
		String userNameStr = request.getParameter("userNameStr");
		
	    User user = userDatabase.findUserByName(userNameStr);       //查询用户表，得到用户信息
		bean = new PostPriseBean();                                 //用来存储点赞信息
		bean.setP_id(Integer.parseInt(p_idStr));
		bean.setUserId(user.getUserId());
		bean.setUserName(userNameStr);
		bean.setHeadName(user.getHeadName());
		boolean result = postDatabase.prise(bean);                  //将点赞信息写入数据库
		String message = "";
		if(result){
			message = toJsonString("Success");
		}else {
			message = toJsonString("Fail");
		}
		
		PrintWriter responseOut=response.getWriter();
		responseOut.write(message);                                //返回处理结果（成功或者失败）
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
