package com.josephblough.alibris.activities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
import com.josephblough.alibris.adapters.WorkOfferAdapter;
import com.josephblough.alibris.data.ItemSearchResult;
import com.josephblough.alibris.data.OfferFilterCriteria;
import com.josephblough.alibris.data.OfferFilterCriteriaCollection;
import com.josephblough.alibris.data.SearchCriteria;
import com.josephblough.alibris.tasks.DataReceiver;
import com.josephblough.alibris.tasks.SearchResultsRetrieverTask;
import com.josephblough.alibris.transport.SearchRequestConstants;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final String JSON_RESULT_STRING_KEY = "json.results";
    private String jsonResults = null;

    private int workId;

    private String lastFilterName;

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
		// When first visiting this screen reset the filter
		app.currentFilter = new OfferFilterCriteria();
		retrieveOffers();
	    }
	}

	getListView().setOnItemClickListener(this);
    }

    @SuppressWarnings("unchecked")
    private void retrieveOffers() {
	progress = ProgressDialog.show(this, "", "Retrieving list of offers");

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
	    JSONArray works = data.optJSONArray("book");
	    int length = (works == null) ? 0 : works.length();
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
	case R.id.reset_filter_menu_item:
	    ((ApplicationController)getApplication()).currentFilter = new OfferFilterCriteria();
	    retrieveOffers();
	    return true;
	case R.id.load_filter_menu_item:
	    loadFilters();
	    return true;
	case R.id.save_filter_menu_item:
	    saveFilter();
	    return true;
	case R.id.delete_filter_menu_item:
	    deleteFilters();
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

	final ApplicationController app = (ApplicationController)getApplication();
	if (app.currentFilter.minPrice != null)
	    minPriceField.setText(formatter.format(app.currentFilter.minPrice));
	if (app.currentFilter.maxPrice != null)
	    maxPriceField.setText(formatter.format(app.currentFilter.maxPrice));
	minConditionField.setSelection(app.currentFilter.minCondition);
	maxConditionField.setSelection(app.currentFilter.maxCondition);
	minSellerRatingField.setSelection(app.currentFilter.minSellerRating);
	sortOrderField.setSelection(app.currentFilter.sort);
	reverseSortOrderField.setChecked(app.currentFilter.reverseSort);

	builder.setView(view);

	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int which) {
		if (!"".equals(minPriceField.getText().toString()))
		    app.currentFilter.minPrice = Double.valueOf(minPriceField.getText().toString());
		else
		    app.currentFilter.minPrice = null;

		if (!"".equals(maxPriceField.getText().toString()))
		    app.currentFilter.maxPrice = Double.valueOf(maxPriceField.getText().toString());
		else
		    app.currentFilter.maxPrice = null;

		app.currentFilter.minCondition = minConditionField.getSelectedItemPosition();
		app.currentFilter.maxCondition = maxConditionField.getSelectedItemPosition();
		app.currentFilter.minSellerRating = minSellerRatingField.getSelectedItemPosition();
		app.currentFilter.sort = sortOrderField.getSelectedItemPosition();
		app.currentFilter.reverseSort = reverseSortOrderField.isChecked();

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

    public void onClick(View v) {
	ItemSearchResult offer = (ItemSearchResult)v.getTag();
	ApplicationController app = (ApplicationController)getApplication();
	app.addToCart(offer);
	//Log.d(TAG, "Added " + offer.sku + " to cart");
	((WorkOfferAdapter)getListAdapter()).notifyDataSetChanged();

	Toast.makeText(this, "Item added to shopping cart", Toast.LENGTH_SHORT).show();
    }


    private Map<String, String> populateParameterMap() {
	final ApplicationController app = (ApplicationController)getApplication();

	Map<String, String> params = new HashMap<String, String>();

	// Word item field
	params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_WORK_ID, Integer.toString(workId));

	// Minimum price
	if (app.currentFilter.minPrice != null) {
	    params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_PRICE_MIN, Double.toString(app.currentFilter.minPrice));
	}

	// Maximum price
	if (app.currentFilter.maxPrice != null) {
	    params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_PRICE_MAX, Double.toString(app.currentFilter.maxPrice));
	}

	// Minimum seller rating
	if (app.currentFilter.minSellerRating != OfferFilterCriteria.FILTER_SELLER_RATING_ANY) {
	    // OfferFilterCriteria stores minimum seller rating by array index so BEST = 0 and POOR = 4
	    // The actual parameter sent to Alibris is in the form BEST = 5 and POOR = 1, so we subtract from 5
	    params.put(SearchRequestConstants.ITEMS_SEARCH_FIELD_RATING, Integer.toString(5 - app.currentFilter.minSellerRating));
	}

	// Sort order
	if (app.currentFilter.reverseSort || app.currentFilter.sort != OfferFilterCriteria.SORT_ORDER_RATING) {
	    String sort = SearchRequestConstants.SORT_RATING;
	    switch (app.currentFilter.sort) {
	    case OfferFilterCriteria.SORT_ORDER_RATING:
		sort = SearchRequestConstants.SORT_RATING;
		break;
	    case OfferFilterCriteria.SORT_ORDER_CONDITION:
		sort = SearchRequestConstants.SORT_CONDITION;
		break;
	    case OfferFilterCriteria.SORT_ORDER_TITLE:
		sort = (app.currentFilter.reverseSort) ? (SearchRequestConstants.SORT_TITLE + "r") : 
		    SearchRequestConstants.SORT_TITLE;
		break;
	    case OfferFilterCriteria.SORT_ORDER_AUTHOR:
		sort = (app.currentFilter.reverseSort) ? (SearchRequestConstants.SORT_AUTHOR + "r") : 
		    SearchRequestConstants.SORT_AUTHOR;
		break;
	    case OfferFilterCriteria.SORT_ORDER_PRICE:
		sort = (app.currentFilter.reverseSort) ? (SearchRequestConstants.SORT_PRICE + "r") : 
		    SearchRequestConstants.SORT_PRICE;
		break;
	    case OfferFilterCriteria.SORT_ORDER_DATE:
		sort = (app.currentFilter.reverseSort) ? (SearchRequestConstants.SORT_DATE + "r") : 
		    SearchRequestConstants.SORT_DATE;
		break;
	    }
	    params.put(SearchRequestConstants.SEARCH_SORT, sort);
	}
	
	// Media type (based on the original search)
	if (app.searchCriteria.media != SearchCriteria.DEFAULT_SEARCH_MEDIA) {
	    String mediaType = SearchRequestConstants.SEARCH_TYPE_BOOKS;
	    switch (app.searchCriteria.media) {
	    case SearchCriteria.SEARCH_MEDIA_ALL_INDEX:
		mediaType = SearchRequestConstants.SEARCH_TYPE_ALL;
		break;
	    case SearchCriteria.SEARCH_MEDIA_MUSIC_INDEX:
		mediaType = SearchRequestConstants.SEARCH_TYPE_MUSIC;
		break;
	    case SearchCriteria.SEARCH_MEDIA_MOVIES_INDEX:
		mediaType = SearchRequestConstants.SEARCH_TYPE_VIDEO;
		break;
	    }
	    params.put(SearchRequestConstants.SEARCH_TYPE, mediaType);
	}

	return params;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);

	if (jsonResults != null)
	    outState.putString(JSON_RESULT_STRING_KEY, jsonResults);
    }

    private void loadFilters() {
	final String key = "Filters";
	SharedPreferences prefs = getSharedPreferences(WorkOffersActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final OfferFilterCriteriaCollection filters = new OfferFilterCriteriaCollection(prefs.getString(key, null));
	    Collection<String> filterCollection = filters.getFilterNames();
	    if (filterCollection.size() > 0) {
		final String[] filterNames = new String[filterCollection.size()];
		Iterator<String> it = filterCollection.iterator();
		for (int i=0; i<filterCollection.size(); i++) {
		    filterNames[i] = it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Load Saved Filter");
		builder.setSingleChoiceItems(filterNames, -1, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
			final String name = filterNames[item];
			final ApplicationController app = (ApplicationController) getApplication();
			app.currentFilter = filters.getFilter(name);

			lastFilterName = name;

			dialog.dismiss();

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
	    else {
		Toast.makeText(this, "No filters have been saved", Toast.LENGTH_LONG).show();
	    }
	}
	else {
	    Toast.makeText(this, "No filters have been saved", Toast.LENGTH_LONG).show();
	}
    }

    private void saveFilter() {
	// see http://androidsnippets.com/prompt-user-input-with-an-alertdialog
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Save Filter");

	// Set an EditText view to get user input 
	final EditText input = new EditText(this);
	if (lastFilterName != null)
	    input.setText(lastFilterName);
	builder.setView(input);

	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		String value = input.getText().toString();
		if (!"".equals(value)) {

		    lastFilterName = value;

		    final String key = "Filters";
		    SharedPreferences prefs = getSharedPreferences(WorkOffersActivity.TAG, 0);
		    OfferFilterCriteriaCollection filters = (prefs.contains(key)) ? 
			    new OfferFilterCriteriaCollection(prefs.getString(key, null)) : new OfferFilterCriteriaCollection();

		    final ApplicationController app = (ApplicationController) getApplication();
		    filters.addFilter(value, app.currentFilter);
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putString(key, filters.toJson());
		    editor.commit();
		}
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
    
    private void deleteFilters() {
	final String key = "Filters";
	final SharedPreferences prefs = getSharedPreferences(WorkOffersActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final OfferFilterCriteriaCollection filters = new OfferFilterCriteriaCollection(prefs.getString(key, null));
	    Collection<String> filterCollection = filters.getFilterNames();
	    if (filterCollection.size() > 0) {
		final String[] filterNames = new String[filterCollection.size()];
		Iterator<String> it = filterCollection.iterator();
		for (int i=0; i<filterCollection.size(); i++) {
		    filterNames[i] = it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Saved Filters");
		final Set<String> filtersToRemove = new HashSet<String>();
		builder.setMultiChoiceItems(filterNames, null, new DialogInterface.OnMultiChoiceClickListener() {

		    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			String name = filterNames[which];
			if (isChecked)
			    filtersToRemove.add(name);
			else
			    filtersToRemove.remove(name);
		    }
		});

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
			// Remove the searches
			for (String name : filtersToRemove) {
			    filters.removeFilter(name);
			}

			// Commit the changes
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(key, filters.toJson());
			editor.commit();

			dialog.dismiss();
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
	    else {
		Toast.makeText(this, "No filters have been saved", Toast.LENGTH_LONG).show();
	    }
	}
	else {
	    Toast.makeText(this, "No filters have been saved", Toast.LENGTH_LONG).show();
	}
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // This is done to reflect changes to the shopping cart that may have happened on the previous activity
        if (getListAdapter() != null) {
            ((WorkOfferAdapter)getListAdapter()).notifyDataSetChanged();
        }
    }
}
