package com.tabibyab;

public class Comment {
	
	int id;
	String rating;
	String name;
	String comment;
	
	public Comment(int id, String name, String comment, String rating) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name ;
		this.comment = comment;
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
