package com.dilonexus.sigmalauncher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dilonexus.sigmalauncher.apps.AppData;
import com.dilonexus.sigmalauncher.apps.AppManager;
import com.dilonexus.sigmalauncher.misc.DataSaver;
import com.dilonexus.sigmalauncher.misc.Options;
import com.dilonexus.sigmalauncher.misc.Screen;
import com.dilonexus.sigmalauncher.views.LoadingScreenView;
import com.dilonexus.sigmalauncher.views.widgets.AppGroupView;

import java.util.List;

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
    private static FrameLayout parent;
    @SuppressLint("StaticFieldLeak")
    private static ScrollView scroll;
    @SuppressLint("StaticFieldLeak")
    private static LinearLayout main;

    private static FrameLayout getParent() {
        return parent;
    }
    private static ScrollView getScroll() {
        return scroll;
    }
    private static LinearLayout getMain() {
        return main;
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
        Screen.init(context);

        clear();
        build();
    }


    static void show(Activity activity) {
        ViewGroup activityView = (ViewGroup) getParent().getParent();
        if(activityView != null) activityView.removeView(getParent());
        activity.setContentView(getParent());
    }

    static void startLoadingApps() {
        final LoadingScreenView loadingScreen = new LoadingScreenView(context);
        getParent().addView(loadingScreen);

        AppManager.clear();
        AppManager.loadAppsResolve();

        // region Load Cached App List

        if(Options.APP_CACHE_ENABLED){
            @SuppressWarnings("unchecked")
            List<AppData> apps = (List<AppData>) DataSaver.readObject("apps");
            if (apps != null && apps.size() == AppManager.getAppResolveList().size()) {
                AppManager.setApps(apps);

                invalidate();
                getParent().removeView(loadingScreen);

                return;
            }
        }

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





    private static void clear(){
        main.removeAllViews();
    }

    private static void build(){
        Screen.init(context);

        AppGroupView group = new AppGroupView(context);

        int p = Screen.dip(5);
        group.setPadding(p, p, p, p);
        group.setItems(AppManager.getApps());

        main.addView(group);
    }
}
