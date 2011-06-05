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

    private static final String SEARCHES_JSON_ARRAY = "searches";
    private static final String NAME_JSON_ELEMENT = "name";
    private static final String SEARCH_TERM_JSON_ELEMENT = "search_term";
    private static final String SEARCH_FIELD_JSON_ELEMENT = "field";
    private static final String SEARCH_MEDIA_JSON_ELEMENT = "media";
    private static final String SORT_ORDER_JSON_ELEMENT = "sort";
    private static final String REVERSE_SORT_ORDER_JSON_ELEMENT = "reverse_sort";
    
    private HashMap<String, SearchCriteria> searches;

    public SearchCriteriaCollection() {
	searches = new HashMap<String, SearchCriteria>();
    }

    public SearchCriteriaCollection(final String jsonString) {
	searches = new HashMap<String, SearchCriteria>();
	
	try {
	    JSONObject json = new JSONObject(jsonString);
	    JSONArray jsonSearches = json.optJSONArray(SEARCHES_JSON_ARRAY);
	    if (jsonSearches != null) {
		int length = jsonSearches.length();
		for (int i=0; i<length; i++) {
		    JSONObject jsonSearch = jsonSearches.getJSONObject(i);
		    String name = jsonSearch.getString(NAME_JSON_ELEMENT);
		    String searchTerm = jsonSearch.getString(SEARCH_TERM_JSON_ELEMENT);
		    int field = jsonSearch.optInt(SEARCH_FIELD_JSON_ELEMENT, SearchCriteria.SEARCH_ALL_INDEX);
		    SearchCriteria search = new SearchCriteria(searchTerm, field);
		    if (jsonSearch.has(SEARCH_MEDIA_JSON_ELEMENT))
			search.media = jsonSearch.getInt(SEARCH_MEDIA_JSON_ELEMENT);
		    if (jsonSearch.has(SORT_ORDER_JSON_ELEMENT))
			search.sort = jsonSearch.getInt(SORT_ORDER_JSON_ELEMENT);
		    if (jsonSearch.has(REVERSE_SORT_ORDER_JSON_ELEMENT))
			search.reverseSort = jsonSearch.getBoolean(REVERSE_SORT_ORDER_JSON_ELEMENT);
		    searches.put(name, search);
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
    
    public void addSearch(final String name, final String searchTerm, final int field) {
	SearchCriteria search = new SearchCriteria(searchTerm, field);
	searches.put(name, search);
    }

    public void addSearch(final String name, final String searchTerm, final int field, final int media, final int sort) {
	SearchCriteria search = new SearchCriteria(searchTerm, field, media, sort);
	searches.put(name, search);
    }

    public void addSearch(final String name, final String searchTerm, final int field, final int media, final int sort, final boolean reverse) {
	SearchCriteria search = new SearchCriteria(searchTerm, field, media, sort, reverse);
	searches.put(name, search);
    }

    public String toJson() {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, SearchCriteria> entry : searches.entrySet()) {
		JSONObject jsonSearch = new JSONObject();
		jsonSearch.put(NAME_JSON_ELEMENT, entry.getKey());
		SearchCriteria search = entry.getValue();
		jsonSearch.put(SEARCH_FIELD_JSON_ELEMENT, search.field);
		jsonSearch.put(SEARCH_TERM_JSON_ELEMENT, search.searchTerm);
		if (search.media != null)
		    jsonSearch.put(SEARCH_MEDIA_JSON_ELEMENT, search.media);
		if (search.sort != null)
		    jsonSearch.put(SORT_ORDER_JSON_ELEMENT, search.sort);
		if (search.reverseSort != null)
		    jsonSearch.put(REVERSE_SORT_ORDER_JSON_ELEMENT, search.reverseSort);

		jsonSearches.put(jsonSearch);
	    }
	    json.put(SEARCHES_JSON_ARRAY, jsonSearches);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return json.toString();
    }
}
