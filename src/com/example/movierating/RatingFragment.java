package com.example.movierating;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RatingFragment extends Fragment implements OnClickListener {

	private static final String API_KEY = "apikey=nukgabadpcrm73c8qa68zksc";
	private static final String SEARCH_REQ = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=";
	private static final String SIMILAR_REQ = "http://api.rottentomatoes.com/api/public/v1.0/movies/";

	ArrayList<JSONObject> movieList;
	JSONObject currentMovie;
	TextView titleText;
	ImageView posterImage;
	EditText searchTerm;

	String title, releaseDate = "";
	Bitmap bitmap = null;

	Random rand;

	public RatingFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rating_fragment, container,
				false);
		movieList = new ArrayList<JSONObject>();
		rand = new Random();

		Button dontButton = (Button) rootView.findViewById(R.id.dontbutton);
		dontButton.setOnClickListener(this);
		Button likeButton = (Button) rootView.findViewById(R.id.likebutton);
		likeButton.setOnClickListener(this);
		Button searchButton = (Button) rootView.findViewById(R.id.searchbutton);
		searchButton.setOnClickListener(this);
		searchTerm = (EditText) rootView.findViewById(R.id.editText1);

		// findNewMovies();

		titleText = (TextView) rootView.findViewById(R.id.movietitle);
		titleText.setText(title + " (" + releaseDate + ")");

		posterImage = (ImageView) rootView.findViewById(R.id.image1);
		posterImage.setImageBitmap(bitmap);

		return rootView;
	}

	void findMovie(String searchQuery) {
		String JSONString = "";

		WebRequest request = new WebRequest(SEARCH_REQ + searchQuery + "&"
				+ API_KEY);
		try {
			JSONString = request.execute().get();
		} catch (ExecutionException e) {

		} catch (InterruptedException e) {

		}

		JSONArray arr = new JSONArray();
		try {
			JSONObject obj = new JSONObject(JSONString);
			arr = obj.getJSONArray("movies");
		} catch (JSONException e) {
		}

		try {
			currentMovie = arr.getJSONObject(0);
		} catch (JSONException e) {
		}
	}

	void findNewMovies() {
		String JSONString = "";
		try {
			WebRequest request = new WebRequest(SIMILAR_REQ
					+ currentMovie.getString("id") + "/similar.json?" + API_KEY);
			currentMovie = null;
			JSONString = request.execute().get();
		} catch (Exception e) {

		}

		JSONArray arr = new JSONArray();
		try {
			JSONObject obj = new JSONObject(JSONString);
			arr = obj.getJSONArray("movies");
		} catch (JSONException e) {
		}

		try {
			for (int i = 0; i < arr.length(); i++) {
				movieList.add(arr.getJSONObject(i));
			}

		} catch (JSONException e) {
		}
	}

	void parseMovie() {

		try {
			JSONObject obj;
			if (currentMovie != null)
				obj = currentMovie;
			else
				obj = movieList.remove(0);
			currentMovie = obj;
			title = obj.getString("title");
			releaseDate = obj.getString("year");

			JSONObject posters = obj.getJSONObject("posters");
			String imageurl = posters.getString("detailed");

			ImageDownloader id = new ImageDownloader(imageurl);
			try {
				bitmap = id.execute().get();
			} catch (ExecutionException e) {

			} catch (InterruptedException e) {

			}
		} catch (JSONException e) {
		}

	}

	void changeMovie() {
		parseMovie();
		titleText.setText(title + " (" + releaseDate + ")");
		posterImage.setImageBitmap(bitmap);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dontbutton:
			changeMovie();
			break;
		case R.id.likebutton:
			findNewMovies();
			changeMovie();
			for(int i=0;i< movieList.size(); i++){
				try{
				String title = movieList.get(i).getString("title");
				Log.i("list", title);
				}catch(Exception e){}
			}
			break;
		case R.id.searchbutton:
			String searchQuery = Uri.encode(searchTerm.getText().toString());
			findMovie(searchQuery);
			changeMovie();
			searchTerm.setText("");
			break;
		}

	}
}
