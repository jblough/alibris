package com.josephblough.alibris.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;

import android.util.Log;


public class DataRetriever {

    private static final String TAG = "DataRetriever";
    
    private static final String API_KEY = "";
    public static final String SHARED_SECRET = "";
    private static final String SEARCH_PARAMETER_REPLACEMENT_STRING = "%SEARCH_PARAMS%";
    private static final String WORK_ID_PARAMETER_REPLACEMENT_STRING = "%WORK_ID_PARAMS%";
    
    private static final String SEARCH_URL = "http://api.alibris.com/v1/public/search?" + SEARCH_PARAMETER_REPLACEMENT_STRING + "&apikey=" + API_KEY + "&outputtype=json";
    private static final String RECOMMEND_URL = "http://api.alibris.com/v1/public/recommend?work=" + WORK_ID_PARAMETER_REPLACEMENT_STRING + "&apikey=" + API_KEY + "&outputtype=json";
    private static final String REVIEW_URL = "http://api.alibris.com/v1/public/review?work=" + WORK_ID_PARAMETER_REPLACEMENT_STRING + "&apikey=" + API_KEY + "&outputtype=json";
    
    public static JSONObject search(final Map<String, String> searchTerms) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    final String url = SEARCH_URL.replaceAll(SEARCH_PARAMETER_REPLACEMENT_STRING, createSearchString(searchTerms));
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    return new JSONObject(response);
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static JSONObject getReviews(final Integer work) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    final String url = REVIEW_URL.replaceAll(WORK_ID_PARAMETER_REPLACEMENT_STRING, work.toString());
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    return new JSONObject(response);
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static JSONObject getRecommendations(final Integer work) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    final String url = RECOMMEND_URL.replaceAll(WORK_ID_PARAMETER_REPLACEMENT_STRING, work.toString());
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    return new JSONObject(response);
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    private static String createSearchString(final Map<String, String> parameters) {
	List< NameValuePair > pairs = new ArrayList< NameValuePair >();
	for (Entry<String, String> entry : parameters.entrySet()) {
	    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	}
	return URLEncodedUtils.format(pairs, "ISO-8859-1");
    }
}
