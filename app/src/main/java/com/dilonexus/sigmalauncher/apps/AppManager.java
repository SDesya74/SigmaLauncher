package com.dilonexus.sigmalauncher.apps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import com.dilonexus.sigmalauncher.Launcher;
import com.dilonexus.sigmalauncher.misc.DataSaver;
import com.dilonexus.sigmalauncher.misc.Options;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AppManager {
    private static List<AppInfo> infoList;
    private static List<AppData> dataList = new ArrayList<>();
    private static PackageManager manager;

    public static void init(Context context) {
        infoList = new ArrayList<>();
        dataList = new ArrayList<>();

        manager = context.getPackageManager();
    }


    public static void clear() {
        infoList.clear();
        dataList.clear();
    }

    public static List<AppInfo> getInfoList() {
        return infoList;
    }
    public static AppInfo getAppInfo(String key) {
        for (AppInfo e : infoList) {
            if (e.getKey().equals(key)) return e;
        }
        return null;
    }


    public static void setDataList(List<AppData> list) {
        dataList = list;
    }
    public static AppData getAppData(String key) {
        for (AppData e : dataList) {
            if (e.getKey().equals(key)) return e;
        }
        return null;
    }


    public static void startApp(AppInfo info) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(info.packageName, info.activityName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            AppData data = getAppData(info.getKey());

            assert data != null;
            data.popularity++;

            Launcher.getContext().startActivity(intent);
            AppManager.save();
        } catch (Exception e) {
            Toast.makeText(Launcher.getContext(), "Activity Not Found", Toast.LENGTH_SHORT).show();
        }
    }



    public static void save() {
        if (Options.APP_CACHE_ENABLED) DataSaver.saveObject("apps/info", infoList);
        DataSaver.saveObject("apps/data", dataList);
    }

    public abstract static class LoadingListener {
        public abstract void onAppLoaded(float progess);

        public abstract void onLoadingEnd();
    }

    public static void load(final LoadingListener listener) {
        new Thread() {
            @Override
            public void run() {
                loadDataList();

                Intent loader = new Intent(Intent.ACTION_MAIN, null);
                loader.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> resolve = manager.queryIntentActivities(loader, 0);

                if (Options.APP_CACHE_ENABLED) {
                    //noinspection unchecked
                    infoList = (List<AppInfo>) DataSaver.readObject("apps/info");
                    if (infoList != null && infoList.size() == resolve.size()){
                       listener.onLoadingEnd();
                       return;
                    }
                }

                float progress = 0f;
                infoList = new ArrayList<>();
                for (ResolveInfo res : resolve) {
                    AppInfo info = loadAppInfo(res);
                    if (getAppData(info.getKey()) == null) dataList.add(new AppData(info.getKey()));
                    infoList.add(info);

                    listener.onAppLoaded(progress);
                    progress += 1 / (float) resolve.size();
                }


                listener.onLoadingEnd();
            }
        }.start();

        /*
        loadingHandler.post(new Runnable() {
            public void run() {
                boolean stop = AppManager.loadNextApp();
                loadingScreen.setProgress(AppManager.getLoadingProgress());
                if (!stop) handler.post(this);
                else {
                    getParent().removeView(loadingScreen);
                    AppManager.save();

                    load();

                    onEndLoading();
                }
            }
        });
        */
    }

    private static AppInfo loadAppInfo(ResolveInfo info) {
        String packageName = info.activityInfo.packageName;
        String activityName = info.activityInfo.name;
        String name = info.loadLabel(manager).toString();
        // Drawable icon = info.loadIcon(manager);

        // region Install Time
        long time = -1;
        try {
            Field field = PackageInfo.class.getField("firstInstallTime");
            time = field.getLong(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (time < 0) {
            try {
                ApplicationInfo data = manager.getApplicationInfo(packageName, 0);
                File apkFile = new File(data.sourceDir);
                if (apkFile.exists()) time = apkFile.lastModified();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        // endregion

        return new AppInfo(packageName, activityName, name, time);
    }

    private static void loadDataList() {
        //noinspection unchecked
        dataList = (List<AppData>) DataSaver.readObject("apps/data");
        if (dataList == null) dataList = new ArrayList<>();
    }
}
