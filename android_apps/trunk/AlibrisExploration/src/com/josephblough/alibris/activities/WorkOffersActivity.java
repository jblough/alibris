package com.josephblough.alibris.activities;

import java.text.NumberFormat;
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
import com.josephblough.alibris.data.OfferFilterCriteria;
import com.josephblough.alibris.tasks.DataReceiver;
import com.josephblough.alibris.tasks.SearchResultsRetrieverTask;
import com.josephblough.alibris.transport.SearchRequestConstants;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class WorkOffersActivity extends ListActivity implements DataReceiver, OnItemClickListener, OnClickListener {

    private static final String TAG = "WorkOffersActivity";
    
    public static final String WORK_ID = "WorkOffersActivity.work_id";
    
    private ProgressDialog progress;

    NumberFormat formatter;// = NumberFormat.getCurrencyInstance();
    OfferFilterCriteria filter = new OfferFilterCriteria();
    
    private static final String JSON_RESULT_STRING_KEY = "json.results";
    private String jsonResults = null;

    private int workId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_offers);
        
        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        
        final ApplicationController app = (ApplicationController) getApplication();
        app.initAlibrisHeader(this);
        
        this.workId = getIntent().getIntExtra(WORK_ID, -1);
        Log.d(TAG, "Work ID: " + this.workId);
        
        if (savedInstanceState != null && savedInstanceState.containsKey(JSON_RESULT_STRING_KEY)) {
            try {
        	dataReceived(new JSONObject(savedInstanceState.getString(JSON_RESULT_STRING_KEY)));
            }
            catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
            }
        }
        else {
            if (this.workId > 0) {
        	retrieveOffers();
            }
        }
        
        getListView().setOnItemClickListener(this);
    }

    @SuppressWarnings("unchecked")
    private void retrieveOffers() {
        progress = ProgressDialog.show(this, "", "Retrieving list of offers");
        
        //Map<String, String> params = new HashMap<String, String>();
        //params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_WORK_ID, Integer.toString(workId));
        Map<String, String> params = populateParameterMap();
        SearchResultsRetrieverTask retriever = new SearchResultsRetrieverTask(WorkOffersActivity.this);
        
        Log.d(TAG, "Retrieving all offers for " + workId);
        retriever.execute(params);
    }
    
    public void dataReceived(JSONObject data) {
	if (progress != null) {
	    progress.dismiss();
	}
	
	try {
	    jsonResults = data.toString();
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
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Change Filter Options");
	View view = getLayoutInflater().inflate(R.layout.work_offers_filter, null);
	final EditText minPriceField = (EditText)view.findViewById(R.id.work_offers_filter_min_price);
	final EditText maxPriceField = (EditText)view.findViewById(R.id.work_offers_filter_max_price);
	final Spinner minConditionField = (Spinner)view.findViewById(R.id.work_offers_filter_min_condition);
	final Spinner maxConditionField = (Spinner)view.findViewById(R.id.work_offers_filter_max_condition);
	final Spinner minSellerRatingField = (Spinner)view.findViewById(R.id.work_offers_filter_min_seller_rating);
	final Spinner sortOrderField = (Spinner)view.findViewById(R.id.work_offers_filter_sort);
	final CheckBox reverseSortOrderField = (CheckBox)view.findViewById(R.id.work_offers_filter_reverse_sort);
	
	if (filter.minPrice != null)
	    minPriceField.setText(formatter.format(filter.minPrice));
	if (filter.maxPrice != null)
	    maxPriceField.setText(formatter.format(filter.maxPrice));
	minConditionField.setSelection(filter.minCondition);
	maxConditionField.setSelection(filter.maxCondition);
	minSellerRatingField.setSelection(filter.minSellerRating);
	sortOrderField.setSelection(filter.sort);
	reverseSortOrderField.setChecked(filter.reverseSort);
	
	builder.setView(view);

	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int which) {
		if (!"".equals(minPriceField.getText().toString()))
		    filter.minPrice = Double.valueOf(minPriceField.getText().toString());
		else
		    filter.minPrice = null;
		
		if (!"".equals(maxPriceField.getText().toString()))
		    filter.maxPrice = Double.valueOf(maxPriceField.getText().toString());
		else
		    filter.maxPrice = null;
		
		filter.minCondition = minConditionField.getSelectedItemPosition();
		filter.maxCondition = maxConditionField.getSelectedItemPosition();
		filter.minSellerRating = minSellerRatingField.getSelectedItemPosition();
		filter.sort = sortOrderField.getSelectedItemPosition();
		filter.reverseSort = reverseSortOrderField.isChecked();
		
		retrieveOffers();
	    }
	});

	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		// Canceled.
		dialog.cancel();
	    }
	});


	builder.show();
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
	
	Toast.makeText(this, "Item added to shopping cart", Toast.LENGTH_SHORT).show();
    }
    
    
    private Map<String, String> populateParameterMap() {
	Map<String, String> params = new HashMap<String, String>();
	
	// Word item field
        params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_WORK_ID, Integer.toString(workId));
	
	// Minimum price
        if (filter.minPrice != null) {
            params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_PRICE_MIN, Double.toString(filter.minPrice));
        }
        
        // Maximum price
        if (filter.maxPrice != null) {
            params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_PRICE_MAX, Double.toString(filter.maxPrice));
        }
	
        // Minimum seller rating
        if (filter.minSellerRating != OfferFilterCriteria.FILTER_SELLER_RATING_ANY) {
            
        }
	
	// Sort order
	if (filter.reverseSort || filter.sort != OfferFilterCriteria.SORT_ORDER_RATING) {
	    String sort = SearchRequestConstants.SORT_RATING;
	    switch (filter.sort) {
	    case OfferFilterCriteria.SORT_ORDER_RATING:
		sort = SearchRequestConstants.SORT_RATING;
		break;
	    case OfferFilterCriteria.SORT_ORDER_CONDITION:
		sort = SearchRequestConstants.SORT_CONDITION;
		break;
	    case OfferFilterCriteria.SORT_ORDER_TITLE:
		sort = (filter.reverseSort) ? (SearchRequestConstants.SORT_TITLE + "r") : 
		    SearchRequestConstants.SORT_TITLE;
		break;
	    case OfferFilterCriteria.SORT_ORDER_AUTHOR:
		sort = (filter.reverseSort) ? (SearchRequestConstants.SORT_AUTHOR + "r") : 
		    SearchRequestConstants.SORT_AUTHOR;
		break;
	    case OfferFilterCriteria.SORT_ORDER_PRICE:
		sort = (filter.reverseSort) ? (SearchRequestConstants.SORT_PRICE + "r") : 
		    SearchRequestConstants.SORT_PRICE;
		break;
	    case OfferFilterCriteria.SORT_ORDER_DATE:
		sort = (filter.reverseSort) ? (SearchRequestConstants.SORT_DATE + "r") : 
		    SearchRequestConstants.SORT_DATE;
		break;
	    }
	    params.put(SearchRequestConstants.SEARCH_SORT, sort);
	}
	
	return params;
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (jsonResults != null)
            outState.putString(JSON_RESULT_STRING_KEY, jsonResults);
    }
}
