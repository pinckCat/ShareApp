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
import com.myAndroidServer.database.UserDatabaseImp;
import com.myAndroidServer.util.Image;
import com.mysql.jdbc.Connection;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 * Servlet implementation class UploadImage
 */
@WebServlet(name = "UploadImageServlet",
urlPatterns = { "/UploadImageServlet" }
)
public class UploadImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDatabaseImp userDatabase;
	private Connection connection;
	String imgFilePath;
	    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadImageServlet() {
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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String username = request.getParameter("username");
		String imageString = request.getParameter("imageString");
	    //System.out.println(imageString);
	
		//将图片写入本地文件里
		OutputStream out = null;
		if (imageString != null){
			try {
				// Base64解码
				byte[] b = new BASE64Decoder().decodeBuffer(imageString);
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {// 调整异常数据
						b[i] += 256;
					}
				}
				// 生成jpeg图片
			    imgFilePath = "d:/AdminImages/"+username+".jpg";// 新生成的图片
				out = new FileOutputStream(imgFilePath);
				out.write(b);
				out.flush();
				String message = "";
				if(userDatabase.uploadHeadImage(username,imgFilePath)){
					   message = toJsonString("Success",Image.GetImageStr(imgFilePath));
					  
					}else{
						message = toJsonString("Fail","");
					}
					
					PrintWriter responseOut=response.getWriter();
					responseOut.write(message);
					responseOut.flush();
					responseOut.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				out.close();
			}
		}
	
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String toJsonString(String string,String headString) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Status",string);
		jsonObject.put("HeadString", headString);
		return jsonObject.toString();
	}
}
