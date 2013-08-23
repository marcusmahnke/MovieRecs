package com.example.movierating;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "DBName";
	private static final int DATABASE_VERSION = 2;
	
	private static final String DATABASE_CREATE = "create table Movies( " +
												"_id integer primary key," +
												"title text not null, " +
												"year text not null, " +
												"imageurl text not null," +
												"thumburl text not null," +
												"image blob," +
												"synopsis text not null, " +
												"critic_score integer not null, " +
												"aud_score integer not null, " +
												"rating text not null," +
												"runtime integer not null," +
												"actors text not null," +
												"similar integer not null," +
												"seen integer not null," +
												"liked integer not null);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS Movies");
        onCreate(db);
		
	}

}
