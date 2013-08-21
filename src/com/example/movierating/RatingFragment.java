package com.example.movierating;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RatingFragment extends Fragment implements OnClickListener {

	private static final String API_KEY = "apikey=nukgabadpcrm73c8qa68zksc";
	private static final String SEARCH_REQ = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=";
	private static final String SIMILAR_REQ1 = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
	private static final String SIMILAR_REQ2 = "/similar.json?";

	MyDB db;

	ArrayList<Movie> similarMovieList, seenMovieList;
	Movie currentMovie;
	TextView titleText;
	ImageView posterView;
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
		similarMovieList = new ArrayList<Movie>();
		seenMovieList = new ArrayList<Movie>();
		loadSimilarMovies();
		loadSeenMovies();
		rand = new Random();
		
		//For testing only
		for(int i =0;i<similarMovieList.size(); i++){
			Log.i("movie list test", similarMovieList.get(i).toString());
		}
		
		// Layout stuff
		ImageButton dontButton = (ImageButton) rootView.findViewById(R.id.dontbutton);
		dontButton.setOnClickListener(this);
		ImageButton likeButton = (ImageButton) rootView.findViewById(R.id.likebutton);
		likeButton.setOnClickListener(this);
		ImageButton notseenButton = (ImageButton) rootView.findViewById(R.id.notseenbutton);
		notseenButton.setOnClickListener(this);
		ImageButton searchButton = (ImageButton) rootView.findViewById(R.id.searchbutton);
		searchButton.setOnClickListener(this);
		searchTerm = (EditText) rootView.findViewById(R.id.editText1);

		titleText = (TextView) rootView.findViewById(R.id.movietitle);
		posterView = (ImageView) rootView.findViewById(R.id.image1);
		posterView.getLayoutParams().width = 173;
		posterView.getLayoutParams().height = 256;
		//posterImage.setAdjustViewBounds(true);
		//posterImage.setMaxWidth(173);
		//posterImage.setMaxHeight(256);
		if(similarMovieList.size() != 0)
			changeMovie();

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause();
		
		Log.i("TEST", "ON FRAGMENT PAUSE");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dontbutton:
			similarMovieList.remove(0);
			db.updateMovieRecord(currentMovie.getId(), 0, 1, 0);
			seenMovieList.add(currentMovie);
			changeMovie();
			break;
		case R.id.likebutton:
			similarMovieList.remove(0);
			db.updateMovieRecord(currentMovie.getId(), 0, 1, 1);
			seenMovieList.add(currentMovie);
			findSimilarMovies();
			changeMovie();
			break;
		case R.id.notseenbutton:
			similarMovieList.remove(0);
			
			Bitmap image = loadImage(currentMovie.thumburl);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			
			db.updateMovieRecord(currentMovie.getId(), 0, 0, 0, byteArray);
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
		currentMovie = similarMovieList.get(0);
		Bitmap image = loadImage(currentMovie.getImageurl());
		titleText.setText(currentMovie.getTitle() + " (" + currentMovie.getYear() + ")");
		posterView.setImageBitmap(image);
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
			similarMovieList.add(currentMovie);

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
				Movie movie = getMovieFromJSON(j);
				if(!similarMovieList.contains(movie) && !seenMovieList.contains(movie)){
					similarMovieList.add(movie);
					
					db.createMovieRecord(movie.getId(), movie.getTitle(), movie.getYear(), null, 
							movie.getImageurl(), movie.getThumburl(), movie.getSynopsis(), 
							movie.getCriticScore(), movie.getAudienceScore(), 1, 0, 0);
				}
				
				
			}
		} catch (JSONException e) {
		}
	}

	void loadSimilarMovies(){
		Cursor mCursor = db.selectMovieRecords(true, false, null);
		for (int i = 0; i < mCursor.getCount(); i++) {
			similarMovieList.add(new Movie(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), 
					mCursor.getString(4), mCursor.getString(5), mCursor.getString(6), mCursor.getInt(7), mCursor.getInt(8)));
			mCursor.moveToNext();
		}
		mCursor.close();
	}
	
	void loadSeenMovies(){
		Cursor mCursor = db.selectMovieRecords(false, true, null);
		for (int i = 0; i < mCursor.getCount(); i++) {
			seenMovieList.add(new Movie(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), 
					mCursor.getString(4), mCursor.getString(5), mCursor.getString(6), mCursor.getInt(7), mCursor.getInt(8)));
			mCursor.moveToNext();
		}
		mCursor.close();
	}
	
	Movie getMovieFromJSON(JSONObject obj) {
		Movie movie = new Movie();
		try {
			JSONObject posters = obj.getJSONObject("posters");
			String imageurl = posters.getString("detailed");
			String thumburl = posters.getString("thumbnail");
			JSONObject ratings = obj.getJSONObject("ratings");
			int criticScore = ratings.getInt("critics_score");
			int audienceScore = ratings.getInt("audience_score");
			
			movie.initMovie(obj.getString("id"), obj.getString("title"), obj.getString("year"), imageurl, thumburl,
					obj.getString("synopsis"), criticScore, audienceScore);
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
