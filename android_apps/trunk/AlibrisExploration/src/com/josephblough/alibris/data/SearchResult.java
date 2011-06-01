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
    
    @Override
    public String toString() {
	return json.toString();
    }
}
