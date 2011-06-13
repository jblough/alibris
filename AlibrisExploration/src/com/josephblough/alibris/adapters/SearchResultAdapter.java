package com.josephblough.alibris.adapters;

import java.text.NumberFormat;
import java.util.List;

import com.josephblough.alibris.data.WorkSearchResult;
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

public class SearchResultAdapter extends ArrayAdapter<WorkSearchResult> {
    
    private static final String TAG = "SearchResultAdapter";

    private static NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private LayoutInflater inflater = null;
    private ApplicationController app;

    public SearchResultAdapter(Activity context, List<WorkSearchResult> objects) {
	super(context, R.layout.search_result_row, objects);
	
	Log.d(TAG, "SearchResultAdapter");
	
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        app = (ApplicationController)context.getApplicationContext();
    }

    public static class ViewHolder{
        public TextView titleText;
        public TextView authorText;
        public TextView priceText;
        public ImageView image;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;
        ViewHolder holder;

	if (row == null) {
	    row = inflater.inflate(R.layout.search_result_row, null);
            holder = new ViewHolder();
            holder.titleText = (TextView)row.findViewById(R.id.search_result_title);
            holder.authorText = (TextView)row.findViewById(R.id.search_result_author);
            holder.priceText = (TextView)row.findViewById(R.id.search_result_lowest_price);
            holder.image = (ImageView)row.findViewById(R.id.search_result_image);
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final WorkSearchResult entry = (WorkSearchResult)super.getItem(position);

	holder.titleText.setText(entry.title);
	holder.authorText.setText(entry.author);
	try {
	    if (entry.minPrice == null)
		holder.priceText.setText("");
	    else
		holder.priceText.setText("Lowest price: " + formatter.format(entry.minPrice));
	}
	catch (NumberFormatException e) {
	    Log.e(TAG, e.getMessage(), e);
	    holder.priceText.setText("");
	}
	
	holder.image.setTag(entry.imageURL);
	app.imageLoader.displayImage(entry.imageURL, holder.image);
	row.setId(entry.workId);
	
	return row;
    }
}
