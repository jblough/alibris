package com.josephblough.alibris.adapters;

import java.text.NumberFormat;
import java.util.List;

import com.josephblough.alibris.data.ItemSearchResult;
import com.josephblough.alibris.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WorkOfferAdapter extends ArrayAdapter<ItemSearchResult> {
    
    private static final String TAG = "WorkOfferAdapter";

    private static LayoutInflater inflater = null;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public WorkOfferAdapter(Activity context, List<ItemSearchResult> objects) {
	super(context, R.layout.work_offer_row, objects);
	
	Log.d(TAG, "WorkOfferAdapter");
	
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView sellerText;
        public TextView bindingText;
        public TextView conditionText;
        public TextView priceText;
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
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final ItemSearchResult entry = (ItemSearchResult)super.getItem(position);

	holder.sellerText.setText("Seller: " + entry.seller);
	holder.bindingText.setText(entry.binding);
	holder.conditionText.setText("Condition: " + entry.condition);
	holder.priceText.setText(formatter.format(entry.price));
	
	// If entry.recommends is not null, then set the image and show the imageview
	
	row.setId(position);
	
	return row;
    }
}
