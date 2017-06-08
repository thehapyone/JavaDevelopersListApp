package com.ayibiowu.ayo.developerlistapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by charl on 6/8/2017.
 */

public class ProfileLoader extends AsyncTaskLoader{

    /** Tag for log messages */
  //  private static final String LOG_TAG = ProfileLoader.class.getName();

    /* Query URL*/
    private String mUri;

    public ProfileLoader(Context context, String url){
        super(context);
        this.mUri = url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */

    @Override
    public List<DeveloperProfile> loadInBackground() {
        if (this.mUri == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of European news.
        List<DeveloperProfile> profile = QueryUtils.fetchProfileData(mUri);
        return profile;
    }
}
