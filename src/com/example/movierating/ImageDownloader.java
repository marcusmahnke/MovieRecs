package com.example.movierating;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageDownloader extends AsyncTask<Void, Void, Bitmap> {

	String url;
	
	public ImageDownloader(String url){
		this.url = url;
	}
	
	@Override
	protected Bitmap doInBackground(Void... arg0) {
		String result ="";
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.addHeader("Accept", "application/json");
		//request.addHeader("deviceId", deviceId);
		HttpResponse response;
		
	    try {  
	    	response = httpclient.execute(request);
	    	HttpEntity entity = response.getEntity();
	    if(entity != null){
	    	InputStream inputStream = null;
	    	try{
	    	inputStream = entity.getContent();
	    	Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	    	Log.i("Test","BITMAP SUCCESS");
	    	return bitmap;
	    	}finally{
	    		if(inputStream != null){
	    			inputStream.close();
	    		}
	    		entity.consumeContent();
	    	}
	    }
	    	
	    } catch (ClientProtocolException e) {  
	    	e.printStackTrace();  
	    } catch (IOException e) {  
	    	e.printStackTrace();  
	    }  
	    
	    
	    httpclient.getConnectionManager().shutdown();  
	    return null;
	}
	
	protected Bitmap onPostExectue(Bitmap b){
		return b;
	}

}
