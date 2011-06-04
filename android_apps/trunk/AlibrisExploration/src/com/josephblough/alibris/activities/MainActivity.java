package com.josephblough.alibris.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.adapters.SearchResultAdapter;
import com.josephblough.alibris.data.SearchCriteria;
import com.josephblough.alibris.data.SearchCriteriaCollection;
import com.josephblough.alibris.data.SearchResult;
import com.josephblough.alibris.data.WorkSearchResult;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends ListActivity implements OnItemClickListener, OnEditorActionListener, DataReceiver {
    
    private static final String TAG = "MainActivity";
    
    private static final int SEARCH_AUTHOR_INDEX = 0;
    private static final int SEARCH_TITLE_INDEX = 1;
    private static final int SEARCH_TOPIC_INDEX = 2;
    
    private EditText searchTermField;
    private Button submitButton;
    private ProgressDialog progress;
    
    private int currentSearchField = 3;
    private String lastSearchName;
    
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

	getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
        
        searchTermField.setOnEditorActionListener(this);
    }

    @SuppressWarnings("unchecked")
    private void performSearch() {
	
	progress = ProgressDialog.show(this, "", "Searching");
	
	Map<String, String> params = new HashMap<String, String>();
	String searchField = SearchRequestConstants.WORKS_SEARCH_FIELDS_ALL;
	switch (currentSearchField) {
	case SEARCH_AUTHOR_INDEX:
	    searchField = SearchRequestConstants.WORKS_SEARCH_FIELD_AUTHOR;
	    break;
	case SEARCH_TITLE_INDEX:
	    searchField = SearchRequestConstants.WORKS_SEARCH_FIELD_TITLE;
	    break;
	case SEARCH_TOPIC_INDEX:
	    searchField = SearchRequestConstants.WORKS_SEARCH_FIELD_TOPIC;
	    break;
	}
	params.put(searchField, searchTermField.getText().toString());
	SearchResultsRetrieverTask retriever = new SearchResultsRetrieverTask(MainActivity.this);
	retriever.execute(params);

	// Hide the keyboard
	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(searchTermField.getWindowToken(), 0);
	
	searchTermField.setVisibility(View.GONE);
	submitButton.setVisibility(View.GONE);
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
	    JSONArray works = data.getJSONArray("work");
	    int length = works.length();
	    List<SearchResult> results = new ArrayList<SearchResult>();
	    for (int i=0; i<length; i++) {
		results.add(new WorkSearchResult(works.getJSONObject(i)));
	    }
	    
	    SearchResultAdapter adapter = new SearchResultAdapter(this, results);
	    setListAdapter(adapter);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_SEARCH) {
	    searchTermField.setVisibility(View.VISIBLE);
	    submitButton.setVisibility(View.VISIBLE);
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
	case R.id.change_search_field_menu_item:
	    changeSearchCriteria();
	    return true;
	case R.id.refresh_menu_item:
	    performSearch();
	    return true;
	case R.id.exit_menu_item:
	    finish();
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
			final SearchCriteria search = searches.getSearch(name);
			currentSearchField = Integer.valueOf(search.parameter);
			searchTermField.setText(search.searchTerm);
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

			searches.addSearch(value, Integer.toString(currentSearchField), searchTermField.getText().toString());
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
    
    private void changeSearchCriteria() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Change Search Field");
	builder.setSingleChoiceItems(R.array.search_fields, currentSearchField, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int item) {
		currentSearchField = item;
		dialog.dismiss();
		if (!"".equals(searchTermField.getText().toString())) {
		    performSearch();
		}
	    }
	});
	builder.show();
    }
}