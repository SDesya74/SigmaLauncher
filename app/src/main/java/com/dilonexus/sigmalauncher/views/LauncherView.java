package com.dilonexus.sigmalauncher.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dilonexus.sigmalauncher.apps.AppManager;
import com.dilonexus.sigmalauncher.misc.Screen;
import com.dilonexus.sigmalauncher.views.widgets.AppGroupView;

public class LauncherView extends FrameLayout {
    private ScrollView scroll;
    private LinearLayout main;


    public LauncherView(Context context) {
        super(context);

        scroll = new ScrollView(getContext());
        addView(scroll);

        main = new LinearLayout(getContext());
        main.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(main);
    }


    public void build(){
        AppGroupView group = new AppGroupView(getContext());

        int p = Screen.dip(5);
        group.setPadding(p, p, p, p);
        group.setWidth(Screen.getWidth());
        group.setItems(AppManager.getApps());
        Toast.makeText(getContext(), "rows: " + group.getRows().size(), Toast.LENGTH_SHORT).show();


        main.addView(group);
    }


}
