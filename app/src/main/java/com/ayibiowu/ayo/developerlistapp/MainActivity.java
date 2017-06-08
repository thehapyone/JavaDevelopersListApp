package com.ayibiowu.ayo.developerlistapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<DeveloperProfile>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String API_REQUEST_URL =
            "https://api.github.com/search/users?q=location:lagos+type:user+language:java";


    /**
     * Constant value for the loader ID.
     */
    private static final int LOADER_ID = 1;

    // Adapter for the list of Developers
    private ProfileListAdapter mAdapter;

    /**TextView to be displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        // Find a reference to the {@link ListView} in the layout
        ListView developerProfileList = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        developerProfileList.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list as input
        mAdapter = new ProfileListAdapter(this, new ArrayList<DeveloperProfile>());

        // so the list can be populated in the user interface
        developerProfileList.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to another activity
        developerProfileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // find the current profile that was clicked on
                DeveloperProfile currentProfilePick = mAdapter.getItem(position);
                // Here we want to extract the userName and possible full name
                // to passed to the next activity.
               String userName = currentProfilePick.getUserName();
               String profileName = currentProfilePick.getName();

                // create a new intent to launch activity
                // Intent profilePageIntent = new Intent(I)
                launchProfilePage(userName, profileName);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            // Otherwise, display error. First, hide loading indicator so error message will be
            // visible.
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("No Internet Connection");
        }


    }

    public void launchProfilePage(String userName, String profileName){
        Intent profilePageIntent = new Intent(this, ProfilePage.class);
        profilePageIntent.putExtra("USERNAME", userName);
        profilePageIntent.putExtra("FULLNAME", profileName);
        startActivity(profilePageIntent);
    }

    @Override
    public Loader<List<DeveloperProfile>> onCreateLoader(int i, Bundle bundle) {


     //   return new ProfileLoader(this, uriBuilder.toString());
        Uri baseUri = Uri.parse(API_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new ProfileLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<DeveloperProfile>> loader, List<DeveloperProfile> profileID) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No articles found."
        mEmptyStateTextView.setText("No Profile Found");

        // Clear the adapter
        mAdapter.clear();

        // If there is a valid list of {@link DeveloperProfile}, then add them to the adapter's data
        // set. This will trigger the ListView to update.
        if (profileID != null && !profileID.isEmpty()) {
            mAdapter.addAll(profileID);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DeveloperProfile>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}


