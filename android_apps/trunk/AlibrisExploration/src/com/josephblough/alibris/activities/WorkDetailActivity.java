package com.josephblough.alibris.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.R;
import com.josephblough.alibris.data.WorkSearchResult;
import com.josephblough.alibris.util.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkDetailActivity extends Activity {

    private static final String TAG = "WorkDetailActivity";
    
    public static final String WORK_AS_JSON = "WorkDetailActivity.work_as_json";

    private ImageLoader imageLoader;
    private WorkSearchResult work;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        
        final String json = getIntent().getStringExtra(WORK_AS_JSON);
        if (json != null) {
            try {
        	work = new WorkSearchResult(new JSONObject(json));
        	Log.d(TAG, "Read in work " + work.workId);
        	
        	if (work.workId > 0) {
        	    this.imageLoader = new ImageLoader(getApplicationContext());
        	    populateWorkDetails();
        	    /*
        	    ReviewRetrieverTask reviewRetriever = new ReviewRetrieverTask();
        	    reviewRetriever.execute(work.workId);

        	    RecommendationRetrieverTask recommendationRetriever = new RecommendationRetrieverTask();
        	    recommendationRetriever.execute(work.workId);
        	    */
        	}
            }
            catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
            }
        }
    }
    
    private void populateWorkDetails() {
	ImageView image = (ImageView) findViewById(R.id.item_details_image);
	image.setTag(work.imageURL);
	
	imageLoader.displayImage(work.imageURL, image);
	
	((TextView) findViewById(R.id.item_details_title)).setText(work.title);
	((TextView) findViewById(R.id.item_details_author)).setText(work.author);
	((TextView) findViewById(R.id.item_details_min_price)).setText(Double.toString(work.minPrice));
	((TextView) findViewById(R.id.item_details_synopsis)).setText(work.synopsis);
    }
}
