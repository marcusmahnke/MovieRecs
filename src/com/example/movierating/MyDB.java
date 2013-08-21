package com.example.movierating;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDB {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public final static String MOVIE_ID = "_id";
	public final static String MOVIE_TITLE = "title";
	
	public MyDB(Context context){
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public long createMovieRecord(String id, String name, String year, byte[] image, String imageurl,
			String thumburl, String synopsis, int criticScore, int audienceScore, int similar, int seen, int liked){  
		ContentValues values = new ContentValues();  
		values.put(MOVIE_ID, id);  
		values.put(MOVIE_TITLE, name);  
		values.put("year", year); 
		values.put("image", image);
		values.put("imageurl", imageurl);
		values.put("thumburl", thumburl);
		values.put("synopsis", synopsis);
		values.put("critic_score", criticScore);
		values.put("aud_score", audienceScore);
		values.put("similar", similar);
		values.put("seen", seen);
		values.put("liked", liked);
		return db.insertWithOnConflict("Movies", null, values, SQLiteDatabase.CONFLICT_IGNORE);  
	} 
	
	public Cursor selectMovieRecords(boolean similar, boolean seen, String orderBy){
		String selection;
		if(similar)
			selection = "similar = 1";
		else if(seen)
			selection = "seen = 1";
		else
			selection = "seen = 0 and similar = 0";
		
		String[] cols = new String[] {MOVIE_ID, MOVIE_TITLE, "year", "image", "imageurl", "thumburl",
				"synopsis", "critic_score", "aud_score", "liked"};

		Cursor mCursor = db.query(true, "Movies", cols, selection, null, null, null, orderBy, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	public long updateMovieRecord(String id, int similar, int seen, int liked){
		ContentValues values = new ContentValues();
		values.put("seen", seen);
		values.put("similar", similar);
		values.put("liked", liked);
		return db.updateWithOnConflict("Movies", values, "_id ='" + id +"'", null, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public long updateMovieRecord(String id, int similar, int seen, int liked, byte[] image){
		ContentValues values = new ContentValues();
		values.put("seen", seen);
		values.put("similar", similar);
		values.put("liked", liked);
		values.put("image", image);
		return db.updateWithOnConflict("Movies", values, "_id ='" + id +"'", null, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
}
