package com.josephblough.alibris.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
import com.josephblough.alibris.adapters.ReviewAdapter;
import com.josephblough.alibris.data.Review;
import com.josephblough.alibris.data.ReviewCollection;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

public class WorkReviewsActivity extends ListActivity {

    private static final String TAG = "WorkReviewsActivity";

    public static final String REVIEWS_AS_JSON = "WorkReviewsActivity.reviews_as_json";

    private ReviewCollection reviewCollection;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.work_reviews);
        
        final ApplicationController app = (ApplicationController) getApplication();
        app.initAlibrisHeader(this);
        
	final String json = getIntent().getStringExtra(REVIEWS_AS_JSON);
	if (json != null) {
	    try {
		reviewCollection = new ReviewCollection(new JSONObject(json));
		List<Review> reviews = reviewCollection.getReviews();

		ReviewAdapter adapter = new ReviewAdapter(this, reviews);
		setListAdapter(adapter);
	    }
	    catch (JSONException e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	}
    }        
}
