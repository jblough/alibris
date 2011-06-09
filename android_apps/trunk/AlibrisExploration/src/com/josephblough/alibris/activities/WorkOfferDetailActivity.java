package com.josephblough.alibris.activities;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
import com.josephblough.alibris.data.WorkItemOfferDetail;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class WorkOfferDetailActivity extends Activity {

    private static final String TAG = "WorkOfferDetailActivity";
    
    public static final String WORK_AS_JSON = "WorkOfferDetailActivity.work_as_json";

    private WorkItemOfferDetail offer;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_offer_details);
        
        final String json = getIntent().getStringExtra(WORK_AS_JSON);
        if (json != null) {
            try {
        	offer = new WorkItemOfferDetail(new JSONObject(json));
        	Log.d(TAG, "Read in offer " + offer.sku);
        	
        	populateOfferDetails();
            }
            catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private synchronized void populateOfferDetails() {
	ImageView image = (ImageView) findViewById(R.id.item_offer_details_image);
	image.setTag(offer.imageURL);
	//Log.d(TAG, "Displaying image " + image.getTag());
	
	ApplicationController app = (ApplicationController) getApplicationContext();
	app.imageLoader.displayImage(offer.imageURL, image);
	
	((TextView) findViewById(R.id.item_offer_details_title_label)).setText(offer.title);
	((TextView) findViewById(R.id.item_offer_details_author_label)).setText("By: " + offer.author);
	((TextView) findViewById(R.id.item_offer_details_price)).setText(NumberFormat.getCurrencyInstance().format(offer.price));
	
	if (offer.comments != null && !"".equals(offer.comments))
	    ((TextView) findViewById(R.id.item_offer_comments)).setText(Html.fromHtml(offer.comments));
	else {
	    findViewById(R.id.item_offer_comments_label).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_comments).setVisibility(View.GONE);
	}
	
	if (offer.notes != null && !"".equals(offer.notes))
	    ((TextView) findViewById(R.id.item_offer_notes)).setText(Html.fromHtml(offer.notes));
	else {
	    findViewById(R.id.item_offer_notes).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_notes_label).setVisibility(View.GONE);
	}
	    
	if (offer.reliability != null) {
	    ((TextView) findViewById(R.id.item_offer_seller_rating_label)).setText("Seller Reliability Rating: (" + offer.reliability + ")");
	    ((RatingBar) findViewById(R.id.item_offer_seller_rating)).setRating(offer.reliability);
	}
	else
	    findViewById(R.id.item_offer_seller_rating).setVisibility(View.GONE);
    }
}
