package com.josephblough.alibris.data;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class SearchResult {

    public String title;
    public String author;
    public int quantityAvailable;
    public String imageURL;
    public int workId;
    
    public float rating;
    public JSONArray reviews;
    
    protected JSONObject json;
    
    public SearchResult(JSONObject json) {
	this.json = json;
	this.json = json;
	this.title = json.optString("title");
	this.author = json.optString("author");
	this.imageURL = json.optString("imageurl");
	this.quantityAvailable = json.optInt("qty_avail", 0);
	this.workId = json.optInt("work_id", -1);
    }
    
    @Override
    public String toString() {
	return json.toString();
    }
}
