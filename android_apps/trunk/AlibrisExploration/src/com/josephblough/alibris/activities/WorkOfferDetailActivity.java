package com.josephblough.alibris.activities;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
import com.josephblough.alibris.data.ItemSearchResult;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkOfferDetailActivity extends Activity {

    private static final String TAG = "WorkOfferDetailActivity";
    
    public static final String WORK_AS_JSON = "WorkOfferDetailActivity.work_as_json";

    private ItemSearchResult offer;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_offer_details);
        
        final String json = getIntent().getStringExtra(WORK_AS_JSON);
        if (json != null) {
            try {
        	offer = new ItemSearchResult(new JSONObject(json));
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
	
	((TextView) findViewById(R.id.item_offer_details_title)).setText(offer.title);
	((TextView) findViewById(R.id.item_offer_details_author)).setText(offer.author);
	((TextView) findViewById(R.id.item_offer_details_price)).setText(NumberFormat.getCurrencyInstance().format(offer.price));
	((TextView) findViewById(R.id.item_offer_details_synopsis)).setText(Html.fromHtml(offer.notes));
    }
}
