package com.josephblough.alibris.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.adapters.SearchResultAdapter;
import com.josephblough.alibris.data.SearchResult;
import com.josephblough.alibris.data.WorkSearchResult;
import com.josephblough.alibris.R;
import com.josephblough.alibris.tasks.DataReceiver;
import com.josephblough.alibris.tasks.SearchResultsRetrieverTask;
import com.josephblough.alibris.transport.SearchRequestConstants;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
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
    
    private EditText searchTermField;
    private Button submitButton;
    private ProgressDialog progress;
    
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
	params.put(SearchRequestConstants.WORKS_SEARCH_FIELDS_ALL, searchTermField.getText().toString());
	SearchResultsRetrieverTask retriever = new SearchResultsRetrieverTask(MainActivity.this);
	retriever.execute(params);

	// Hide the keyboard
	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	//imm.hideSoftInputFromInputMethod(searchTermField.getWindowToken(), 0);
	
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
}