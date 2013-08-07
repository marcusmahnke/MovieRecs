package com.example.movierating;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SeenMoviesListFragment extends ListFragment{
	MyDB db;
	CustomCursorAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        db = new MyDB(this.getActivity());
		Cursor mCursor = db.selectMovieRecords(false, true, "title");
		
		adapter = new CustomCursorAdapter(this.getActivity(), mCursor, 0, false);
	    setListAdapter(adapter);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
		Cursor mCursor = db.selectMovieRecords(false, true, "title");
		adapter.changeCursor(mCursor);
		adapter.notifyDataSetChanged();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, container,
				false);
	    
	    return rootView;
    }

}
