package com.josephblough.alibris.data;

import org.json.JSONObject;

public class Review {

    /**
	Work_id - Alibris Work ID (Integer)
	Overall_rating - Average rating from all reviews (Decimal)
	Id - Alibris Review ID (Integer)
	Date - Date review was submitted (Date/time)
	Name - Name of reviewer
	Location - Location of reviewer
	Rating - Rating provided by reviewer (Integer)
	Recommend - Would reviewer recommend review (‘true’ or ‘false’)
	Body - Body of review
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
	
    }
}
