package com.josephblough.alibris.activities;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;
import com.josephblough.alibris.adapters.ShoppingCartAdapter;
import com.josephblough.alibris.data.ItemSearchResult;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class ShoppingCartActivity extends ListActivity implements OnItemClickListener, OnClickListener {

    private static final String TAG = "ShoppingCartActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);

	final ApplicationController app = (ApplicationController)getApplication();
	ShoppingCartAdapter adapter = new ShoppingCartAdapter(this, app.shoppingCart);
	setListAdapter(adapter);

	getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
        
        ((Button)findViewById(R.id.checkout)).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCartActivity.this);
        	builder.setTitle("Checkout");
        	builder.setMessage("Are you sure that you're ready to checkout?");
        	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int whichButton) {
                	AlertDialog.Builder builder2 = new AlertDialog.Builder(ShoppingCartActivity.this);
                	builder2.setMessage("You will now be redirected to the Alibris website to checkout");
                	builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			    
			    public void onClick(DialogInterface dialog, int which) {
	        		app.checkOut(ShoppingCartActivity.this);
			    }
			});
                	
                	builder2.show();
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
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	Log.d(TAG, "onItemClick");
	Log.d(TAG, "Clicked on " + getListAdapter().getItemId(position) + ", position " + position);
	final String workAsJson = ((ShoppingCartAdapter)getListAdapter()).getItem(position).toString();
	
	Intent intent = new Intent(this, WorkOfferDetailActivity.class);
	intent.putExtra(WorkOfferDetailActivity.WORK_AS_JSON, workAsJson);
	startActivity(intent);
    }

    public void onClick(View v) {
	ItemSearchResult offer = (ItemSearchResult)v.getTag();
	ApplicationController app = (ApplicationController)getApplication();
	app.removeFromCart(offer);
	//Log.d(TAG, "Removed " + offer.sku + " from cart");
	((ShoppingCartAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
