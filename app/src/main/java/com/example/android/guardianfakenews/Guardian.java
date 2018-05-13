package com.example.android.guardianfakenews;

/**
 * Created by Tudor on 07.07.2017.
 */

public class Guardian {

    private final String mTitle;
    private final String mSection;
    private final String mId;
    private final String mUrl;

    public Guardian(String title, String section, String id, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getId() {
        return mId;
    }
}
