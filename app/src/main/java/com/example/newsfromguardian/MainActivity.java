package com.example.newsfromguardian;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    private String initialURL = "https://content.guardianapis.com/search?api-key=79d3892b-f836-46ad-91ef-9e444bedf0b5&q=";
    private String NEWS_REQUEST_URL = "";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter adapter;
    private TextView mEmptyStateTextView;
    EditText searchEditText;
    View loadingIndicator;
    ListView newsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the views with their IDs
        searchEditText = findViewById(R.id.searchET);
        newsListView = findViewById(R.id.newsListview);
        loadingIndicator = findViewById(R.id.loading_indicator);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        Button searchButton = findViewById(R.id.searchButton);

        //Search button onCLickListener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                mEmptyStateTextView.setVisibility(GONE);
                searchEditText.getText().clear();
                loadingIndicator.setVisibility(View.VISIBLE);

                String query = searchEditText.getText().toString();
                String[] queries = query.split(" ");
                String addOn = "";
                for(int i = 0;i<queries.length;i++){
                    addOn += queries[i];
                }
                //adding the query entered by the student to the request url
                NEWS_REQUEST_URL = initialURL + addOn;


                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                //checking if there is internet connectivity
                if (networkInfo != null && networkInfo.isConnected()){
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.restartLoader(NEWS_LOADER_ID,null,MainActivity.this);
                }  else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    loadingIndicator.setVisibility(GONE);
                    newsListView.setEmptyView(mEmptyStateTextView);
                    mEmptyStateTextView.setText("No Internet Connection");
                }

            }
        });

        adapter = new NewsAdapter(this,R.layout.listview_adapter, new ArrayList<News>());
        newsListView.setAdapter(adapter);
        newsListView.setClickable(true);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = adapter.getItem(position);
                Uri bookListingUri = Uri.parse(news.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,bookListingUri);
                startActivity(websiteIntent);

            }
        });
        newsListView.setAdapter(adapter);


    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.e("", "onCreateLoader ");
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        adapter.clear();
        if (news != null && !news.isEmpty()) {
            newsListView.setVisibility(View.VISIBLE);
            adapter.addAll(news);
        }else if(news == null || news.isEmpty()){
            mEmptyStateTextView.setText("No News for now");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            newsListView.setVisibility(GONE);
            loadingIndicator.setVisibility(GONE);
        }
        loadingIndicator.setVisibility(GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}
