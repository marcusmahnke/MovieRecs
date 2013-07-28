package com.example.movierating;

public class Movie {
	String id, title, year, imageurl;
	
	Movie(String id, String title, String year, String imageurl){
		initMovie(id, title, year, imageurl);
	}
	
	Movie(){
		initMovie("","","","");
	}
	
	void initMovie(String id, String title, String year, String imageurl){
		this.id = id;
		this.title = title;
		this.year = year;
		this.imageurl = imageurl;
	}
	
	public String toString(){
		return id + ": " + title + " (" + year + ")";
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
}
