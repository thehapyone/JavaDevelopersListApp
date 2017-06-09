package com.ayibiowu.ayo.developerlistapp;

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
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ayibiowu.ayo.developerlistapp.MainActivity.LOG_TAG;


/**
 * Helper methods related to requesting and receiving developrer data from GITHUB.
 */
public final class QueryUtils {

   // URL url = new URL("https://developer.github.com/v3/search/#search-users");


    private QueryUtils() {
    }

    public static List<DeveloperProfile> fetchProfileData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // perform http request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpResquest(url);
        }
        catch (IOException e){
            Log.e("MainActivity.class", "Something went wrong");
           // Toast.makeText(QueryUtils.this, "Error, can't connect", Toast.LENGTH_SHORT).show();
            // So exception handler

        }
        List<DeveloperProfile> profile = extractJsonData(jsonResponse);

        return profile;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpResquest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            // Creating the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(20000 /* milliseconds */);
            urlConnection.setConnectTimeout(20000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            InputStreamReader dataIn = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(dataIn);
            // Extracting the data from the inputStream
            String tempData = reader.readLine();
            StringBuilder output = new StringBuilder();
            while (tempData != null) {
                output.append(tempData);
                tempData = reader.readLine();
            }
            jsonResponse = output.toString();

        } catch (IOException e) {
            Log.e("QueryUtils.class", "Connection went wrong");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }

        }
        return jsonResponse;

    }

    /** This is method is suppose to extract the profile details of the user **/

    private static String extractMoreInfo(String profileUrl) {
        final String PROFILURL = "https://api.github.com/users/";
        //String moreData[] = {};
        String userName = "";
        URL url = createUrl(PROFILURL+profileUrl);
        // perform http request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpResquest(url);
        }
        catch (IOException e){
            Log.e("QueryUtilis.class", "Something went wrong");
            // Toast.makeText(QueryUtils.this, "Error, can't connect", Toast.LENGTH_SHORT).show();
            // So exception handler
        }
        // Here we are extracting out the userName and possible any other.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of user objects with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            userName = baseJsonResponse.getString("name");
           // moreData[0] = baseJsonResponse.getString("name");
           // moreData[1] = baseJsonResponse.getString("location");
           // moreData[2] = "Followers: "+baseJsonResponse.getInt("followers");
           // moreData[2] = Integer.toString(baseJsonResponse.getInt("followers"));
           // moreData[3] = Integer.toString(baseJsonResponse.getInt("following"));
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the  JSON results @Extra More data", e);
        }
        //  profile name and the location
        return userName;

    }


    private static List<DeveloperProfile> extractJsonData(String resultJSON) {

//          // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<DeveloperProfile> developerProfiles = new ArrayList<>();


        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of user objects with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject(resultJSON);
            JSONArray profileArray = baseJsonResponse.getJSONArray("items");

            //  profile name and the location


            for (int i=0; i<profileArray.length(); i++) {

                JSONObject currentData = profileArray.getJSONObject(i);
                String profileName = currentData.getString("login");
               
               /* The code below send a new request to the Github API
                 to be able to update the profile name. 
                 This dreastically slows down the app. Uncomment if you want this feature*/

                //String profileName =  extractMoreInfo(userName);
                 profileName = userName;

                DeveloperProfile developerProfile1 = new DeveloperProfile(profileName, userName, "Lagos, Nigeria");
                developerProfiles.add(developerProfile1);

            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the  JSON results", e);
        }

        return developerProfiles;
    }


}
