package com.dilonexus.sigmalauncher.misc;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;


public class Screen {
    private static int width = -1;
    private static int height = -1;
    private static float scale = 1f;
    private static int actionBarHeight = -1;
    private static int statusBarHeight = -1;

    private static DisplayMetrics metrics;
    public static void init(Activity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        Point point = new Point();
        display.getSize(point);
        width = point.x;
        height = point.y;

        scale = context.getResources().getDisplayMetrics().density;

        TypedArray ta = context.getTheme().obtainStyledAttributes(new int[] {android.R.attr.actionBarSize});
        actionBarHeight = (int) ta.getDimension(0, 0 );

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);

    }

    public static int getWidth(){
        return width;
    }
    public static int getHeight(){
        return height;
    }
    public static int dip(float px){
        return (int) (0.5f + px*scale);
    }
    public static DisplayMetrics getMetrics(){
        return metrics;
    }
    public static int getActionBarHeight(){
        return actionBarHeight;
    }
    public static int getStatusBarHeight(){
        return statusBarHeight;
    }
    public static int getUsefulHeight() {
        return getHeight() - getStatusBarHeight();
    }
}
