package com.example.movierating;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDB {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public final static String LIKED_TABLE = "LikedMovies";
	
	public final static String MOVIE_ID = "_id";
	public final static String MOVIE_TITLE = "title";
	
	public MyDB(Context context){
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public void clearTable(String table){
		db.delete(table, null, null);
	}
	
	public long createRecords(String table, String id, String name, String year, String imageurl){  
		ContentValues values = new ContentValues();  
		values.put(MOVIE_ID, id);  
		values.put(MOVIE_TITLE, name);  
		values.put("year", year);  
		values.put("imageurl", imageurl);  
		return db.insertOrThrow(table, null, values);  
	}  
	
	
	
	public Cursor selectRecords(String table){
		String[] cols = new String[] {MOVIE_ID, MOVIE_TITLE, "year", "imageurl"};
		Cursor mCursor = db.query(true, table, cols, null, null, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
}
