package com.dilonexus.sigmalauncher;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.dilonexus.sigmalauncher.apps.AppData;
import com.dilonexus.sigmalauncher.apps.AppManager;
import com.dilonexus.sigmalauncher.misc.DataSaver;
import com.dilonexus.sigmalauncher.views.LauncherView;
import com.dilonexus.sigmalauncher.views.LoadingScreenView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Handler loadingHandler;

    LauncherView launcher;
    LoadingScreenView loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = new LauncherView(this);
        setContentView(launcher);

        loadApps();
    }


    public void onAppsLoaded(){
        launcher.removeView(loadingScreen);
        launcher.build();
    }

    @SuppressWarnings("unchecked")
    public void loadApps(){
        loadingScreen = new LoadingScreenView(this);
        launcher.addView(loadingScreen);

        List<AppData> apps = (List<AppData>) DataSaver.readObject("apps");
        AppManager.loadAppsResolve();
        if(apps != null && apps.size() == AppManager.getAppResolveList().size()){
            AppManager.setApps(apps);
            onAppsLoaded();
            return;
        }

        loadingHandler = new Handler();
        loadingHandler.post(new Runnable() {
            public void run() {
                boolean stop = AppManager.loadNextApp();
                loadingScreen.setProgress(AppManager.getLoadingProgress());
                if(!stop) loadingHandler.post(this);
                else{
                    onAppsLoaded();

                    DataSaver.saveObject("apps", AppManager.getApps());
                }
            }
        });
    }
}
