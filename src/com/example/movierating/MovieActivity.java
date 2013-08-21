package com.example.movierating;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieActivity extends Activity {
	Movie movie;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_activity);

		Bundle extras = getIntent().getExtras();
		movie = new Movie(extras.getString("id"), extras.getString("title"),
				extras.getString("year"), extras.getString("imageurl"),
				extras.getString("thumburl"), extras.getString("synopsis"),
				extras.getInt("critic_score"), extras.getInt("aud_score"));
		
		TextView titleView = (TextView) this.findViewById(R.id.title);
		TextView synopsisView = (TextView) this.findViewById(R.id.synopsis);
		TextView criticScore = (TextView) this.findViewById(R.id.criticscore);
		TextView audienceScore = (TextView) this.findViewById(R.id.audiencescore);
		ImageView posterView = (ImageView) this.findViewById(R.id.image1);
		//ImageView criticImage = (ImageView)	this.findViewById(R.id.tomato);
		//ImageView audienceImage = (ImageView) this.findViewById(R.id.popcorn);
		
		Drawable criticImage, audienceImage;
		if(movie.getCriticScore() >= 60)
			criticImage = getResources().getDrawable(R.drawable.tomato_small);
		else
			criticImage = getResources().getDrawable(R.drawable.rotten_small);
		
		if(movie.getAudienceScore() >= 60)
			audienceImage = getResources().getDrawable(R.drawable.redpop_small);
		else
			audienceImage = getResources().getDrawable(R.drawable.greenpop_small);
		
		criticScore.setCompoundDrawablesWithIntrinsicBounds(criticImage, null, null, null);
		audienceScore.setCompoundDrawablesWithIntrinsicBounds(audienceImage, null, null, null);	
		criticScore.setCompoundDrawablePadding(5);
		audienceScore.setCompoundDrawablePadding(5);
		
		criticScore.setText(movie.getCriticScore() + "%");
		audienceScore.setText(movie.getAudienceScore() + "%");
		posterView.setImageBitmap(loadImage(movie.getImageurl()));
		titleView.setText(movie.getTitle() + " (" + movie.getYear() + ")");
		synopsisView.setText(movie.getSynopsis());
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
