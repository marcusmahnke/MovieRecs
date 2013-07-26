package com.example.movierating;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RatingFragment extends Fragment {

	private static final String IMAGE_SIZE = "w342";
	private static final String POPULAR_REQ = "http://api.themoviedb.org/3/movie/popular?api_key=0ed5fe01841e0f1796c7a9a24357999b";

	String title, releaseDate ="";
	Bitmap bitmap = null;
	
	public RatingFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rating_fragment, container,
				false);
		
		findNewMovie();
		
		TextView textView = (TextView) rootView
				.findViewById(R.id.movietitle);
		textView.setText(title + " (" + releaseDate + ")");
		
		ImageView imageView = (ImageView) rootView
				.findViewById(R.id.image1);
		imageView.setImageBitmap(bitmap);
		
		return rootView;
	}

	void findNewMovie() {
		String JSONString = "";

		WebRequest request = new WebRequest(POPULAR_REQ);
		try {
			JSONString = request.execute().get();
		} catch (ExecutionException e) {

		} catch (InterruptedException e) {

		}

		String image_path = "";
		//title = "";
		//releaseDate = "";

		JSONArray arr = new JSONArray();
		try {
			JSONObject obj = new JSONObject(JSONString);
			arr = obj.getJSONArray("results");
		} catch (JSONException e) {
		}

		try {
			JSONObject obj = arr.getJSONObject(0);
			title = obj.getString("title");
			releaseDate = obj.getString("release_date");
			image_path = obj.getString("poster_path");
			releaseDate = releaseDate.substring(0, 4);

			//bitmap = null;
			String imageurl = "http://d3gtl9l2a4fn1j.cloudfront.net/t/p/"
					+ IMAGE_SIZE + "/" + image_path
					+ "?api_key=0ed5fe01841e0f1796c7a9a24357999b";

			ImageDownloader id = new ImageDownloader(imageurl);
			try {
				bitmap = id.execute().get();
			} catch (ExecutionException e) {

			} catch (InterruptedException e) {

			}

		} catch (JSONException e) {
		}
	}
}
