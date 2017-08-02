package com.myAndroidServer.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myAndroidServer.database.GetConnection;
import com.myAndroidServer.database.PostDatabaseImp;
import com.myAndroidServer.database.TopicDatabaseImp;
import com.myAndroidServer.database.UserDatabaseImp;
import com.myAndroidServer.entity.PostBean;
import com.myAndroidServer.entity.User;
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 * Servlet implementation class CreateTopicPostSevlet
 */
@WebServlet(name = "/CreateTopicPostSevlet",
urlPatterns ={"/CreateTopicPostSevlet"})
public class CreateTopicPostSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDatabaseImp userDatabase;
	private TopicDatabaseImp topicDatabase;
	private Connection connection;
      
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateTopicPostSevlet() {
        super();
        // TODO Auto-generated constructor stub
        GetConnection connectionClass = new GetConnection();
        connection = connectionClass.getConnection();
        topicDatabase = new TopicDatabaseImp(connection); 
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
		

		String userName = request.getParameter("userName");
		String textContent = request.getParameter("textContent");
		String imageStr = request.getParameter("imageStr");
		String t_idStr = request.getParameter("t_idStr");
		int t_id = Integer.parseInt(t_idStr);
		//将图片写入本地文件里
				OutputStream out = null;
				String imgFilePath = null;
				if (!imageStr.equals("")){
					try {
						// Base64解码
						byte[] b = new BASE64Decoder().decodeBuffer(imageStr);
						for (int i = 0; i < b.length; ++i) {
							if (b[i] < 0) {// 调整异常数据
								b[i] += 256;
							}
						}
						// 生成jpeg图片
					    imgFilePath = "d:/postImages/"+(System.currentTimeMillis())/1000+".jpg";// 新生成的图片
						out = new FileOutputStream(imgFilePath);
						out.write(b);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						out.close();
					}
				}
				User user =userDatabase.findUserByName(userName);
				String message = "";
				if(topicDatabase.createTopicPost(new PostBean(t_id,user.getUserId(),userName,user.getHeadName(),textContent,imgFilePath))){
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
