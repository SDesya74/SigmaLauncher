package com.dilonexus.sigmalauncher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dilonexus.sigmalauncher.apps.AppManager;
import com.dilonexus.sigmalauncher.misc.DataSaver;
import com.dilonexus.sigmalauncher.misc.Screen;
import com.dilonexus.sigmalauncher.views.LoadingScreenView;
import com.dilonexus.sigmalauncher.views.widgets.AppGroupView;

class Launcher {
    private static Handler handler = new Handler();

    // region Context
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    static Context getContext() {
        return context;
    }
    // endregion

    // region Views
    @SuppressLint("StaticFieldLeak")
    static FrameLayout parent;
    @SuppressLint("StaticFieldLeak")
    static ScrollView scroll;
    @SuppressLint("StaticFieldLeak")
    static LinearLayout main;

    static FrameLayout getParent() {
        return parent;
    }
    // endregion

    static void init(Context c) {
        context = c;

        parent = new FrameLayout(context);

        scroll = new ScrollView(context);
        parent.addView(scroll);

        main = new LinearLayout(context);
        main.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(main);
    }


    static void invalidate() {
        clear();
        build();
    }

    static void show(Activity activity) {
        activity.setContentView(getParent());
    }


    static void startLoadingApps() {
        final LoadingScreenView loadingScreen = new LoadingScreenView(context);
        getParent().addView(loadingScreen);

        AppManager.loadAppsResolve();

        // region Load Cached App List
/*
        @SuppressWarnings("unchecked")
        List<AppData> apps = (List<AppData>) DataSaver.readObject("apps");
        if (apps != null && apps.size() == AppManager.getAppResolveList().size()) {
            AppManager.setApps(apps);

            invalidate();
            getParent().removeView(loadingScreen);

            return;
        }
*/
        // endregion

        // region Load App List In Handler
        handler.post(new Runnable() {
            public void run() {
                boolean stop = AppManager.loadNextApp();
                loadingScreen.setProgress(AppManager.getLoadingProgress());
                if (!stop) handler.post(this);
                else {
                    invalidate();
                    getParent().removeView(loadingScreen);

                    DataSaver.saveObject("apps", AppManager.getApps());
                }
            }
        });
        // endregion
    }





















    static void clear(){
        main.removeAllViews();
    }

    static void build(){
        AppGroupView group = new AppGroupView(context);

        int p = Screen.dip(5);
        group.setPadding(p, p, p, p);
        group.setWidth(Screen.getWidth());
        group.setItems(AppManager.getApps());
        Toast.makeText(getContext(), "rows: " + group.getRows().size(), Toast.LENGTH_SHORT).show();

        main.addView(group);
    }
}
