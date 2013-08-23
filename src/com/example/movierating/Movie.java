package com.example.movierating;

import java.util.List;

public class Movie {
	String[] castArray;
	String id, title, year, imageurl, thumburl, synopsis, rating, consensus;
	int criticScore, audienceScore, runtime;
	
	Movie(String id, String title, String year, String imageurl, String thumburl, String synopsis, int criticScore, int audienceScore,
			String rating, int runtime, String[] cast, String consensus){
		initMovie(id, title, year, imageurl, thumburl, synopsis, criticScore, audienceScore, rating, runtime, cast, consensus);
	}
	
	Movie(){
		initMovie("","","","","","", -1, -1, "", -1, null, "");
	}
	
	void initMovie(String id, String title, String year, String imageurl, String thumburl, 
			String synopsis, int criticScore, int audienceScore,
			String rating, int runtime, String[] cast, String consensus){
		this.id = id;
		this.title = title;
		this.year = year;
		this.imageurl = imageurl;
		this.thumburl = thumburl;
		this.synopsis = synopsis;
		this.audienceScore = audienceScore;
		this.criticScore = criticScore;
		this.rating = rating;
		this.runtime = runtime;
		this.castArray = cast;
		this.consensus = consensus;
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
	
	public String getCastString() {
		String c = "";
		if(castArray!=null){
			for(int i=0; i<castArray.length; i++){
				c += castArray[i];
				if(i!=castArray.length-1)
					c+=",";
			}
		}
		return c;
	}
	
	public String getCastStringFormatted() {
		String c = "";
		for(int i=0; i<castArray.length; i++){
			c += castArray[i];
			if(i!=castArray.length-1)
				c+=", ";
		}
		return c;
	}
	
	public String[] getCastArray() {
		return castArray;
	}

	public void setCastArray(String[] castArray) {
		this.castArray = castArray;
	}

	public String getConsensus() {
		return consensus;
	}

	public void setConsensus(String consensus) {
		this.consensus = consensus;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
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
