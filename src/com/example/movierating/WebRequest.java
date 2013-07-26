package com.example.movierating;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class WebRequest extends AsyncTask<Void, Void, String> {

	String url;
	
	public WebRequest(String url){
		this.url = url;
	}
	
	@Override
	protected String doInBackground(Void... arg0) {
		String result ="";
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.addHeader("Accept", "application/json");
		//request.addHeader("deviceId", deviceId);
		HttpResponse response;
		ResponseHandler<String> handler = new BasicResponseHandler();  
	    try {  
	    	result = httpclient.execute(request, handler);
	    	//response = httpclient.execute(request);  
	    	//Log.i("Test",response.getStatusLine().toString());
	    } catch (ClientProtocolException e) {  
	    	e.printStackTrace();  
	    } catch (IOException e) {  
	    	e.printStackTrace();  
	    }  
	    httpclient.getConnectionManager().shutdown();  
	    Log.i("test", result);
		
		return result;
	}
	
	protected String onPostExectue(String s){
		return s;
	}

}
