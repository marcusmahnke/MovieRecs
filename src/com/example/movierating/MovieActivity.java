package com.example.movierating;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieActivity extends Activity implements OnClickListener{
	Movie movie;
	MyDB db;
	int liked, seen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_activity);

		db = new MyDB(this);
		
		Bundle extras = getIntent().getExtras();
		movie = new Movie(extras.getString("id"), extras.getString("title"),
				extras.getString("year"), extras.getString("imageurl"),
				extras.getString("thumburl"), extras.getString("synopsis"),
				extras.getInt("critic_score"), extras.getInt("aud_score"));
		liked = extras.getInt("liked");
		seen = extras.getInt("seen");
		
		TextView titleView = (TextView) this.findViewById(R.id.title);
		TextView synopsisView = (TextView) this.findViewById(R.id.synopsis);
		TextView criticScore = (TextView) this.findViewById(R.id.criticscore);
		TextView audienceScore = (TextView) this.findViewById(R.id.audiencescore);
		ImageView posterView = (ImageView) this.findViewById(R.id.image1);
		ImageButton dontButton = (ImageButton) this.findViewById(R.id.dontbutton);
		dontButton.setOnClickListener(this);
		ImageButton likeButton = (ImageButton) this.findViewById(R.id.likebutton);
		likeButton.setOnClickListener(this);
		ImageButton notseenButton = (ImageButton) this.findViewById(R.id.notseenbutton);
		notseenButton.setOnClickListener(this);
		
		if(seen==1){
			if(liked==1)
				likeButton.setImageResource(R.drawable.rating_good_selected);
			else
				dontButton.setImageResource(R.drawable.rating_bad_selected);
		}
		else{
			notseenButton.setVisibility(View.GONE);
		}
		
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
		Log.i("synopsis test", "test:" + movie.getSynopsis());
		if(movie.getSynopsis().trim().equals(""))
			synopsisView.setText("Unavailable");
		else
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

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.likebutton:
			db.updateMovieRecord(movie.getId(), 0, 1, 1);
			finish();
			break;
		case R.id.dontbutton:
			db.updateMovieRecord(movie.getId(), 0, 1, 0);
			finish();
			break;
		case R.id.notseenbutton:
			if(seen==1){
				db.updateMovieRecord(movie.getId(), 0, 0, 0);
				finish();
			}
			break;
		}
	}

}
