package com.josephblough.alibris.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.data.ItemSearchResult;
import com.josephblough.alibris.data.WorkSearchResult;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class WorkOfferDetailActivity extends Activity {

    private static final String TAG = "WorkOfferDetailActivity";
    
    public static final String WORK_AS_JSON = "WorkOfferDetailActivity.work_as_json";

    private ItemSearchResult offer;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        final String json = getIntent().getStringExtra(WORK_AS_JSON);
        if (json != null) {
            try {
        	offer = new ItemSearchResult(new JSONObject(json));
        	Log.d(TAG, "Read in offer " + offer.sku);
            }
            catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
            }
        }
    }
}
