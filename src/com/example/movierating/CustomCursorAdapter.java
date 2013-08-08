package com.example.movierating;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
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
		TextView audienceScoreView = (TextView) v.findViewById(R.id.audiencescore);
		ImageView criticImage = (ImageView) v.findViewById(R.id.tomato);
		ImageView audienceImage = (ImageView) v.findViewById(R.id.popcorn);
		ImageView posterView = (ImageView) v.findViewById(R.id.poster);
		//posterView.getLayoutParams().width = 85;
		//posterView.getLayoutParams().height = 126;
		posterView.setAdjustViewBounds(true);
		posterView.setMaxWidth(61);
		posterView.setMaxHeight(91);
		String title = c.getString(1);
		String year = c.getString(2);
		
		
		if(withImages){
			int criticScore = c.getInt(7);
			int audienceScore = c.getInt(8);
			byte[] byteArray = c.getBlob(3);
			
			if(criticScore >= 60)
				criticImage.setImageResource(R.drawable.tomato);
			else
				criticImage.setImageResource(R.drawable.rotten);
			
			if(audienceScore >= 60)
				audienceImage.setImageResource(R.drawable.redpop);
			else
				audienceImage.setImageResource(R.drawable.greenpop);
			
			criticScoreView.setText(Integer.toString(criticScore) + "%");
			audienceScoreView.setText(Integer.toString(audienceScore) + "%");
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
