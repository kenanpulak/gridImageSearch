package com.kenanpulak.gridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.kenanpulak.gridimagesearch.R;
import com.kenanpulak.gridimagesearch.adapters.ImageResultsAdapter;
import com.kenanpulak.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends Activity {

    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        //Creates the data sourcs
        imageResults = new ArrayList<ImageResult>();
        // Attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this,imageResults);
        // Link the adapter to the adapterView (gridView)
        gvResults.setAdapter(aImageResults);
    }

    private void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Launch the image display activity
                // Creating an intent
                Intent i = new Intent(SearchActivity.this,ImageDisplayActivity.class);
                // Get the image result to display
                ImageResult result = imageResults.get(position);
                // Pass image result into the intent
                i.putExtra("result",result);
                //Launch the new activity
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Fired whenever the button is pressed
    public void onImageSearch(View v){
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String searchURL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+ query +"&rsz=8";
        client.get(searchURL, new JsonHttpResponseHandler(){
            @Override
        public void onSuccess(int statusCode, Header[] headers,JSONObject response){

                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); // clear the existing images from the array when theres a new search
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

}
