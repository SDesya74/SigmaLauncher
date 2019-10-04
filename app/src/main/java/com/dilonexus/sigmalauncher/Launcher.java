package com.dilonexus.sigmalauncher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dilonexus.sigmalauncher.apps.AppData;
import com.dilonexus.sigmalauncher.apps.AppManager;
import com.dilonexus.sigmalauncher.apps.AppSorter;
import com.dilonexus.sigmalauncher.misc.DataSaver;
import com.dilonexus.sigmalauncher.misc.Options;
import com.dilonexus.sigmalauncher.misc.Screen;
import com.dilonexus.sigmalauncher.views.LoadingScreenView;
import com.dilonexus.sigmalauncher.views.widgets.AppGroup.AppGroupData;
import com.dilonexus.sigmalauncher.views.widgets.AppGroup.AppGroupView;
import com.dilonexus.sigmalauncher.views.widgets.WidgetData;

import java.util.ArrayList;
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
    /*private static ScrollView getScroll() {
        return scroll;
    }*/
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

        initWidgets();
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







    private static List<WidgetData> widgets;
    private static List<WidgetData> getWidgets(){
        return widgets;
    }

    private static void initWidgets(){
        if(widgets != null && widgets.size() > 0) return;

        @SuppressWarnings("unchecked")
        List<WidgetData> list = (List<WidgetData>) DataSaver.readObject("widgets");
        if(list != null){
            widgets = list;
            Toast.makeText(context, "Data loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        widgets = new ArrayList<>();

        AppGroupData appGroup = new AppGroupData(AppManager.getApps());
        appGroup.setSortType(AppSorter.SortType.APP_NAME);
        appGroup.setPadding(Screen.dip(5), Screen.dip(5));
        appGroup.setMargin(Screen.dip(5), Screen.dip(5));
        widgets.add(appGroup);

        DataSaver.saveObject("widgets", widgets);
    }



    private static void build(){
        Screen.init(context);

        /*
        AppGroup group = new AppGroup(AppManager.getApps());
        group.sortBy(AppSorter.SortType.APP_NAME, false);

        AppGroupView view = new AppGroupView(context, group);

        int p = Screen.dip(5);
        view.setPadding(p, p, p, p);

        main.addView(view);
        */

        for(WidgetData data : getWidgets()){
            if(data instanceof AppGroupData){
                AppGroupView view = new AppGroupView(context, (AppGroupData) data);
                getMain().addView(view);
            }
        }
    }
}
