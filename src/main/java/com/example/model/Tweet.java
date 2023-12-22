package com.example.model;

public class Tweet {
	private String overview;
	private String postTime;
	private String content;
	private String info;
	private String hashtag;
	public Tweet(String overview, String postTime, String content, String info, String hashtag) {
		super();
		this.overview = overview;
		this.postTime = postTime;
		this.content = content;
		this.info = info;
		this.hashtag = hashtag;
	}
	public Tweet() {
		super();
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getHashtag() {
		return hashtag;
	}
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	

}
