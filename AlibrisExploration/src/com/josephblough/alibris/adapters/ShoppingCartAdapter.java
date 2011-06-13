package com.josephblough.alibris.adapters;

import java.text.NumberFormat;
import java.util.List;

import com.josephblough.alibris.activities.ShoppingCartActivity;
import com.josephblough.alibris.data.ItemSearchResult;
import com.josephblough.alibris.ApplicationController;
import com.josephblough.alibris.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShoppingCartAdapter extends ArrayAdapter<ItemSearchResult> {
    
    private static final String TAG = "WorkOfferAdapter";

    private static LayoutInflater inflater = null;
    private ShoppingCartActivity activity;
    private ApplicationController app;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public ShoppingCartAdapter(Activity context, List<ItemSearchResult> objects) {
	super(context, R.layout.shopping_cart_row, objects);
	
	Log.d(TAG, "WorkOfferAdapter");
	
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activity = (ShoppingCartActivity)context;
        app = (ApplicationController)context.getApplicationContext();
    }

    public static class ViewHolder{
        public TextView titleText;
        public TextView authorText;
        public ImageView image;
        public TextView priceText;
        public ImageView removeFromCartImage;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;
        ViewHolder holder;

	if (row == null) {
	    row = inflater.inflate(R.layout.shopping_cart_row, null);
            holder = new ViewHolder();
            holder.titleText = (TextView)row.findViewById(R.id.shopping_cart_item_title);
            holder.authorText = (TextView)row.findViewById(R.id.shopping_cart_item_author);
            holder.priceText = (TextView)row.findViewById(R.id.shopping_cart_item_price);
            holder.image = (ImageView)row.findViewById(R.id.shopping_cart_item_image);
            holder.removeFromCartImage = (ImageView)row.findViewById(R.id.shopping_cart_item_remove);
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final ItemSearchResult entry = (ItemSearchResult)super.getItem(position);

	holder.titleText.setText(entry.title);
	holder.authorText.setText(entry.author);
	holder.priceText.setText(formatter.format(entry.price));
	holder.image.setTag(entry.imageURL);
	app.imageLoader.displayImage(entry.imageURL, holder.image);
	
	    holder.removeFromCartImage.setTag(entry);
	    holder.removeFromCartImage.setOnClickListener(activity);
	    holder.removeFromCartImage.setVisibility(View.VISIBLE);
	
	// If entry.recommends is not null, then set the image and show the imageview
	
	row.setId(position);
	
	return row;
    }
}
