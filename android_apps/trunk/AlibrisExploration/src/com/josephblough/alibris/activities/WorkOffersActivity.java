package com.josephblough.alibris.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class WorkOffersActivity extends ListActivity implements DataReceiver, OnItemClickListener, OnClickListener {

    private static final String TAG = "WorkOffersActivity";
    
    public static final String WORK_ID = "WorkOffersActivity.work_id";
    
    private ProgressDialog progress;
    
    private int workId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_offers);
        
        final ApplicationController app = (ApplicationController) getApplication();
        app.initAlibrisHeader(this);
        
        getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
        
        this.workId = getIntent().getIntExtra(WORK_ID, -1);
        Log.d(TAG, "Work ID: " + this.workId);
        
        if (this.workId > 0) {
            retrieveOffers();
        }
    }

    @SuppressWarnings("unchecked")
    private void retrieveOffers() {
        progress = ProgressDialog.show(this, "", "Retrieving list of offers");
        
        Map<String, String> params = new HashMap<String, String>();
        params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_WORK_ID, Integer.toString(workId));
        SearchResultsRetrieverTask retriever = new SearchResultsRetrieverTask(WorkOffersActivity.this);
        
        Log.d(TAG, "Retrieving all offers for " + workId);
        retriever.execute(params);
    }
    
    public void dataReceived(JSONObject data) {
	if (progress != null) {
	    progress.dismiss();
	}
	
	try {
	    JSONArray works = data.getJSONArray("book");
	    int length = works.length();
	    Log.d(TAG, "Retrieved " + length + " offers");
	    List<ItemSearchResult> results = new ArrayList<ItemSearchResult>();
	    for (int i=0; i<length; i++) {
		results.add(new ItemSearchResult(works.getJSONObject(i)));
	    }
	    
	    WorkOfferAdapter adapter = new WorkOfferAdapter(this, results);
	    setListAdapter(adapter);
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
    
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.work_offer_list_menu, menu);
	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.refresh_offers_menu_item:
	    retrieveOffers();
	    return true;
	case R.id.filter_offers_menu_item:
	    displayFilterOptions();
	    return true;
	case R.id.offer_options_menu_item:
	    displaySortOptions();
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    private void displayFilterOptions() {
	// Allow filtering based on binding, signed, first edition, dust jacket, language, min and max price,
	//	min and max year, less than X days listed
	// Binding filter has different meanings based on the media type
    }
    
    private void displaySortOptions() {
	// Same order by as works, but also added "c" for condition
    }

    public void onClick(View v) {
	ItemSearchResult offer = (ItemSearchResult)v.getTag();
	ApplicationController app = (ApplicationController)getApplication();
	app.addToCart(offer);
	//Log.d(TAG, "Added " + offer.sku + " to cart");
	((WorkOfferAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
