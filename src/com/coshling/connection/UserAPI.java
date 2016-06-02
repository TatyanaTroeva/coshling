package com.coshling.connection;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class UserAPI
{
	//http://coshiling.top-hub.com/signup.php?email=nikolapopov889@gmail.com&grad_year=2014
	//public static final String COSHLING_BASE_URL 				= "http://88.80.131.140:3000/" ;
	public static final String COSHLING_BASE_URL 				= "http://coshiling.top-hub.com/" ;
	public static final String COSHLING_LOGIN_URL				= COSHLING_BASE_URL + "login.php?email=%s&grad_year=%s" ;
	public static final String COSHLING_VERIFICATION_URL		= COSHLING_BASE_URL + "signup.php?email=%s&grad_year=%s" ;
	public static final String COSHLING_CARDS_URL				= COSHLING_BASE_URL + "get_all_cards.php" ;
	public static final String RESPONSE_FAIL					= "FAIL" ;
	public static final String RESPONSE_OK						= "OK" ;

	public static String getResponse(String url) {
		String response = "";
		url = url.replaceFirst(" ", "0x20");
		DefaultHttpClient hc=new DefaultHttpClient(getHttpParameters());  
		ResponseHandler <String> res=new BasicResponseHandler();  
		HttpGet httpGet=new HttpGet(url);  
		try{
			response=hc.execute(httpGet,res);
			return response;
		}catch(ConnectTimeoutException e){
		}
		catch(Exception e){
		}
		return null;
	}
	
	public static HttpParams getHttpParameters() {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        return httpParameters;
    }
}