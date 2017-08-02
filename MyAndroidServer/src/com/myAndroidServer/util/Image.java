package com.myAndroidServer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Encoder;

public class Image {
	  /**将image转成base64编码**/
	   public static String GetImageStr(String picUrl) 
	   {
		   // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
			InputStream in = null;
			byte[] data = null;
			// 读取图片字节数组
			if(picUrl==null||picUrl.equals("")){
				picUrl="d:/AdminImages/a.jpg";               //选取固定图片，作为没有上传头像的管理员的默认头像
				try {
					in = new FileInputStream(picUrl);
					data = new byte[in.available()];
					in.read(data);
					in.close();
				}catch (IOException e){
					e.printStackTrace();
				}
					// 对字节数组Base64编码
					BASE64Encoder encoder = new BASE64Encoder();
				return encoder.encode(data);// 返回Base64编码过的字节数组字符串
			}
			else{
				try {
						in = new FileInputStream(picUrl);
						data = new byte[in.available()];
						in.read(data);
						in.close();
					}catch (IOException e){
						e.printStackTrace();
					}
						// 对字节数组Base64编码
						BASE64Encoder encoder = new BASE64Encoder();
					return encoder.encode(data);// 返回Base64编码过的字节数组字符串
				}
		}
}
