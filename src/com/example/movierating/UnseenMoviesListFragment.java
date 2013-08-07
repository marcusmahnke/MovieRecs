package com.example.movierating;

import android.support.v4.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UnseenMoviesListFragment extends ListFragment {
	MyDB db;
	CustomCursorAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        db = new MyDB(this.getActivity());
		Cursor mCursor = db.selectMovieRecords(false, false, "title");
		
		adapter = new CustomCursorAdapter(this.getActivity(), mCursor, 0, true);
	    setListAdapter(adapter);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
		Cursor mCursor = db.selectMovieRecords(false, false, "title");
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
