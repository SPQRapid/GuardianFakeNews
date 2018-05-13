package com.example.android.guardianfakenews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tudor on 09.07.2017.
 */

public class QueryUtils {

    // Tag for log messages.
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Empty constructor.
    private QueryUtils() {

    }

    public static List<Guardian> fetchGuardianData(String requestUrl) {
        // Create URL object.
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back.
        String jsonResponse = null;
        try {
            jsonResponse = createHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Guardian}.
        List<Guardian> guardianList = extractFeatureFromJson(jsonResponse);

        // Return the list of the {@link Guardian}.
        return guardianList;
    }

    /**
     * Returns new URL object from the given stringUrl.
     *
     * @param stringUrl
     * @return
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem creating url", e);
        }
        return url;
    }

    private static String createHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

        // If the url is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000 /*milliseconds */);
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON. " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Creating a String builder so that we can uses it in the
     * fetchGuardianData method.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Return a list of {@link Guardian} object that has been build up from
     * parsing the given JSON response.
     *
     * @param jsonResponse
     * @return
     */
    private static List<Guardian> extractFeatureFromJson(String jsonResponse) {

        if (jsonResponse == null) {
            return null;
        }

        List<Guardian> guardianList = new ArrayList<>();

        try {
            JSONObject mainJson = new JSONObject(jsonResponse);
            JSONObject responseJson = mainJson.getJSONObject("response");
            JSONArray resultsJson = responseJson.getJSONArray("results");
            for (int i = 0; i < resultsJson.length(); i++) {
                JSONObject guardianObject = resultsJson.getJSONObject(i);
                String sectionId = guardianObject.getString("sectionId");
                String sectionName = guardianObject.getString("sectionName");
                String webTitle = guardianObject.getString("webTitle");
                String webUrl = guardianObject.getString("webUrl");

                Guardian guardian = new Guardian(webTitle, sectionName, sectionId, webUrl);
                guardianList.add(guardian);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problems parsing the JSON. ", e);
        }
        return guardianList;
    }
}
