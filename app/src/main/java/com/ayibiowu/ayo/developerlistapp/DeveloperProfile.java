package com.ayibiowu.ayo.developerlistapp;

/**
 * Created by charl on 6/8/2017.
 */

public class DeveloperProfile {

    private String myName;
    private String myLocation;
    private String myUserName;
    private String myFollowers;
    private String myFollowing;

//    public DeveloperProfile(String myName,  String myUserName, String myLocation, String myFollowers, String myFollowing){
//        this.myName = myName;
//        this.myLocation = myLocation;
//        this.myUserName = myUserName;
//        this.myFollowers = myFollowers;
//        this.myFollowing = myFollowing;
//    }

    public DeveloperProfile(String myName,  String myUserName, String myLocation){
        this.myName = myName;
        this.myLocation = myLocation;
        this.myUserName = myUserName;

    }

    public String getName()
    {
        return myName;
    }

    public String getMyLocation()
    {
        return myLocation;
    }

    // Could be useful for future version
    public String getUserName(){ return myUserName; }
    public String getMyFollowers() { return myFollowers;}
    public String getMyFollowing() { return myFollowing;}


}
