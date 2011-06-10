package com.josephblough.alibris.activities;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
import com.josephblough.alibris.data.WorkItemOfferDetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class WorkOfferDetailActivity extends Activity implements OnClickListener {

    private static final String TAG = "WorkOfferDetailActivity";
    
    public static final String WORK_AS_JSON = "WorkOfferDetailActivity.work_as_json";

    private WorkItemOfferDetail offer;
    private Button toggleCartButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_offer_details);
        
        toggleCartButton = (Button)findViewById(R.id.toggle_cart_button);
        toggleCartButton.setOnClickListener(this);

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
        
        final ApplicationController app = (ApplicationController) getApplication();
        app.initAlibrisHeader(this);
    }

    private void updateToggleCartButtonText() {
	ApplicationController app = (ApplicationController)getApplication();
	toggleCartButton.setText(app.isOfferInCart(offer) ? "Remove from cart" : "Add to cart");
    }
    
    private synchronized void populateOfferDetails() {
	ImageView image = (ImageView) findViewById(R.id.item_offer_details_image);
	image.setTag(offer.imageURL);
	//Log.d(TAG, "Displaying image " + image.getTag());
	
	final ApplicationController app = (ApplicationController) getApplicationContext();
	app.imageLoader.displayImage(offer.imageURL, image);

	image.setOnClickListener(new OnClickListener() {
	    
	    public void onClick(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(WorkOfferDetailActivity.this);

		builder.setTitle("Image Close-up");
		ImageView tempImage = new ImageView(WorkOfferDetailActivity.this);
		app.imageLoader.displayImage(offer.imageURL, tempImage);
		builder.setView(tempImage);
		builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
			// Canceled.
			dialog.cancel();
		    }
		});
		
		builder.show();
	    }
	});
	
	((TextView) findViewById(R.id.item_offer_details_title_label)).setText(offer.title);
	((TextView) findViewById(R.id.item_offer_details_author_label)).setText("By: " + offer.author);
	((TextView) findViewById(R.id.item_offer_details_price)).setText(NumberFormat.getCurrencyInstance().format(offer.price));
	
	// Publisher
	if (offer.publisher != null && !"".equals(offer.publisher))
	    ((TextView) findViewById(R.id.item_offer_publisher)).setText(offer.publisher);
	else {
	    findViewById(R.id.item_offer_publisher_label).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_publisher).setVisibility(View.GONE);
	}
	
	// Publication Date
	if (offer.publicationDate != null && !"".equals(offer.publicationDate))
	    ((TextView) findViewById(R.id.item_offer_publication_date)).setText(offer.publicationDate);
	else {
	    findViewById(R.id.item_offer_publication_date_label).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_publication_date).setVisibility(View.GONE);
	}
	
	// Publication Location
	if (offer.locationPublished != null && !"".equals(offer.locationPublished))
	    ((TextView) findViewById(R.id.item_offer_publication_place)).setText(offer.locationPublished);
	else {
	    findViewById(R.id.item_offer_publication_place_label).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_publication_place).setVisibility(View.GONE);
	}
	
	// ISBN
	if (offer.isbn != null && !"".equals(offer.isbn))
	    ((TextView) findViewById(R.id.item_offer_isbn)).setText(offer.isbn);
	else {
	    findViewById(R.id.item_offer_isbn_label).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_isbn).setVisibility(View.GONE);
	}
	
	// Comments
	if (offer.comments != null && !"".equals(offer.comments))
	    ((TextView) findViewById(R.id.item_offer_comments)).setText(Html.fromHtml(offer.comments));
	else {
	    findViewById(R.id.item_offer_comments_label).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_comments).setVisibility(View.GONE);
	}
	
	// Condition
	if ((offer.condition != null && !"".equals(offer.condition)) ||
		(offer.itemCondition != null)) {
	    if (offer.itemCondition != null) {
		((TextView) findViewById(R.id.item_offer_condition)).setText(offer.getItemConditionAsString());
	    }
	    else if (offer.condition != null && !"".equals(offer.condition)) {
		((TextView) findViewById(R.id.item_offer_condition)).setText(Html.fromHtml(offer.condition));
	    }
	    else {
		findViewById(R.id.item_offer_condition).setVisibility(View.GONE);
	    }
	}
	else {
	    findViewById(R.id.item_offer_condition_label).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_condition).setVisibility(View.GONE);
	}
	
	// Notes
	if (offer.notes != null && !"".equals(offer.notes))
	    ((TextView) findViewById(R.id.item_offer_notes)).setText(Html.fromHtml(offer.notes));
	else {
	    findViewById(R.id.item_offer_notes).setVisibility(View.GONE);
	    findViewById(R.id.item_offer_notes_label).setVisibility(View.GONE);
	}
	
	// Seller reliability rating
	if (offer.reliability != null) {
	    ((TextView) findViewById(R.id.item_offer_seller_rating_label)).setText("Seller Reliability Rating: (" + offer.reliability + ")");
	    ((RatingBar) findViewById(R.id.item_offer_seller_rating)).setRating(offer.reliability);
	}
	else
	    findViewById(R.id.item_offer_seller_rating).setVisibility(View.GONE);
    }

    public void onClick(View v) {
	
	ApplicationController app = (ApplicationController)getApplication();
	if (app.isOfferInCart(offer)) {
	    app.removeFromCart(offer);
	}
	else {
	    app.addToCart(offer);
	}
	
	updateToggleCartButtonText();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // This is done to reflect changes to the shopping cart that may have happened on the previous activity
        updateToggleCartButtonText();
    }
}
