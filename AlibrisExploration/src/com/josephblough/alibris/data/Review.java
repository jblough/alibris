package com.josephblough.alibris.data;

import org.json.JSONObject;

public class Review {

    /**
	id - Alibris Review ID (Integer)
	date - Date review was submitted (Date/time)
	name - Name of reviewer
	location - Location of reviewer
	rating - Rating provided by reviewer (Integer)
	recommend - Would reviewer recommend review ('true' or 'false')
	body - Body of review
     */
    
    public int id;
    public String date;
    public String name;
    public String location;
    public int rating;
    public Boolean recommends;
    public String body;
    
    protected JSONObject json;
    
    public Review(JSONObject json) {
	this.json = json;
	
	id = json.optInt("id", -1);
	date = json.optString("date", "");
	name = json.optString("name", "");
	location = json.optString("location", "");
	rating = json.optInt("rating", 0);
	if (json.has("recommends"))
	    recommends = json.optBoolean("recommends");
	body = json.optString("body", "");
    }
}
