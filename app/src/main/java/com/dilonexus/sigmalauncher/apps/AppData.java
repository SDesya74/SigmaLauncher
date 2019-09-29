package com.dilonexus.sigmalauncher.apps;

import java.io.Serializable;

public class AppData implements Serializable {
    public long uniqueID;
    private String packageName;
    public String getPackage(){
        return  packageName;
    }
    private String activityName;
    public String getActivity() {
        return activityName;
    }

    private String name;
    public String getName(){
        return name;
    }

    // region Popularity
    private int popularity;
    public int getPopularity(){
        return popularity;
    }

    public void setPopularity(int popularity){
        this.popularity = popularity;
    }

    public void addPopularity(int popularity){
        this.setPopularity(getPopularity() + popularity);
    }
    // endregion


    private long genUniqueId(){
        return Math.round(Math.random() * 10_000_000_000_000_000L);
    }
    public AppData(String packageName, String activityName, String name, int popularity){
        this.uniqueID = this.genUniqueId();
        while(AppManager.getAppByID(this.uniqueID) != null) this.uniqueID = genUniqueId();
        this.packageName = packageName;
        this.activityName = activityName;
        this.name = name;
        this.popularity = popularity;
    }
}
