package com.example.android.guardianfakenews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Tudor on 09.07.2017.
 */

public class GuardianLoader extends AsyncTaskLoader<List<Guardian>> {

    // Tags for log messages.
    private static final String LOG_TAG = GuardianLoader.class.getSimpleName();

    // Query URL.
    private final String mUrl;

    /**
     * Constructs a new {@link GuardianLoader}
     *
     * @param context
     * @param url
     */
    public GuardianLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Guardian> loadInBackground() {

        // Purpose for the background thread.
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of guardianList.
        List<Guardian> guardianList = QueryUtils.fetchGuardianData(mUrl);
        return guardianList;
    }

}
