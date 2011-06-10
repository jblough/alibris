package com.josephblough.alibris.activities;

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

import com.josephblough.alibris.adapters.SearchResultAdapter;
import com.josephblough.alibris.data.SearchCriteria;
import com.josephblough.alibris.data.SearchCriteriaCollection;
import com.josephblough.alibris.data.WorkSearchResult;
import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
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
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
//import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends ListActivity implements OnItemClickListener, OnEditorActionListener, DataReceiver {
    
    private static final String TAG = "MainActivity";
    
    private static final String JSON_RESULT_STRING_KEY = "json.results";
    
    private EditText searchTermField;
    private Button submitButton;
    private ProgressDialog progress;
    
    private String lastSearchName;
    
    private String jsonResults = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        searchTermField = (EditText) findViewById(R.id.search_parameter);
        submitButton = (Button) findViewById(R.id.submit_search);
        submitButton.setOnClickListener(new OnClickListener() {
	    
	    public void onClick(View v) {
		performSearch();
	    }
	});

        final ApplicationController app = (ApplicationController) getApplication();
        app.initAlibrisHeader(this);
        
	getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
        
        searchTermField.setOnEditorActionListener(this);
        
        if (savedInstanceState != null && savedInstanceState.containsKey(JSON_RESULT_STRING_KEY)) {
            try {
        	hideSearchFields();
        	dataReceived(new JSONObject(savedInstanceState.getString(JSON_RESULT_STRING_KEY)));
            }
            catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private void hideSearchFields() {
	// Hide the keyboard
	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(searchTermField.getWindowToken(), 0);
	
	searchTermField.setVisibility(View.GONE);
	submitButton.setVisibility(View.GONE);
    }
    
    private void showSearchFields() {
	searchTermField.setVisibility(View.VISIBLE);
	submitButton.setVisibility(View.VISIBLE);
    }
    
    @SuppressWarnings("unchecked")
    private void performSearch() {
	
	progress = ProgressDialog.show(this, "", "Searching");
	
	Map<String, String> params = populateParameterMap();
	SearchResultsRetrieverTask retriever = new SearchResultsRetrieverTask(MainActivity.this);
	retriever.execute(params);

	hideSearchFields();
    }
    
    public void error(String error) {
	if (progress != null) {
	    progress.dismiss();
	}
	
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
    
    public void dataReceived(JSONObject data) {
	if (progress != null) {
	    progress.dismiss();
	}
	
	try {
	    jsonResults = data.toString();
	    JSONArray works = data.getJSONArray("work");
	    int length = works.length();
	    List<WorkSearchResult> results = new ArrayList<WorkSearchResult>();
	    for (int i=0; i<length; i++) {
		results.add(new WorkSearchResult(works.getJSONObject(i)));
	    }
	    
	    SearchResultAdapter adapter = new SearchResultAdapter(this, results);
	    setListAdapter(adapter);
	    
	    // Display the search fields if there were no results
	    if (length == 0) {
		showSearchFields();
	    }
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_SEARCH) {
	    showSearchFields();
	    return true;
	}
	return super.onKeyUp(keyCode, event);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	Log.d(TAG, "Clicked on " + getListAdapter().getItemId(position) + ", position " + position);
	final String workAsJson = ((SearchResultAdapter)getListAdapter()).getItem(position).toString();
	
	Intent intent = new Intent(this, WorkDetailActivity.class);
	intent.putExtra(WorkDetailActivity.WORK_AS_JSON, workAsJson);
	startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
	//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

	return super.onContextItemSelected(item);
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	if (event == null || ( event.getAction() == 1)) {
	    performSearch();
	}
	return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.search_screen_menu, menu);
	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.load_search_menu_item:
	    loadSearches();
	    return true;
	case R.id.save_search_menu_item:
	    saveSearch();
	    return true;
	case R.id.delete_search_menu_item:
	    deleteSearches();
	    return true;
	case R.id.change_search_field_menu_item:
	    changeSearchCriteria();
	    return true;
	case R.id.refresh_menu_item:
	    performSearch();
	    return true;
	case R.id.search_menu_item:
	    showSearchFields();
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    private void loadSearches() {
	final String key = "Searches";
	SharedPreferences prefs = getSharedPreferences(MainActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    Collection<String> searchCollection = searches.getSearchNames();
	    if (searchCollection.size() > 0) {
		final String[] searchNames = new String[searchCollection.size()];
		Iterator<String> it = searchCollection.iterator();
		for (int i=0; i<searchCollection.size(); i++) {
		    searchNames[i] = it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Load Saved Search");
		builder.setSingleChoiceItems(searchNames, -1, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
			final String name = searchNames[item];
			final ApplicationController app = (ApplicationController) getApplication();
			app.searchCriteria = searches.getSearch(name);

			MainActivity.this.searchTermField.setText(app.searchCriteria.searchTerm);

			lastSearchName = name;

			dialog.dismiss();

			if (!"".equals(searchTermField.getText().toString())) {
			    performSearch();
			}
		    }
		});
		builder.show();
	    }
	    else {
		Toast.makeText(this, "No searches have been saved", Toast.LENGTH_LONG).show();
	    }
	}
	else {
	    Toast.makeText(this, "No searches have been saved", Toast.LENGTH_LONG).show();
	}
    }
    
    private void saveSearch() {
	if (!"".equals(searchTermField.getText().toString())) {
	    
	    // see http://androidsnippets.com/prompt-user-input-with-an-alertdialog
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Save Search");

	    // Set an EditText view to get user input 
	    final EditText input = new EditText(this);
	    if (lastSearchName != null)
		input.setText(lastSearchName);
	    builder.setView(input);

	    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    String value = input.getText().toString();
		    if (!"".equals(value)) {

			lastSearchName = value;
			
			final String key = "Searches";
			SharedPreferences prefs = getSharedPreferences(MainActivity.TAG, 0);
			SearchCriteriaCollection searches = (prefs.contains(key)) ? 
				new SearchCriteriaCollection(prefs.getString(key, null)) : new SearchCriteriaCollection();

			final ApplicationController app = (ApplicationController) getApplication();
			app.searchCriteria.searchTerm = searchTermField.getText().toString();
			if (app.searchCriteria.media == SearchCriteria.DEFAULT_SEARCH_MEDIA && 
				app.searchCriteria.sort == SearchCriteria.DEFAULT_SEARCH_SORT) {
			    searches.addSearch(value, app.searchCriteria.searchTerm, app.searchCriteria.field);
			}
			else {
			    if (app.searchCriteria.reverseSort) {
				searches.addSearch(value, app.searchCriteria.searchTerm, app.searchCriteria.field, app.searchCriteria.media, app.searchCriteria.sort, true);
			    }
			    else {
				searches.addSearch(value, app.searchCriteria.searchTerm, app.searchCriteria.field, app.searchCriteria.media, app.searchCriteria.sort);
			    }
			}
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(key, searches.toJson());
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
	else {
	    Toast.makeText(this, "No search terms to save", Toast.LENGTH_LONG).show();
	}
    }
    
    private void deleteSearches() {
	final String key = "Searches";
	final SharedPreferences prefs = getSharedPreferences(MainActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    Collection<String> searchCollection = searches.getSearchNames();
	    if (searchCollection.size() > 0) {
		final String[] searchNames = new String[searchCollection.size()];
		Iterator<String> it = searchCollection.iterator();
		for (int i=0; i<searchCollection.size(); i++) {
		    searchNames[i] = it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Saved Searches");
		final Set<String> searchesToRemove = new HashSet<String>();
		builder.setMultiChoiceItems(searchNames, null, new DialogInterface.OnMultiChoiceClickListener() {

		    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			String name = searchNames[which];
			if (isChecked)
			    searchesToRemove.add(name);
			else
			    searchesToRemove.remove(name);
		    }
		});

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
			// Remove the searches
			for (String name : searchesToRemove) {
			    searches.removeSearch(name);
			}

			// Commit the changes
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(key, searches.toJson());
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
		Toast.makeText(this, "No searches have been saved", Toast.LENGTH_LONG).show();
	    }
	}
	else {
	    Toast.makeText(this, "No searches have been saved", Toast.LENGTH_LONG).show();
	}
    }
    
    private void changeSearchCriteria() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Change Search Options");
	View view = getLayoutInflater().inflate(R.layout.search_options, null);
	final Spinner searchFieldSpinner = (Spinner)view.findViewById(R.id.search_field);
	final Spinner searchMediaSpinner = (Spinner)view.findViewById(R.id.search_media);
	final Spinner searchSortSpinner = (Spinner)view.findViewById(R.id.search_sort);
	final CheckBox reverseSort = (CheckBox)view.findViewById(R.id.search_reverse_sort);
	
	final ApplicationController app = (ApplicationController) getApplication();
	searchFieldSpinner.setSelection(app.searchCriteria.field);
	searchMediaSpinner.setSelection(app.searchCriteria.media);
	searchSortSpinner.setSelection(app.searchCriteria.sort);
	reverseSort.setChecked(app.searchCriteria.reverseSort);
	
	builder.setView(view);

	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int which) {
		app.searchCriteria.field= searchFieldSpinner.getSelectedItemPosition();
		app.searchCriteria.media = searchMediaSpinner.getSelectedItemPosition();
		app.searchCriteria.sort = searchSortSpinner.getSelectedItemPosition();
		app.searchCriteria.reverseSort = reverseSort.isChecked();

		performSearch();
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
    
    private Map<String, String> populateParameterMap() {
	final ApplicationController app = (ApplicationController) getApplication();

	Map<String, String> params = new HashMap<String, String>();
	
	// Search field
	String searchField = SearchRequestConstants.WORKS_SEARCH_FIELDS_ALL;
	switch (app.searchCriteria.field) {
	case SearchCriteria.SEARCH_AUTHOR_INDEX:
	    searchField = SearchRequestConstants.WORKS_SEARCH_FIELD_AUTHOR;
	    break;
	case SearchCriteria.SEARCH_TITLE_INDEX:
	    searchField = SearchRequestConstants.WORKS_SEARCH_FIELD_TITLE;
	    break;
	case SearchCriteria.SEARCH_TOPIC_INDEX:
	    searchField = SearchRequestConstants.WORKS_SEARCH_FIELD_TOPIC;
	    break;
	}
	params.put(searchField, searchTermField.getText().toString());
	
	// Media type
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
	
	// Sort order
	if (app.searchCriteria.reverseSort || app.searchCriteria.sort != SearchCriteria.DEFAULT_SEARCH_SORT) {
	    String sort = SearchRequestConstants.SORT_RATING;
	    switch (app.searchCriteria.sort) {
	    case SearchCriteria.SORT_ORDER_RATING_INDEX:
		sort = SearchRequestConstants.SORT_RATING;
		break;
	    case SearchCriteria.SORT_ORDER_TITLE_INDEX:
		sort = (app.searchCriteria.reverseSort) ? (SearchRequestConstants.SORT_TITLE + "r") : 
		    SearchRequestConstants.SORT_TITLE;
		break;
	    case SearchCriteria.SORT_ORDER_AUTHOR_INDEX:
		sort = (app.searchCriteria.reverseSort) ? (SearchRequestConstants.SORT_AUTHOR + "r") : 
		    SearchRequestConstants.SORT_AUTHOR;
		break;
	    case SearchCriteria.SORT_ORDER_PRICE_INDEX:
		sort = (app.searchCriteria.reverseSort) ? (SearchRequestConstants.SORT_PRICE + "r") : 
		    SearchRequestConstants.SORT_PRICE;
		break;
	    case SearchCriteria.SORT_ORDER_DATE_INDEX:
		sort = (app.searchCriteria.reverseSort) ? (SearchRequestConstants.SORT_DATE + "r") : 
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