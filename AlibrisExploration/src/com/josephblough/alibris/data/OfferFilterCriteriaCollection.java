package com.josephblough.alibris.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class OfferFilterCriteriaCollection {

    private static final String TAG = "OfferFilterCriteriaCollection";

    private static final String FILTERS_JSON_ARRAY = "filters";
    private static final String NAME_JSON_ELEMENT = "name";
    private static final String MIN_PRICE_JSON_ELEMENT = "min_price";
    private static final String MAX_PRICE_JSON_ELEMENT = "max_price";
    private static final String MIN_CONDITION_JSON_ELEMENT = "min_condition";
    private static final String MAX_CONDITION_JSON_ELEMENT = "max_condition";
    private static final String MIN_SELLER_RATING_JSON_ELEMENT = "min_seller_rating";
    private static final String SORT_ORDER_JSON_ELEMENT = "sort";
    private static final String REVERSE_SORT_ORDER_JSON_ELEMENT = "reverse_sort";
    
    private HashMap<String, OfferFilterCriteria> filters;

    public OfferFilterCriteriaCollection() {
	filters = new HashMap<String, OfferFilterCriteria>();
    }

    public OfferFilterCriteriaCollection(final String jsonString) {
	filters = new HashMap<String, OfferFilterCriteria>();
	
	try {
	    JSONObject json = new JSONObject(jsonString);
	    JSONArray jsonFilters = json.optJSONArray(FILTERS_JSON_ARRAY);
	    if (jsonFilters != null) {
		int length = jsonFilters.length();
		for (int i=0; i<length; i++) {
		    JSONObject jsonFilter = jsonFilters.getJSONObject(i);
		    String name = jsonFilter.getString(NAME_JSON_ELEMENT);
		    OfferFilterCriteria filter = new OfferFilterCriteria();
		    if (jsonFilter.has(MIN_PRICE_JSON_ELEMENT))
			filter.minPrice = jsonFilter.getDouble(MIN_PRICE_JSON_ELEMENT);
		    if (jsonFilter.has(MAX_PRICE_JSON_ELEMENT))
			filter.maxPrice = jsonFilter.getDouble(MAX_PRICE_JSON_ELEMENT);

		    if (jsonFilter.has(MIN_CONDITION_JSON_ELEMENT))
			filter.minCondition = jsonFilter.getInt(MIN_CONDITION_JSON_ELEMENT);
		    if (jsonFilter.has(MAX_CONDITION_JSON_ELEMENT))
			filter.maxCondition = jsonFilter.getInt(MAX_CONDITION_JSON_ELEMENT);

		    if (jsonFilter.has(MIN_SELLER_RATING_JSON_ELEMENT))
			filter.minSellerRating = jsonFilter.getInt(MIN_SELLER_RATING_JSON_ELEMENT);
		    
		    if (jsonFilter.has(SORT_ORDER_JSON_ELEMENT))
			filter.sort = jsonFilter.getInt(SORT_ORDER_JSON_ELEMENT);
		    if (jsonFilter.has(REVERSE_SORT_ORDER_JSON_ELEMENT))
			filter.reverseSort = jsonFilter.getBoolean(REVERSE_SORT_ORDER_JSON_ELEMENT);
		    
		    filters.put(name, filter);
		}
	    }
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }

    public Collection<String> getFilterNames() {
	return filters.keySet();
    }
    
    public OfferFilterCriteria getFilter(final String name) {
	return filters.get(name);
    }
    
    public void removeFilter(final String name) {
	filters.remove(name);
    }
    
    public void addFilter(final String name, final OfferFilterCriteria filter) {
	filters.put(name, filter);
    }

    public void addFilter(final String name, final Double minPrice, final Double maxPrice, final int minCondition, 
	    final int maxCondition, final int minSellerRating, final int sort, final boolean reverseSort) {
	final OfferFilterCriteria filter = new OfferFilterCriteria();
	filter.minPrice = minPrice;
	filter.maxPrice = maxPrice;
	filter.minCondition = minCondition;
	filter.maxCondition = maxCondition;
	filter.minSellerRating = minSellerRating;
	filter.sort = sort;
	filter.reverseSort = reverseSort;
	
	filters.put(name, filter);
    }
    
    public String toJson() {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonFilters = new JSONArray();
	    for (Entry<String, OfferFilterCriteria> entry : filters.entrySet()) {
		JSONObject jsonFilter = new JSONObject();
		jsonFilter.put(NAME_JSON_ELEMENT, entry.getKey());
		OfferFilterCriteria filter = entry.getValue();
		
		if (filter.minPrice != null)
		    jsonFilter.put(MIN_PRICE_JSON_ELEMENT, filter.minPrice);
		if (filter.maxPrice != null)
		    jsonFilter.put(MAX_PRICE_JSON_ELEMENT, filter.maxPrice);
		
		if (filter.minCondition != OfferFilterCriteria.FILTER_CONDITION_ANY)
		    jsonFilter.put(MIN_CONDITION_JSON_ELEMENT, filter.minCondition);
		if (filter.maxCondition != OfferFilterCriteria.FILTER_CONDITION_ANY)
		    jsonFilter.put(MAX_CONDITION_JSON_ELEMENT, filter.maxCondition);
		
		if (filter.minSellerRating != OfferFilterCriteria.FILTER_SELLER_RATING_ANY)
		    jsonFilter.put(MIN_SELLER_RATING_JSON_ELEMENT, filter.minSellerRating);
		
		jsonFilter.put(SORT_ORDER_JSON_ELEMENT, filter.sort);
		jsonFilter.put(REVERSE_SORT_ORDER_JSON_ELEMENT, filter.reverseSort);

		jsonFilters.put(jsonFilter);
	    }
	    json.put(FILTERS_JSON_ARRAY, jsonFilters);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return json.toString();
    }
}
