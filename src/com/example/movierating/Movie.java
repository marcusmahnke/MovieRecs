package com.example.movierating;

public class Movie {
	String id, title, year, imageurl, thumburl, synopsis;
	int criticScore, audienceScore;
	
	Movie(String id, String title, String year, String imageurl, String thumburl, String synopsis, int criticScore, int audienceScore){
		initMovie(id, title, year, imageurl, thumburl, synopsis, criticScore, audienceScore);
	}
	
	Movie(){
		initMovie("","","","","","", -1, -1);
	}
	
	void initMovie(String id, String title, String year, String imageurl, String thumburl, 
			String synopsis, int criticScore, int audienceScore){
		this.id = id;
		this.title = title;
		this.year = year;
		this.imageurl = imageurl;
		this.thumburl = thumburl;
		this.synopsis = synopsis;
		this.audienceScore = audienceScore;
		this.criticScore = criticScore;
	}
	
	@Override
	public boolean equals(Object other){
		Movie m = (Movie) other;
		if(m.getId().equals(this.id))
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode(){
		return this.id.hashCode();
	}
	
	@Override
	public String toString(){
		return id + ": " + title + " (" + year + ")";
	}
	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public int getCriticScore() {
		return criticScore;
	}

	public void setCriticScore(int criticScore) {
		this.criticScore = criticScore;
	}

	public int getAudienceScore() {
		return audienceScore;
	}

	public void setAudienceScore(int audienceScore) {
		this.audienceScore = audienceScore;
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
	
	public String getThumburl() {
		return thumburl;
	}

	public void setThumburl(String thumburl) {
		this.thumburl = thumburl;
	}
}
