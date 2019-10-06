package com.dilonexus.sigmalauncher.apps;

public class AppInfo {
    public String packageName;
    public String activityName;
    public String name;
    public long installTime;

    public String getKey(){
        return packageName + ":" + activityName;
    }

    public AppInfo(String packageName, String activityName, String name, long installTime){
        this.packageName = packageName;
        this.activityName = activityName;
        this.name = name;
        this.installTime = installTime;
    }
}
