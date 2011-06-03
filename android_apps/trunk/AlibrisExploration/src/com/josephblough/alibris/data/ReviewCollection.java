package com.josephblough.alibris.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReviewCollection {

    /**
	work_id - Alibris Work ID (Integer)
	overall_rating - Average rating from all reviews (Decimal)
	review - array of 
		id - Alibris Review ID (Integer)
		date - Date review was submitted (Date/time)
		name - Name of reviewer
		location - Location of reviewer
		rating - Rating provided by reviewer (Integer)
		recommend - Would reviewer recommend review ('true' or 'false')
		body - Body of review
     */
    
    public int workId;
    public double overallRating;
    private List<Review> reviews;
    
    protected JSONObject json;
    
    public ReviewCollection(JSONObject json) {
	this.json = json;
	
	workId = json.optInt("work_id", -1);
	overallRating = json.optDouble("overall_rating", 0.0);
    }
    
    public List<Review> getReviews() {
	if (reviews == null) {
	    reviews = new ArrayList<Review>();
	    JSONArray jsonReviews = json.optJSONArray("review");
	    if (jsonReviews != null) {
		int length = jsonReviews.length();
		for (int i=0; i<length; i++) {
		    reviews.add(new Review(jsonReviews.optJSONObject(i)));
		}
	    }
	}
	return reviews;
    }
}
