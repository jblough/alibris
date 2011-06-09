package com.josephblough.alibris.data;

import org.json.JSONObject;

public class WorkItemOfferDetail extends ItemSearchResult {

    public String comments;
    public Integer reliability;
    
    public WorkItemOfferDetail(JSONObject json) {
	super(json);
	
	this.comments = json.optString("comments", "");
	if (json.has("reliability"));
		this.reliability = json.optInt("reliability");
    }

}
