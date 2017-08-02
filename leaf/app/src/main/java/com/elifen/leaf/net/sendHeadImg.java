package com.elifen.leaf.net;

import android.graphics.Bitmap;

import com.elifen.leaf.entity.CurrentUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 上传图片
 * Created by Elifen on 2017/4/13.
 */

    public class sendHeadImg {
        private final String TAG = "sendHeadImg";
    public CurrentUser currentUser;
    public String state;
    public String headPicture;
    public Bitmap image;

    /**执行用户登录操作，接受服务器端返回的数据**/
    public String userLogin(String url,String username,String password)
    {
        String result = null;
        String ResponseFrom=null;
        ConnNet connNet=new ConnNet();        //创建连接
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("imageString", password));

        try {
            @SuppressWarnings("deprecation")
            HttpEntity entity=new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost httpPost=connNet.gethttPost(url);

            httpPost.setEntity(entity);
            HttpClient client=new DefaultHttpClient();
            HttpResponse httpResponse=client.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK)
            {
                ResponseFrom= EntityUtils.toString(httpResponse.getEntity(),"utf-8");//获得服务器响应返回字符

                if(ResponseFrom.equals("failed")){
                    result="用户名或密码错误！";
                }else{
                    result="成功";
                }
            }
            else if(httpResponse.getStatusLine().getStatusCode()!=HttpStatus.SC_OK)
            {
                result="失败";
            }
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (ParseException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return result;
    }
}
