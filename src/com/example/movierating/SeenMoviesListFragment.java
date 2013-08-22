package com.example.movierating;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class SeenMoviesListFragment extends ListFragment implements OnClickListener{
	MyDB db;
	CustomCursorAdapter adapter;
	ImageView alphabetSort, dateSort, criticSort, audienceSort;
	String currentSorting;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		currentSorting = "title";
		
        db = new MyDB(this.getActivity());
		Cursor mCursor = db.selectMovieRecords(false, true, "title");
		
		adapter = new CustomCursorAdapter(this.getActivity(), mCursor, 0, false);
	    setListAdapter(adapter);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
		changeList(currentSorting);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, container,
				false);
	    
        alphabetSort = (ImageView) rootView.findViewById(R.id.alphabeticalsort);
	    alphabetSort.setOnClickListener(this);
        dateSort = (ImageView) rootView.findViewById(R.id.datesort);
	    dateSort.setOnClickListener(this);
        criticSort = (ImageView) rootView.findViewById(R.id.tomatosort);
	    criticSort.setOnClickListener(this);
        audienceSort = (ImageView) rootView.findViewById(R.id.popcornsort);
	    audienceSort.setOnClickListener(this);
	    
	    criticSort.setImageResource(R.drawable.thumbup);
		audienceSort.setImageResource(R.drawable.thumbdown);
	    
	    return rootView;
    }
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.alphabeticalsort:
			alphabetSort.setImageResource(R.drawable.alphabetical_selected);
			dateSort.setImageResource(R.drawable.datesort);
			criticSort.setImageResource(R.drawable.thumbup);
			audienceSort.setImageResource(R.drawable.thumbdown);
			changeList("title");
			Log.i("test", "button press");
			break;
		case R.id.datesort:
			alphabetSort.setImageResource(R.drawable.alphabetical);
			dateSort.setImageResource(R.drawable.datesort_selected);
			criticSort.setImageResource(R.drawable.thumbup);
			audienceSort.setImageResource(R.drawable.thumbdown);
			changeList("year");
			break;
		case R.id.tomatosort:
			alphabetSort.setImageResource(R.drawable.alphabetical);
			dateSort.setImageResource(R.drawable.datesort);
			criticSort.setImageResource(R.drawable.thumbup_selected);
			audienceSort.setImageResource(R.drawable.thumbdown);
			changeList("liked desc");
			break;
		case R.id.popcornsort:
			alphabetSort.setImageResource(R.drawable.alphabetical);
			dateSort.setImageResource(R.drawable.datesort);
			criticSort.setImageResource(R.drawable.thumbup);
			audienceSort.setImageResource(R.drawable.thumbdown_selected);
			changeList("liked");
			break;
		}
		
	}

	void changeList(String orderBy){
		currentSorting = orderBy;
		
		Cursor mCursor = db.selectMovieRecords(false, true, orderBy);
		adapter.changeCursor(mCursor);
		adapter.notifyDataSetChanged();
	}
	
	@Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor mCursor = adapter.getCursor();
        mCursor.moveToPosition(position);
        
        Intent myIntent = new Intent(getActivity(), MovieActivity.class);
        myIntent.putExtra("id", mCursor.getString(0));
        myIntent.putExtra("title", mCursor.getString(1));
        myIntent.putExtra("year", mCursor.getString(2));
        myIntent.putExtra("imageurl", mCursor.getString(4));
        myIntent.putExtra("thumburl", mCursor.getString(5));
        myIntent.putExtra("synopsis", mCursor.getString(6));
        myIntent.putExtra("critic_score", mCursor.getInt(7));
        myIntent.putExtra("aud_score", mCursor.getInt(8));
        myIntent.putExtra("liked", mCursor.getInt(9));
        myIntent.putExtra("seen", 1);
		getActivity().startActivity(myIntent);
    }

}
