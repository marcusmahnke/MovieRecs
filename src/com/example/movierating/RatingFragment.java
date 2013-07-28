package com.example.movierating;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
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
	private static final String SIMILAR_REQ1 = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
	private static final String SIMILAR_REQ2 = "/similar.json?";

	MyDB db;

	ArrayList<Movie> movieList;
	Movie currentMovie;
	TextView titleText;
	ImageView posterImage;
	EditText searchTerm;

	Random rand;

	public RatingFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.rating_fragment, container,
				false);
		db = new MyDB(this.getActivity());
		movieList = new ArrayList<Movie>();
		loadSimilarMovies();
		rand = new Random();
		
		//For testing only
		for(int i =0;i<movieList.size(); i++){
			Log.i("movie list test", movieList.get(i).toString());
		}
		
		// Layout stuff
		Button dontButton = (Button) rootView.findViewById(R.id.dontbutton);
		dontButton.setOnClickListener(this);
		Button likeButton = (Button) rootView.findViewById(R.id.likebutton);
		likeButton.setOnClickListener(this);
		Button searchButton = (Button) rootView.findViewById(R.id.searchbutton);
		searchButton.setOnClickListener(this);
		searchTerm = (EditText) rootView.findViewById(R.id.editText1);

		titleText = (TextView) rootView.findViewById(R.id.movietitle);
		posterImage = (ImageView) rootView.findViewById(R.id.image1);
		
		if(movieList.size() != 0)
			changeMovie();

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause();
		saveSimilarMovies();
		
		Log.i("TEST", "ON FRAGMENT PAUSE");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dontbutton:
			movieList.remove(0);
			changeMovie();
			break;
		case R.id.likebutton:
			movieList.remove(0);
			db.createRecords("LikedMovies", currentMovie.getId(),
					currentMovie.getTitle(), currentMovie.getYear(), currentMovie.getImageurl());
			findSimilarMovies();
			changeMovie();
			break;
		case R.id.searchbutton:
			String searchQuery = Uri.encode(searchTerm.getText().toString());
			findMovie(searchQuery);
			changeMovie();
			searchTerm.setText("");
			break;
		}

	}
	
	void changeMovie() {
		currentMovie = movieList.get(0);
		Bitmap image = loadImage(currentMovie.getImageurl());
		titleText.setText(currentMovie.getTitle() + " (" + currentMovie.getYear() + ")");
		posterImage.setImageBitmap(image);
	}
	
	void findMovie(String searchQuery) {
		String JSONString = "";

		WebRequest request = new WebRequest(SEARCH_REQ + searchQuery + "&"
				+ API_KEY);
		try {
			JSONString = request.execute().get();
		} catch (Exception e) {
		}

		JSONArray arr = new JSONArray();
		try {
			JSONObject obj = new JSONObject(JSONString);
			arr = obj.getJSONArray("movies");
			obj = arr.getJSONObject(0);
			currentMovie = getMovieFromJSON(obj);
		} catch (JSONException e) {
		}

	}

	void findSimilarMovies() {
		String JSONString = "";
		
		WebRequest request = new WebRequest(SIMILAR_REQ1
				+ currentMovie.getId() + SIMILAR_REQ2 + API_KEY);
		try {
			JSONString = request.execute().get();
		} catch (Exception e) {	
		}

		JSONArray arr = new JSONArray();	
		try {
			JSONObject obj = new JSONObject(JSONString);
			arr = obj.getJSONArray("movies");

			for (int i = 0; i < arr.length(); i++) {
				JSONObject j = arr.getJSONObject(i);
				movieList.add(getMovieFromJSON(j));
			}

		} catch (JSONException e) {
		}
	}

	void loadSimilarMovies(){
		Cursor mCursor = db.selectRecords("SimilarMovies");
		for (int i = 0; i < mCursor.getCount(); i++) {
			String[] movieData = new String[4];
			for (int j = 0; j < mCursor.getColumnCount(); j++) {
				movieData[j] = mCursor.getString(j);
			}
			movieList.add(new Movie(movieData[0], movieData[1], movieData[2], movieData[3]));
			mCursor.moveToNext();
		}
		mCursor.close();
	}
	
	void saveSimilarMovies(){
		db.clearTable("SimilarMovies");
		
		for (int i = 0; i < movieList.size(); i++) {
			Movie movie = movieList.get(i);
			db.createRecords("SimilarMovies", movie.getId(),
					movie.getTitle(), movie.getYear(), movie.getImageurl());
		}
	}
	
	Movie getMovieFromJSON(JSONObject obj) {
		Movie movie = new Movie();
		try {
			JSONObject posters = obj.getJSONObject("posters");
			String imageurl = posters.getString("detailed");
			movie.initMovie(obj.getString("id"), obj.getString("title"), obj.getString("year"), imageurl);
		} catch (JSONException e) {
		}

		return movie;
	}
	
	Bitmap loadImage(String imageurl){
		Bitmap image = null;
		ImageDownloader id = new ImageDownloader(imageurl);
		try {
			image = id.execute().get();
		} catch (Exception e) {
		}
		
		return image;
	}
}
