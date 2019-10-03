package com.dilonexus.sigmalauncher.views;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dilonexus.sigmalauncher.misc.Screen;

public class LoadingScreenView extends LinearLayout {
    private RoundProgressBarView progressBar;
    public LoadingScreenView(Context context) {
        super(context);

        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);

        int size = Math.min(Screen.getWidth(), Screen.getHeight()) * 4 / 5;
        progressBar = new RoundProgressBarView(getContext());
        addView(progressBar, new ViewGroup.LayoutParams(size, size));
    }

    public void setProgress(double progress){
        progressBar.setProgress(progress);
    }
}
