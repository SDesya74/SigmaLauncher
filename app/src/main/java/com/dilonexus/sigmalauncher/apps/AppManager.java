package com.dilonexus.sigmalauncher.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

public class AppManager {
    private static List<AppData> apps;
    private static PackageManager manager;
    public static void init(Context context){
        apps = new ArrayList<>();
        manager = context.getPackageManager();
    }

    public static List<AppData> getApps(){
        return apps;
    }
    public static void setApps(List<AppData> list){
        apps = list;
    }
    public static AppData getAppByID(long id){
        for(AppData app : apps){
            if(app.uniqueID == id) return app;
        }
        return null;
    }

    // region Loading Apps
    private static List<ResolveInfo> appResolveList;
    public static List<ResolveInfo> getAppResolveList(){
        return appResolveList;
    }
    public static void loadAppsResolve(){
        Intent loader = new Intent(Intent.ACTION_MAIN, null);
        loader.addCategory(Intent.CATEGORY_LAUNCHER);
         appResolveList = manager.queryIntentActivities(loader, 0);
    }
    private static void loadApp(ResolveInfo info){
        String packageName = info.activityInfo.packageName;
        String activityName = info.activityInfo.name;
        String name = info.loadLabel(manager).toString();
        // Drawable icon = info.loadIcon(manager);
        AppData app = new AppData(packageName, activityName, name, 0);
        apps.add(app);
    }
    private static int loadingIndex;
    public static boolean loadNextApp(){
        loadApp(appResolveList.get(loadingIndex++));
        return getLoadingProgress() >= 1;
    }
    public static double getLoadingProgress(){
        return loadingIndex / (double) appResolveList.size();
    }
    // endregion
}
