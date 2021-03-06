package com.kenanpulak.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import com.kenanpulak.gridimagesearch.R;
import com.kenanpulak.gridimagesearch.adapters.ImageResultsAdapter;
import com.kenanpulak.gridimagesearch.fragments.FilterFragment;
import com.kenanpulak.gridimagesearch.fragments.FilterFragment.FilterDialogListener;
import com.kenanpulak.gridimagesearch.listeners.EndlessScrollListener;
import com.kenanpulak.gridimagesearch.models.Filter;
import com.kenanpulak.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends FragmentActivity implements FilterDialogListener{

    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private Filter searchFilter;
    private SearchView mSearchView;
    private String mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();

        searchFilter = new Filter();

        //Creates the data source
        imageResults = new ArrayList<ImageResult>();
        // Attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this,imageResults);
        // Link the adapter to the adapterView (gridView)
        gvResults.setAdapter(aImageResults);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(totalItemsCount);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        AsyncHttpClient client = new AsyncHttpClient();
        String searchURL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+ mSearchQuery +"&rsz=8" + "&start=" + offset;

        StringBuilder s = new StringBuilder(100);
        s.append(searchURL);
        if (searchFilter.size != null){
            s.append("&imgsz=");
            s.append(searchFilter.size);
        }
        if (searchFilter.color != null){
            s.append("&imgcolor=");
            s.append(searchFilter.color);
        }
        if (searchFilter.type != null){
            s.append("&imgtype=");
            s.append(searchFilter.type);
        }
        if (searchFilter.site != null){
            s.append("&as_sitesearch=");
            s.append(searchFilter.site);
        }

        client.get(s.toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers,JSONObject response){

                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    //imageResults.clear(); // clear the existing images from the array when theres a new search
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupViews(){
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

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchQuery = query;
                newSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    public void onFilterAction(MenuItem mi) {
        showFilterFragment();
    }

    private void showFilterFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterFragment = FilterFragment.newInstance("Filter");
        Bundle bundle = new Bundle();
        bundle.putParcelable("filter",searchFilter);
        filterFragment.setArguments(bundle);
        filterFragment.show(fm, "fragment_filter");
    }

    public void newSearch(){

        aImageResults.clear();

        AsyncHttpClient client = new AsyncHttpClient();
        String searchURL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+ mSearchQuery +"&rsz=8";

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

        StringBuilder s = new StringBuilder(100);
        s.append(searchURL);
        if (searchFilter.size != null){
            s.append("&imgsz=");
            s.append(searchFilter.size);
        }
        if (searchFilter.color != null){
            s.append("&imgcolor=");
            s.append(searchFilter.color);
        }
        if (searchFilter.type != null){
            s.append("&imgtype=");
            s.append(searchFilter.type);
        }
        if (searchFilter.site != null){
            s.append("&as_sitesearch=");
            s.append(searchFilter.site);
        }

        client.get(s.toString(), new JsonHttpResponseHandler(){
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
                if(aImageResults.getCount() <= 8){
                    //customLoadMoreDataFromApi(8);
                }
            }
        });
    }

    @Override
    public void onFinishFilterDialog(Filter filter)
    {
        searchFilter = filter;
    }
}
