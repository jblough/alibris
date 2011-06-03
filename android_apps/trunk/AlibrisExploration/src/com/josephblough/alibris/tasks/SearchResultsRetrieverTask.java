package com.josephblough.alibris.tasks;

import java.util.Map;

import org.json.JSONObject;

import com.josephblough.alibris.transport.DataRetriever;

import android.os.AsyncTask;
import android.util.Log;

public class SearchResultsRetrieverTask extends
	AsyncTask<Map<String, String>, Void, JSONObject> {

    private static final String TAG = "SearchResultsRetrieverTask";
    
    private DataReceiver receiver;
    public SearchResultsRetrieverTask(final DataReceiver receiver) {
	this.receiver = receiver;
    }
    
    @Override
    protected JSONObject doInBackground(Map<String, String>... params) {
	return DataRetriever.search(params[0]);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute");
        Log.d(TAG, result.toString());
        
        this.receiver.dataReceived(result);
    }
}
