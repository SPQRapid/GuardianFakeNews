package com.example.android.guardianfakenews;

import android.os.Handler;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GuardianActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Guardian>> {

    private static final String GUARDIAN_URL = "https://content.guardianapis.com/search?api-key=a4541cfb-c2a5-4af9-9a06-b11da6c0638b";

    // Adapter for the list of news.
    private GuardianAdapter mAdapter;

    // A constant value for the Guardian loader ID.
    private static final int GUARDIAN_LOADER_ID = 0;

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        // Start long running operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while ((progressStatus < 100)) {
                    progressStatus += 1;
                    // Update the progress bar and display the current value in TextView
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus + "/" + progressBar.getMax());

                        }
                    });

                    try {
                        Thread.sleep(10 /* milliseconds */);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        ListView guardianListView = (ListView) findViewById(R.id.list);

        mAdapter = new GuardianAdapter(this, new ArrayList<Guardian>());

        guardianListView.setAdapter(mAdapter);

        guardianListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Guardian guardian = mAdapter.getItem(i);
                Uri guardianUri = Uri.parse(guardian.getUrl());
                Intent guardianIntent = new Intent(Intent.ACTION_VIEW, guardianUri);
                startActivity(guardianIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(GUARDIAN_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Guardian>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String numberOfArticles = sharedPreferences.getString(getString(R.string.settings_number_of_articles_key),
                getString(R.string.settings_number_of_articles_default));

        Uri baseUri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("page-size", numberOfArticles);

        return new GuardianLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Guardian>> loader, List<Guardian> guardianList) {
        mAdapter.clear();

        if (guardianList != null && !guardianList.isEmpty()) {

            // Hiding the progress bar.
            View loadingIndicator = findViewById(R.id.progressBar_cyclic);
            loadingIndicator.setVisibility(View.GONE);

            // Hiding the progress bar.
            View secondLoadingIndicator = findViewById(R.id.progressBar);
            secondLoadingIndicator.setVisibility(View.GONE);

            // Hiding the TextView of the loading percentage.
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setVisibility(View.GONE);
            mAdapter.addAll(guardianList);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Guardian>> loader) {
        mAdapter.clear();
    }
}
