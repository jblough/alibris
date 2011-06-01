package com.josephblough.alibris.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    
    public ImageDownloaderTask(final ImageView imageView) {
	this.imageView = imageView;
    }
    
    @Override
    protected Bitmap doInBackground(String... params) {
	return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        
        if (this.imageView != null && this.imageView.getVisibility() == View.VISIBLE)
            this.imageView.setImageBitmap(result);
    }
}
