package com.kenanpulak.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kenanpulak.gridimagesearch.R;
import com.kenanpulak.gridimagesearch.TouchImageView;
import com.kenanpulak.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        //Remove the actionbar on the image dispaly activity
        getActionBar().hide();
        // Pull out the url from the intent
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        // Find the image view
        TouchImageView ivImageResult = (TouchImageView) findViewById(R.id.ivImageResult);
        // Load the image url into the imageview using picasso
        Picasso.with(this).load(result.fullURL).into(ivImageResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
