package com.example.movierating;

import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class UnseenMoviesListFragment extends ListFragment implements OnClickListener{
	MyDB db;
	CustomCursorAdapter adapter;
	ImageView alphabetSort, dateSort, criticSort, audienceSort;
	String currentSorting;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		currentSorting = "title";
		
        db = new MyDB(this.getActivity());
		Cursor mCursor = db.selectMovieRecords(false, false, "title");
		
		adapter = new CustomCursorAdapter(this.getActivity(), mCursor, 0, true);
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
	    
	    return rootView;
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.alphabeticalsort:
			alphabetSort.setImageResource(R.drawable.alphabetical_selected);
			dateSort.setImageResource(R.drawable.datesort);
			criticSort.setImageResource(R.drawable.tomato);
			audienceSort.setImageResource(R.drawable.redpop);
			changeList("title");
			break;
		case R.id.datesort:
			alphabetSort.setImageResource(R.drawable.alphabetical);
			dateSort.setImageResource(R.drawable.datesort_selected);
			criticSort.setImageResource(R.drawable.tomato);
			audienceSort.setImageResource(R.drawable.redpop);
			changeList("year desc");
			break;
		case R.id.tomatosort:
			alphabetSort.setImageResource(R.drawable.alphabetical);
			dateSort.setImageResource(R.drawable.datesort);
			criticSort.setImageResource(R.drawable.tomato_selected);
			audienceSort.setImageResource(R.drawable.redpop);
			changeList("critic_score desc");
			break;
		case R.id.popcornsort:
			alphabetSort.setImageResource(R.drawable.alphabetical);
			dateSort.setImageResource(R.drawable.datesort);
			criticSort.setImageResource(R.drawable.tomato);
			audienceSort.setImageResource(R.drawable.redpop_selected);
			changeList("aud_score desc");
			break;
		}
		
	}

	void changeList(String orderBy){
		currentSorting = orderBy;
		
		Cursor mCursor = db.selectMovieRecords(false, false, orderBy);
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
        myIntent.putExtra("rating", mCursor.getString(9));
        myIntent.putExtra("runtime", mCursor.getInt(10));
        myIntent.putExtra("cast", mCursor.getString(11));   
        myIntent.putExtra("seen", 0);
		getActivity().startActivity(myIntent);
    }

}
