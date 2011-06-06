package com.josephblough.alibris.activities;

import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.adapters.ShoppingCartAdapter;
import com.josephblough.alibris.adapters.WorkOfferAdapter;
import com.josephblough.alibris.data.ItemSearchResult;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ShoppingCartActivity extends ListActivity implements OnItemClickListener, OnClickListener {

    private static final String TAG = "ShoppingCartActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	ApplicationController app = (ApplicationController)getApplication();
	ShoppingCartAdapter adapter = new ShoppingCartAdapter(this, app.shoppingCart);
	setListAdapter(adapter);

	getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	Log.d(TAG, "onItemClick");
	Log.d(TAG, "Clicked on " + getListAdapter().getItemId(position) + ", position " + position);
	final String workAsJson = ((WorkOfferAdapter)getListAdapter()).getItem(position).toString();
	
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
