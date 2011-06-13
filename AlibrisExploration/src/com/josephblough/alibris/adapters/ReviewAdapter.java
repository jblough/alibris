package com.josephblough.alibris.adapters;

import java.util.List;

import com.josephblough.alibris.data.Review;
import com.josephblough.alibris.R;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewAdapter extends ArrayAdapter<Review> {
    
    private static final String TAG = "ReviewAdapter";

    private static LayoutInflater inflater = null;

    public ReviewAdapter(Activity context, List<Review> objects) {
	super(context, R.layout.review_row, objects);
	
	Log.d(TAG, "ReviewAdapter");
	
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView nameText;
        public TextView dateText;
        public RatingBar ratingBar;
        public TextView bodyText;
        public ImageView recommendedImage;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;
        ViewHolder holder;

	if (row == null) {
	    row = inflater.inflate(R.layout.review_row, null);
            holder = new ViewHolder();
            holder.nameText = (TextView)row.findViewById(R.id.review_name);
            holder.dateText = (TextView)row.findViewById(R.id.review_date);
            holder.ratingBar = (RatingBar)row.findViewById(R.id.review_rating);
            holder.bodyText = (TextView)row.findViewById(R.id.review_body);
            holder.recommendedImage = (ImageView)row.findViewById(R.id.review_recommends);
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final Review entry = (Review)super.getItem(position);

	holder.nameText.setText(entry.name);
	holder.dateText.setText(entry.date);
	holder.bodyText.setText(Html.fromHtml(entry.body));
	holder.ratingBar.setMax(5);
	holder.ratingBar.setRating(entry.rating);
	
	// If entry.recommends is not null, then set the image and show the imageview
	
	row.setId(entry.id);

	return row;
    }
}
