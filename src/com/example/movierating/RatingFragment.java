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
	private static final String SIMILAR_REQ = "http://api.rottentomatoes.com/api/public/v1.0/movies/";

	MyDB db;

	ArrayList<Movie> movieList;
	Movie currentMovie;
	TextView titleText;
	ImageView posterImage;
	EditText searchTerm;

	String title, year = "";
	Bitmap bitmap = null;

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

		Cursor mCursor = db.selectRecords("SimilarMovies");
		for (int i = 0; i < mCursor.getCount(); i++) {
			String[] movieData = new String[4];
			for (int j = 0; j < mCursor.getColumnCount(); j++) {
				movieData[j] = mCursor.getString(j);
				//Log.i("db test", mCursor.getString(j));
			}
			movieList.add(new Movie(movieData[0], movieData[1], movieData[2], movieData[3]));
			mCursor.moveToNext();
		}
		mCursor.close();

		for(int i =0;i<movieList.size(); i++){
			Log.i("movie list test", movieList.get(i).toString());
		}
		rand = new Random();

		Button dontButton = (Button) rootView.findViewById(R.id.dontbutton);
		dontButton.setOnClickListener(this);
		Button likeButton = (Button) rootView.findViewById(R.id.likebutton);
		likeButton.setOnClickListener(this);
		Button searchButton = (Button) rootView.findViewById(R.id.searchbutton);
		searchButton.setOnClickListener(this);
		searchTerm = (EditText) rootView.findViewById(R.id.editText1);

		titleText = (TextView) rootView.findViewById(R.id.movietitle);
		titleText.setText(title + " (" + year + ")");

		posterImage = (ImageView) rootView.findViewById(R.id.image1);
		posterImage.setImageBitmap(bitmap);
		
		if(movieList.size() != 0)
			changeMovie();

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause();
		db.clearTable("SimilarMovies");
		Log.i("TEST", "ON FRAGMENT PAUSE");
		try {
			for (int i = 0; i < movieList.size(); i++) {
				Movie movie = movieList.get(i);
				db.createRecords("SimilarMovies", movie.getId(),
						movie.getTitle(), movie.getYear(), movie.getImageurl());
			}
		} catch (Exception e) {
		}
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
			obj = arr.getJSONObject(0);
			currentMovie = getMovieFromJSON(obj);
		} catch (JSONException e) {
		}

	}

	void findNewMovies() {
		String JSONString = "";
		try {
			WebRequest request = new WebRequest(SIMILAR_REQ
					+ currentMovie.getId() + "/similar.json?" + API_KEY);
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
				JSONObject j = arr.getJSONObject(i);
				movieList.add(getMovieFromJSON(j));
			}

		} catch (JSONException e) {
		}
	}

	void parseMovie() {

		Movie movie;
		if (currentMovie != null)
			return;
			//movie = currentMovie;
		else
			movie = movieList.get(0);
		
		currentMovie = movie;
		title = movie.getTitle();
		year = movie.getYear();
		
		String imageurl = movie.getImageurl();

		ImageDownloader id = new ImageDownloader(imageurl);
		try {
			bitmap = id.execute().get();
		} catch (ExecutionException e) {

		} catch (InterruptedException e) {

		}

	}

	void changeMovie() {
		parseMovie();
		titleText.setText(title + " (" + year + ")");
		posterImage.setImageBitmap(bitmap);
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
			try {
				db.createRecords("LikedMovies", currentMovie.getId(),
						currentMovie.getTitle(), currentMovie.getYear(), currentMovie.getImageurl());
			} catch (Exception e) {
			}
			findNewMovies();
			changeMovie();
			for (int i = 0; i < movieList.size(); i++) {
				try {
					String title = movieList.get(i).getTitle();
					Log.i("list", title);
				} catch (Exception e) {
				}
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

	Movie getMovieFromJSON(JSONObject obj) {
		Movie movie = new Movie(null, null, null, null);
		try {
			JSONObject posters = obj.getJSONObject("posters");
			String imageurl = posters.getString("detailed");
			movie = new Movie(obj.getString("id"), obj.getString("title"), obj.getString("year"),
					imageurl);
		} catch (JSONException e) {
		}

		return movie;
	}
}
