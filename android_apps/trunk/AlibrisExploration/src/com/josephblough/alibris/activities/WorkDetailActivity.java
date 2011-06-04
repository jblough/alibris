package com.josephblough.alibris.activities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
import com.josephblough.alibris.adapters.SearchResultAdapter;
import com.josephblough.alibris.data.ReviewCollection;
import com.josephblough.alibris.data.SearchResult;
import com.josephblough.alibris.data.WorkSearchResult;
import com.josephblough.alibris.tasks.DataReceiver;
import com.josephblough.alibris.tasks.RecommendationRetrieverTask;
import com.josephblough.alibris.tasks.ReviewRetrieverTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class WorkDetailActivity extends Activity implements OnItemClickListener {

    private static final String TAG = "WorkDetailActivity";
    
    public static final String WORK_AS_JSON = "WorkDetailActivity.work_as_json";

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
        	    populateWorkDetails();

        	    ReviewRetrieverTask reviewRetriever = new ReviewRetrieverTask(new DataReceiver() {
		        
		        public void error(String error) {
		            Toast.makeText(WorkDetailActivity.this, error, Toast.LENGTH_SHORT).show();
		        }
		        
		        public void dataReceived(final JSONObject data) {
		            ReviewCollection reviews = new ReviewCollection(data);
		            TextView ratingLabel = (TextView)findViewById(R.id.item_details_overall_rating_label);
		            RatingBar ratingBar = (RatingBar)findViewById(R.id.item_details_overall_rating);
		            Button seeReviewsButton = (Button)findViewById(R.id.item_details_reviews_button);
		            if (reviews.getReviews() != null && reviews.getReviews().size() > 0) {
		        	Log.d(TAG, "Setting rating to " + reviews.overallRating + " for " + 
		        		reviews.getReviews().size() + " reviews");
		        	ratingBar.setRating((float)reviews.overallRating);
		        	
		        	ratingLabel.setText("Overall rating: " + Double.toString(reviews.overallRating));
		        	
		        	seeReviewsButton.setOnClickListener(new OnClickListener() {
				    public void onClick(View v) {
					Intent intent = new Intent(WorkDetailActivity.this, WorkReviewsActivity.class);
					intent.putExtra(WorkReviewsActivity.REVIEWS_AS_JSON, data.toString());
					startActivity(intent);
				    }
				});
		        	
		        	ratingLabel.setVisibility(View.VISIBLE);
		        	ratingBar.setVisibility(View.VISIBLE);
		        	seeReviewsButton.setVisibility(View.VISIBLE);
		            }
		            else {
		        	ratingLabel.setText("No reviews at this time");
		        	
		        	ratingLabel.setVisibility(View.VISIBLE);
		        	ratingBar.setVisibility(View.GONE);
		        	seeReviewsButton.setVisibility(View.GONE);
		            }
		        }
		    });
        	    reviewRetriever.execute(work.workId);

        	    RecommendationRetrieverTask recommendationRetriever = new RecommendationRetrieverTask(new DataReceiver() {
		        
		        public void error(String error) {
		            Toast.makeText(WorkDetailActivity.this, error, Toast.LENGTH_SHORT).show();
		        }
		        
		        public void dataReceived(JSONObject data) {
		            ListView recommendationList = (ListView)findViewById(R.id.item_details_recommendations_list);
		            recommendationList.setOnItemClickListener(WorkDetailActivity.this);
		            try {
		        	JSONArray works = data.getJSONArray("work");
		        	int length = works.length();
		        	List<SearchResult> results = new ArrayList<SearchResult>();
		        	for (int i=0; i<length; i++) {
		        	    results.add(new WorkSearchResult(works.getJSONObject(i)));
		        	}

		        	SearchResultAdapter adapter = new SearchResultAdapter(WorkDetailActivity.this, results);
		        	recommendationList.setAdapter(adapter);
		        	recommendationList.setVisibility(View.VISIBLE);
		            }
		            catch (JSONException e) {
		        	recommendationList.setVisibility(View.GONE);
		        	Log.e(TAG, e.getMessage(), e);
		            }
		        }
		    });
        	    recommendationRetriever.execute(work.workId);

        	}
            }
            catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
            }
        }
    }
    
    private synchronized void populateWorkDetails() {
	ImageView image = (ImageView) findViewById(R.id.item_details_image);
	image.setTag(work.imageURL);
	//Log.d(TAG, "Displaying image " + image.getTag());
	
	ApplicationController app = (ApplicationController) getApplicationContext();
	app.imageLoader.displayImage(work.imageURL, image);
	
	((TextView) findViewById(R.id.item_details_title)).setText(work.title);
	((TextView) findViewById(R.id.item_details_author)).setText(work.author);
	((TextView) findViewById(R.id.item_details_min_price)).setText(NumberFormat.getCurrencyInstance().format(work.minPrice));
	((TextView) findViewById(R.id.item_details_available)).setText(Integer.toString(work.quantityAvailable));
	((TextView) findViewById(R.id.item_details_synopsis)).setText(Html.fromHtml(work.synopsis));
	
	((Button) findViewById(R.id.item_details_see_all_offers)).setOnClickListener(new OnClickListener() {
	    
	    public void onClick(View v) {
		Intent intent = new Intent(WorkDetailActivity.this, WorkOffersActivity.class);
		intent.putExtra(WorkOffersActivity.WORK_ID, work.workId);
		startActivity(intent);
	    }
	});
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView recommendationList = (ListView)findViewById(R.id.item_details_recommendations_list);
	Log.d(TAG, "Clicked on " + recommendationList.getAdapter().getItemId(position) + ", position " + position);
	final String workAsJson = ((SearchResultAdapter)recommendationList.getAdapter()).getItem(position).toString();
	
	Intent intent = new Intent(this, WorkDetailActivity.class);
	intent.putExtra(WorkDetailActivity.WORK_AS_JSON, workAsJson);
	startActivity(intent);
    }
}
