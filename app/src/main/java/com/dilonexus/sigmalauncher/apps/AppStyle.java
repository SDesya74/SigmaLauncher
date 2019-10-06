package com.dilonexus.sigmalauncher.apps;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.TypedValue;

import com.dilonexus.sigmalauncher.misc.Options;
import com.dilonexus.sigmalauncher.misc.Screen;

public class AppStyle {
    private String key;
    public String getKey(){
        return key;
    }


    private Point padding;
    protected void setPadding(int vertical, int horizontal){
        padding = new Point(vertical, horizontal);
        validate();
    }
    private void setPadding(int padding){
        setPadding(padding, padding);
    }

    public Rect backBounds;
    public Rect textBounds;

    private void validate(){
        AppData data = AppManager.getAppData(key);
        assert data != null;
        this.popularity = data.popularity;

        float size = getPaintTextSize();
        textBounds = AppDrawer.getTextBounds(getLabel(), size);

        int height = AppDrawer.getTextBounds("p", size).height();
        textBounds.top = -height / 2;
        textBounds.bottom = height / 2;

        backBounds = new Rect(textBounds);
        // region Paddings
        backBounds.left -= padding.x;
        backBounds.right += padding.x;

        backBounds.top -= padding.y;
        backBounds.bottom += padding.y;
        // endregion
    }



    public int popularity;
    private int getTextSize(){
        return Options.MIN_TEXT_SIZE + popularity;
    }
    float getPaintTextSize(){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, getTextSize(), Screen.getMetrics());
    }


    private String label;
    String getLabel(){
        if(label == null || label.length() < 1) {
            AppInfo info = AppManager.getAppInfo(this.getKey());
            assert info != null;

            label = info.name;
        }
        return label;
    }



    public int backColor;
    public int textColor;








    public AppStyle(AppInfo app){
        this.key = app.getKey();
        this.label = app.name;

        AppData data = AppManager.getAppData(app.getKey());
        assert data != null;
        this.popularity = data.popularity;

        setPadding(0);

        this.backColor = Color.TRANSPARENT;
        this.textColor = Color.LTGRAY;
    }
}
