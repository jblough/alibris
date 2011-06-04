package com.josephblough.alibris.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchCriteriaCollection {

    private static final String TAG = "SearchCriteriaCollection";

    private HashMap<String, SearchCriteria> searches;

    public SearchCriteriaCollection() {
	searches = new HashMap<String, SearchCriteria>();
    }

    public SearchCriteriaCollection(final String jsonString) {
	searches = new HashMap<String, SearchCriteria>();
	
	try {
	    JSONObject json = new JSONObject(jsonString);
	    JSONArray jsonSearches = json.optJSONArray("searches");
	    if (jsonSearches != null) {
		int length = jsonSearches.length();
		for (int i=0; i<length; i++) {
		    JSONObject search = jsonSearches.getJSONObject(i);
		    searches.put(search.getString("name"), 
			    new SearchCriteria(search.getString("parameter"), search.getString("searchTerm")));
		}
	    }
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }

    public Collection<String> getSearchNames() {
	return searches.keySet();
    }
    
    public SearchCriteria getSearch(final String name) {
	return searches.get(name);
    }
    
    public void removeSearch(final String name) {
	searches.remove(name);
    }
    
    public void addSearch(final String name, final String parameter, final String searchTerm) {
	SearchCriteria search = new SearchCriteria(parameter, searchTerm);
	searches.put(name, search);
    }

    public String toJson() {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, SearchCriteria> entry : searches.entrySet()) {
		JSONObject search = new JSONObject();
		search.put("name", entry.getKey());
		search.put("parameter", entry.getValue().parameter);
		search.put("searchTerm", entry.getValue().searchTerm);

		jsonSearches.put(search);
	    }
	    json.put("searches", jsonSearches);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return json.toString();
    }
}
