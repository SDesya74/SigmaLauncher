package com.dilonexus.sigmalauncher.apps;

import java.io.Serializable;

public class AppData implements Serializable {
    public long uniqueID;
    private String packageName;
    private String activityName;
    private String name;

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

    public AppData(String packageName, String activityName, String name, int popularity){
        this.uniqueID = Math.round(Math.random() * 10000000000000000L);
        this.packageName = packageName;
        this.activityName = activityName;
        this.name = name;
        this.popularity = popularity;
    }


}
