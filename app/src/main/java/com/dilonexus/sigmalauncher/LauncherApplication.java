package com.dilonexus.sigmalauncher;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dilonexus.sigmalauncher.apps.AppManager;
import com.dilonexus.sigmalauncher.misc.DataSaver;
import com.dilonexus.sigmalauncher.misc.FontManager;
import com.dilonexus.sigmalauncher.misc.Screen;

public class LauncherApplication extends android.app.Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Screen.init(context);
        AppManager.init(context);
        DataSaver.init(context);
        FontManager.init(context);

        Launcher.init(context);
    }

    public static Context getContext() {
        return LauncherApplication.context;
    }
}
