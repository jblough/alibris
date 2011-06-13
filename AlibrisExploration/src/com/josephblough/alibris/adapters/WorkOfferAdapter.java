package com.josephblough.alibris.adapters;

import java.text.NumberFormat;
import java.util.List;

import com.josephblough.alibris.activities.WorkOffersActivity;
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

public class WorkOfferAdapter extends ArrayAdapter<ItemSearchResult> {
    
    private static final String TAG = "WorkOfferAdapter";

    private static LayoutInflater inflater = null;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private WorkOffersActivity activity;

    public WorkOfferAdapter(Activity context, List<ItemSearchResult> objects) {
	super(context, R.layout.work_offer_row, objects);
	
	Log.d(TAG, "WorkOfferAdapter");
	
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activity = (WorkOffersActivity)context;
    }

    public static class ViewHolder{
        public TextView sellerText;
        public TextView bindingText;
        public TextView conditionText;
        public TextView priceText;
        public ImageView shoppingCartImage;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;
        ViewHolder holder;

	if (row == null) {
	    row = inflater.inflate(R.layout.work_offer_row, null);
            holder = new ViewHolder();
            holder.sellerText = (TextView)row.findViewById(R.id.work_offer_seller);
            holder.bindingText = (TextView)row.findViewById(R.id.work_offer_binding);
            holder.conditionText = (TextView)row.findViewById(R.id.work_offer_condition);
            holder.priceText = (TextView)row.findViewById(R.id.work_offer_price);
            holder.shoppingCartImage = (ImageView)row.findViewById(R.id.work_offer_add_to_shopping_cart);
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final ItemSearchResult entry = (ItemSearchResult)super.getItem(position);

	holder.sellerText.setText("Seller: " + entry.seller);
	if (entry.binding != null && !"".equals(entry.binding)) {
	    holder.bindingText.setText(entry.binding);
	}
	else {
	    holder.bindingText.setText("Binding: Unspecified");
	}
	holder.conditionText.setText("Condition: " + entry.condition);
	holder.priceText.setText(formatter.format(entry.price));

	ApplicationController app = (ApplicationController)activity.getApplication();
	if (app.isOfferInCart(entry)) {
	    holder.shoppingCartImage.setVisibility(View.INVISIBLE);
	}
	else {
	    holder.shoppingCartImage.setTag(entry);
	    holder.shoppingCartImage.setOnClickListener(activity);
	    holder.shoppingCartImage.setVisibility(View.VISIBLE);
	}
	
	row.setId(position);
	
	return row;
    }
}
