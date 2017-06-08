package com.ayibiowu.ayo.developerlistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by charl on 6/8/2017.
 */

public class ProfileListAdapter extends ArrayAdapter<DeveloperProfile> {


    public ProfileListAdapter (Context content, List<DeveloperProfile> developerProfiles){
        super(content, 0, developerProfiles);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_tree, parent, false);
        }

        // Get the developerProfile object located at this position in the list
        DeveloperProfile developerProfile = getItem(position);

        // Find the TextView in the list_tree.xml layout with the ID version_name
        TextView profileName = (TextView) listItemView.findViewById(R.id.developerName);
        // Get the version name from the current developerProfile object and
        // set this text on the name TextView
        profileName.setText(developerProfile.getName());

        // Find the TextView in the list_tree.xml layout with the ID version_name
        TextView userName = (TextView) listItemView.findViewById(R.id.userName);
        // Get the version name from the current developerProfile object and
        // set this text on the name TextView
        userName.setText(developerProfile.getUserName());

        // Find the TextView in the list_tree.xml layout with the ID version_number
        TextView locationView = (TextView) listItemView.findViewById(R.id.locationText);
        // Get the version number from the current developerProfile object and
        // set this text on the number TextView
        locationView.setText(developerProfile.getMyLocation());
//
//        // Find the TextView in the list_tree.xml layout with the ID version_name
//        TextView followers = (TextView) listItemView.findViewById(R.id.followersText);
//        // Get the version name from the current developerProfile object and
//        // set this text on the name TextView
//        followers.setText(developerProfile.getMyFollowers());
//
//        // Find the TextView in the list_tree.xml layout with the ID version_name
//        TextView following = (TextView) listItemView.findViewById(R.id.followingText);
//        // Get the version name from the current developerProfile object and
//        // set this text on the name TextView
//        following.setText(developerProfile.getMyFollowing());

        // Return the whole list item layout
        // so that it can be shown in the ListView
        return listItemView;
    }

}


