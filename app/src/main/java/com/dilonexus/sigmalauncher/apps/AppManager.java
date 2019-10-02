package com.dilonexus.sigmalauncher.apps;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import com.dilonexus.sigmalauncher.LauncherApplication;
import com.dilonexus.sigmalauncher.misc.DataSaver;

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

    public static void startApp(AppData app){
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(app.getPackage(), app.getActivity()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            app.addPopularity(1);
            LauncherApplication.getContext().startActivity(intent);
            DataSaver.saveObject("apps", apps);
        }catch(ActivityNotFoundException e){
            Toast.makeText(LauncherApplication.getContext(), "Activity Not Found", Toast.LENGTH_SHORT).show();
        }
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
