package com.example.movierating;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter{
	LayoutInflater inflater;
	boolean withImages;
	
	public CustomCursorAdapter(Context context, Cursor c, int flags, boolean withImages) {
		super(context, c, flags);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.withImages = withImages;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		TextView titleView = (TextView) v.findViewById(R.id.title);
		TextView yearView = (TextView) v.findViewById(R.id.year);
		TextView criticScoreView = (TextView) v.findViewById(R.id.criticscore);
		ImageView posterView = (ImageView) v.findViewById(R.id.poster);
		
		String title = c.getString(1);
		String year = c.getString(2);
		
		
		if(withImages){
			int criticScore = c.getInt(7);
			byte[] byteArray = c.getBlob(3);
			criticScoreView.setText(Integer.toString(criticScore) + "%");
			Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			posterView.setImageBitmap(image);
		}
		
		titleView.setText(title);
		yearView.setText(year);
		
		
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		View v = inflater.inflate(R.layout.list_item, parent, false);
        return v;
	}

}
