package com.josephblough.alibris.data;

import org.json.JSONObject;

public class WorkItemOfferDetail extends ItemSearchResult {

    public String comments;
    public Integer reliability;
    public String publicationDate;
    public String isbn;
    public Integer itemCondition;
    public String locationPublished;
    public String locationShippedFrom;
    public String media;
    
    public WorkItemOfferDetail(JSONObject json) {
	super(json);
	
	this.comments = json.optString("comments");
	if (json.has("reliability"))
	    this.reliability = json.optInt("reliability");
	this.publicationDate = json.optString("date_pub");
	this.isbn = json.optString("isbn");
	if (json.has("fl_bookcond"))
	    this.itemCondition = json.optInt("fl_bookcond");
	this.locationPublished = json.optString("place_pub");
	this.locationShippedFrom = json.optString("shiploc");
	this.media = json.optString("media_type");
    }

    public String getItemConditionAsString() {
	if (itemCondition != null) {
	    switch (itemCondition) {
	    case 6:
		return "New";
	    case 5:
		return "Fine / Like New";
	    case 4:
		return "Very Good";
	    case 3:
		return "Good";
	    case 2:
		return "Fair";
	    case 1:
		return "Poor";
	    }
	}
	return "";
    }
    
    public String getLocationShippedFromAsString() {
	if ("DOM".equals(locationShippedFrom)) {
	    return "Ships from the US";
	}
	else if ("INT".equals(locationShippedFrom)) {
	    return "Ships from outside the US";
	}
	else if ("STOCK".equals(locationShippedFrom)) {
	    return "Ships from Alibris' Nevada warehouse";
	}
	else if ("STOCKNEW".equals(locationShippedFrom)) {
	    return "Ships from Ingram";
	}
	else if ("".equals(locationShippedFrom)) {
	    
	}
	return null;
    }
}
