package com.josephblough.alibris.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.adapters.WorkOfferAdapter;
import com.josephblough.alibris.data.ItemSearchResult;
import com.josephblough.alibris.tasks.DataReceiver;
import com.josephblough.alibris.tasks.SearchResultsRetrieverTask;
import com.josephblough.alibris.transport.SearchRequestConstants;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class WorkOffersActivity extends ListActivity implements DataReceiver, OnItemClickListener {

    private static final String TAG = "WorkOffersActivity";
    
    public static final String WORK_ID = "WorkOffersActivity.work_id";
    
    private ProgressDialog progress;
    
    private int workId;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
        
        this.workId = getIntent().getIntExtra(WORK_ID, -1);
        Log.d(TAG, "Work ID: " + this.workId);
        
        if (this.workId > 0) {
            progress = ProgressDialog.show(this, "", "Retrieving list of offers");
            
            Map<String, String> params = new HashMap<String, String>();
            params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_WORK_ID, Integer.toString(workId));
            SearchResultsRetrieverTask retriever = new SearchResultsRetrieverTask(WorkOffersActivity.this);
            
            Log.d(TAG, "Retrieving all offers for " + workId);
            retriever.execute(params);
        }
    }

    public void dataReceived(JSONObject data) {
	if (progress != null) {
	    progress.dismiss();
	}
	
	try {
	    JSONArray works = data.getJSONArray("book");
	    int length = works.length();
	    List<ItemSearchResult> results = new ArrayList<ItemSearchResult>();
	    for (int i=0; i<length; i++) {
		results.add(new ItemSearchResult(works.getJSONObject(i)));
	    }
	    
	    WorkOfferAdapter adapter = new WorkOfferAdapter(this, results);
	    setListAdapter(adapter);
	    
	    fakeIt();
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }

    public void error(String error) {
	if (progress != null) {
	    progress.dismiss();
	}
	
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	Log.d(TAG, "onItemClick");
	Log.d(TAG, "Clicked on " + getListAdapter().getItemId(position) + ", position " + position);
	final String workAsJson = ((WorkOfferAdapter)getListAdapter()).getItem(position).toString();
	
	Intent intent = new Intent(this, WorkOfferDetailActivity.class);
	intent.putExtra(WorkOfferDetailActivity.WORK_AS_JSON, workAsJson);
	startActivity(intent);
    }
    
    private void fakeIt() {
	final String workAsJson = ((WorkOfferAdapter)getListAdapter()).getItem(0).toString();
	
	Intent intent = new Intent(this, WorkOfferDetailActivity.class);
	intent.putExtra(WorkOfferDetailActivity.WORK_AS_JSON, workAsJson);
	startActivity(intent);
    }
}
