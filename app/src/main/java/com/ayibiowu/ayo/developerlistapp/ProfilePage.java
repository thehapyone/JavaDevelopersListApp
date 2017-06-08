package com.ayibiowu.ayo.developerlistapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class ProfilePage extends AppCompatActivity {
    private static final String gitHubProfileUrl = "https://api.github.com/users/";
    private static final String MainGitHubProfileUrl = "https://github.com/users/";

    private String userName ="";
    private String profileName ="";
    private String myGitProfile = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        setTitle("Profile Page");

        userName = getIntent().getStringExtra("USERNAME");
        profileName = getIntent().getStringExtra("FULLNAME");

        // Update the Profile Name
        TextView myName = (TextView) findViewById(R.id.profile_name);
        myName.setText(profileName);

        // Using StrictMode to enforce the Networking Thread, since no AsyncTask is implemented
        // The activity will crash.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        getAvatarImage();

    }
    /** Go back to the Home Page **/

    public void HomeButton (View view) {
        // go home;
        Intent goHome = new Intent(this, MainActivity.class);
        startActivity(goHome);
    }

    /** Method For Sharing Profile Information
     * It uses the ACTION_SEND Intent **/

    public void ShareButton (View view) {
        // go share;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        share.putExtra(Intent.EXTRA_SUBJECT, "GitHub Profile");
        share.putExtra(Intent.EXTRA_TEXT, myGitProfile);

        startActivity(Intent.createChooser(share, "Share Profile!"));
    }
    /** Method For getting the Profile Avatar
     * I leverage on the Universal Image Loader Library
     * More Info - https://github.com/nostra13/Android-Universal-Image-Loader
     * **/
    public void getAvatarImage(){
        myGitProfile = MainGitHubProfileUrl+userName;

        URL profileURL = QueryUtils.createUrl(gitHubProfileUrl+userName);
        String jsonResponse = "";
        String avatarLink = "";

        // First Update the GITHUB Profile link information
        TextView gitUrl = (TextView) findViewById(R.id.profile_url);
        gitUrl.setText(myGitProfile);
        // Connecting...
        try {
            jsonResponse = QueryUtils.makeHttpResquest(profileURL);
        }
        catch (IOException e){
            Log.e("ProfilePage.class", "Something went wrong");
             Toast.makeText(ProfilePage.this, "Error, Can't connect to Network", Toast.LENGTH_SHORT).show();
            // So exception handler
        }
        // Here we are extracting out the avatar link.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of user objects with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            avatarLink = baseJsonResponse.getString("avatar_url");

            Log.i("Test", avatarLink);

        } catch (JSONException e) {

            Log.e("ProfilePage.java", "Problem parsing the  JSON results", e);
        }

        if (avatarLink != null) {
            // Callimg the imageLoader class for viewing the image

            ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
            ImageView profileImage = (ImageView) findViewById(R.id.avatar_image_view);

            imageLoader.displayImage(avatarLink,profileImage);
        }


    }


}
