package com.elifen.leaf.net;

import org.apache.http.client.methods.HttpPost;

public class ConnNet
{
	private static final String URLVAR="http://192.168.137.1:8080/MyAndroidServer/";

	public HttpPost gethttPost(String uripath)
	{
		HttpPost httpPost=new HttpPost(URLVAR+uripath);
		return httpPost;
	}

}
