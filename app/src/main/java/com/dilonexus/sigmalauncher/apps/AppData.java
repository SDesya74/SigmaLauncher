package com.dilonexus.sigmalauncher.apps;

import java.io.Serializable;

public class AppData implements Serializable {
    long uniqueID;
    long installTime;

    // region Package
    private String packageName;
    public String getPackage(){
        return  packageName;
    }
    // endregion

    // region Activity
    private String activityName;
    String getActivity() {
        return activityName;
    }
    // endregion

    // region Name
    private String name;
    String getName(){
        return name;
    }
    // endregion

    // region Popularity
    private int popularity;
    int getPopularity(){
        return popularity;
    }

    private void setPopularity(int popularity){
        this.popularity = popularity;
    }

    public void addPopularity(int popularity){
        this.setPopularity(getPopularity() + popularity);
    }
    // endregion

    // region Unique ID
    private long genUniqueId(){
        return Math.round(Math.random() * 10_000_000_000_000_000L);
    }
    AppData(String packageName, String activityName, String name, int popularity){
        this.uniqueID = this.genUniqueId();
        while(AppManager.getAppByID(this.uniqueID) != null) this.uniqueID = genUniqueId();
        this.packageName = packageName;
        this.activityName = activityName;
        this.name = name;
        this.popularity = popularity;
    }
    // endregion
}
