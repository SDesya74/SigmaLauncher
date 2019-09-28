package com.dilonexus.sigmalauncher.views;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dilonexus.sigmalauncher.apps.AppManager;

public class LauncherView extends FrameLayout {
    private LinearLayout main;


    public LauncherView(Context context) {
        super(context);

        main = new LinearLayout(getContext());
        main.setOrientation(LinearLayout.VERTICAL);
        addView(main);
    }


    public void build(){
        Toast.makeText(getContext(), "Loaded " + AppManager.getApps().size() + " apps", Toast.LENGTH_SHORT).show();
    }


}
