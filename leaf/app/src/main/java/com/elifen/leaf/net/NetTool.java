package com.elifen.leaf.net;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class NetTool {


    public int CODE_NUMBER=0;        //连接服务器进行交互时是否成功（放到Activity做Switch条件判断）
    private InputStream in;
    /**执行用户登录操作，接受服务器端返回的数据**/
    public String Httpconnection(String url,List<NameValuePair> params)
    {
        StringBuilder builder = null;
        ConnNet connNet=new ConnNet();     //创建连接
        try {
            @SuppressWarnings("deprecation")
            HttpEntity entity=new UrlEncodedFormEntity(params, HTTP.UTF_8);   //HttpEntity用来设置HttpPost请求参数
            HttpPost httpPost=connNet.gethttPost(url);        //申明为Post请求，请求地址为connNet返回的服务器地址

            httpPost.setEntity(entity);                       //添加请求参数
            HttpClient client=new DefaultHttpClient();
            HttpResponse httpResponse=client.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
                in = httpResponse.getEntity().getContent();                           //获取服务器响应内容
                BufferedReader br = new BufferedReader(new InputStreamReader(in));    //缓冲方式文本读取
                String line = null;
                builder = new StringBuilder();
                while ((line = br.readLine())!=null){
                    builder.append(line);
                }
                return builder.toString();                      //转成String类型，返回数据
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "InternetGG";
        }
        return "InternetGG";
    }
}