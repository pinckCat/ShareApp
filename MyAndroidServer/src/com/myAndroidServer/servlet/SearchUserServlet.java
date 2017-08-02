package com.myAndroidServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.myAndroidServer.database.GetConnection;
import com.myAndroidServer.database.UserDatabaseImp;
import com.myAndroidServer.entity.PostPriseBean;
import com.myAndroidServer.entity.User;
import com.myAndroidServer.util.Image;
import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class SearchUserServlet
 */
@WebServlet(name = "SearchUserServlet",
urlPatterns ={"/SearchUserServlet"})
public class SearchUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDatabaseImp userDatabaseImp;
	private Connection connection; 
	private List<PostPriseBean> users;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchUserServlet() {
        super();
        // TODO Auto-generated constructor stub
        GetConnection connectionClass = new GetConnection();
        connection = connectionClass.getConnection();
        userDatabaseImp = new UserDatabaseImp(connection);
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String str = request.getParameter("str");
		users = new ArrayList<PostPriseBean>();
		boolean haveData = false;
		 if(isNumeric(str)){
				int id =  Integer.parseInt(str);
				User user = userDatabaseImp.findUserById(id);
				if(user != null){
					PostPriseBean bean = new PostPriseBean(Image.GetImageStr(user.getHeadName()),user.getUsername(),user.getUserId());
					users.add(bean);
					haveData = true;
				}
			}
		    List<PostPriseBean> newList = userDatabaseImp.findUser(str);
		    if(newList != null){
		       haveData = true;
		       for(int i=0;i<newList.size();i++){
			          users.add(newList.get(i));
		        }
		     }
		    if(!haveData){
		    	users = null;
		    }
		    //利用Gson将json转换成字符串
	  		Gson gson = new Gson();
	  		String jsonString = gson.toJson(users);
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
	
  //判断是否为数字
  public static boolean isNumeric(String str) {
	    for (int i = str.length(); --i >= 0;) {
	       if (!Character.isDigit(str.charAt(i))) {
	           return false;
	           }
	        }
	        return true;
	    }
}
