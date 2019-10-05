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
import com.dilonexus.sigmalauncher.apps.AppSorter;
import com.dilonexus.sigmalauncher.misc.DataSaver;
import com.dilonexus.sigmalauncher.misc.FontManager;
import com.dilonexus.sigmalauncher.misc.Options;
import com.dilonexus.sigmalauncher.misc.Screen;
import com.dilonexus.sigmalauncher.views.LoadingScreenView;
import com.dilonexus.sigmalauncher.views.widgets.AppGroup.AppGroupData;
import com.dilonexus.sigmalauncher.views.widgets.AppGroup.AppGroupView;
import com.dilonexus.sigmalauncher.views.widgets.WidgetData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Launcher {
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
    }

    public static void invalidate() {
        Screen.init(context);

        clear();

        loadWidgets();
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
                getParent().removeView(loadingScreen);

                onAppsLoaded();
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
                    getParent().removeView(loadingScreen);
                    DataSaver.saveObject("apps", AppManager.getApps());

                    onAppsLoaded();
                }
            }
        });
        // endregion
    }
    private static void onAppsLoaded(){
        invalidate();
    }

    private static void clear(){
        main.removeAllViews();
    }

    private static List<WidgetData> widgets;
    private static List<WidgetData> getWidgets(){
        return widgets;
    }

    private static void loadWidgets(){

        if(Options.APP_CACHE_ENABLED){
            // noinspection unchecked
            widgets = (List<WidgetData>) DataSaver.readObject("widgets");
            if(widgets != null && widgets.size() > 0) {
                return;
            }
        }


        widgets = new ArrayList<>();

        AppGroupData appGroup = new AppGroupData(AppManager.getApps());
        appGroup.setSortType(AppSorter.SortType.INSTALL_TIME);
        appGroup.setReverseSorting(true);
        appGroup.setPadding(Screen.dip(5), Screen.dip(5));
        appGroup.setMargin(Screen.dip(5), Screen.dip(5));
        widgets.add(appGroup);

        DataSaver.saveObject("widgets", widgets);
    }

    private static void build(){
        Screen.init(context);

        for(WidgetData widget : getWidgets()){
            if(widget instanceof AppGroupData){
                AppGroupData data = (AppGroupData) widget;

                AppGroupView view = new AppGroupView(context, data);
                getMain().addView(view);
            }
        }
    }









    public static boolean onMenuOpened(){

        /*
        DataSaver.deleteFile("apps");
        DataSaver.deleteFile("widgets");
        */


        /*
        int rnd = new Random().nextInt(FontManager.getFonts().size());
        FontManager.setCurrentFont(FontManager.getFonts().get(rnd));
        */
        Launcher.invalidate();

        return true;
    }
}
