package com.josephblough.alibris;

import java.util.ArrayList;
import java.util.List;

import com.josephblough.alibris.activities.ShoppingCartActivity;
import com.josephblough.alibris.data.ItemSearchResult;
import com.josephblough.alibris.data.SearchCriteria;
import com.josephblough.alibris.util.ImageLoader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ApplicationController extends Application {

    private final static String TAG = "ApplicationController";
    
    public final static String ALIBRIS_URL = "http://www.alibris.com";
    public final static String ALIBRIS_CART_URL = "http://www.alibris.com/cart?invId=";
    
    public ImageLoader imageLoader;
    public List<ItemSearchResult> shoppingCart = new ArrayList<ItemSearchResult>();
    public SearchCriteria searchCriteria = null;

    public void onCreate() {
	super.onCreate();
	
	//Do Application initialization over here
	Log.d(TAG, "onCreate");

        imageLoader = new ImageLoader(this);
    }
    
    public void addToCart(final ItemSearchResult offer) {
	shoppingCart.add(offer);
    }
    
    public void removeFromCart(final ItemSearchResult offer) {
	shoppingCart.remove(offer);
    }
    
    public boolean isOfferInCart(final ItemSearchResult offer) {
	return shoppingCart.contains(offer);
    }
    
    public void presentShoppingCart(final Context comingFrom) {
	Intent intent = new Intent(comingFrom, ShoppingCartActivity.class);
	comingFrom.startActivity(intent);
    }
    
    public void checkOut(final Context comingFrom) {
	StringBuffer url = new StringBuffer(ALIBRIS_CART_URL);
	boolean first = true;
	for (ItemSearchResult item : shoppingCart) {
	    if (first)
		first = false;
	    else
		url.append(',');

	    url.append(item.sku);
	}
	
	Log.d(TAG, "Checkout URL: " + url.toString());
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url.toString()));
	comingFrom.startActivity(intent);
    }
    
    public void visitAlibris(final Context comingFrom) {
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(ALIBRIS_URL));
	comingFrom.startActivity(intent);
    }

    public  void initAlibrisHeader(final Activity comingFrom) {
        ((ImageView) comingFrom.findViewById(R.id.alibris_header_image)).setOnClickListener(new OnClickListener() {
	    
	    public void onClick(View v) {
		visitAlibris(comingFrom);
	    }
	});
        
        ((Button) comingFrom.findViewById(R.id.button_shopping_cart)).setOnClickListener(new OnClickListener() {
	    
	    public void onClick(View v) {
		presentShoppingCart(comingFrom);
	    }
	});
    }
}
