package com.josephblough.alibris.tasks;

import org.json.JSONObject;

import com.josephblough.alibris.transport.DataRetriever;

import android.os.AsyncTask;
import android.util.Log;

public class RecommendationRetrieverTask extends
	AsyncTask<Integer, Void, JSONObject> {

    private static final String TAG = "RecommendationRetrieverTask";
    private DataReceiver receiver;

    public RecommendationRetrieverTask(DataReceiver receiver) {
	this.receiver = receiver;
    }
    
    @Override
    protected JSONObject doInBackground(Integer... params) {
	return DataRetriever.getRecommendations(params[0]);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute");
        Log.d(TAG, result.toString());
        
        receiver.dataReceived(result);
    }
}
