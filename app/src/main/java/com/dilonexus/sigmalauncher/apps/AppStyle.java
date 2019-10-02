package com.dilonexus.sigmalauncher.apps;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.TypedValue;

import com.dilonexus.sigmalauncher.misc.Screen;

public class AppStyle {
    public long uniqueID;

    // region Paddings
    private int horizontalPadding;
    private int verticalPadding;
    protected void setPadding(int vertical, int horizontal){
        horizontalPadding = horizontal;
        verticalPadding = vertical;
        validateBounds();
    }
    public void setPadding(int padding){
        setPadding(padding, padding);
    }
    // endregion

    int backColor;
    // private int backShape;
    // private int backStyle;

    int textColor;
    private void setTextColorByBackColor(){
        int c = backColor;
        if((Color.red(c)*0.299 + Color.green(c)*0.587 + Color.blue(c)*0.114) > 186){
            textColor = Color.BLACK;
        }else textColor = Color.WHITE;
    }

    private int popularity;
    private int getTextSize(){
        return 14 + popularity; // TODO: Вынести минимальный размер текста в настройки
    }
    float getPaintTextSize(){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, getTextSize(), Screen.getMetrics());
    }

    public void updatePopularity(){
        AppData data = AppManager.getAppByID(this.uniqueID);

        assert data != null;

        this.popularity = data.getPopularity();
    }

    public AppStyle(AppData app){
        this.uniqueID = app.uniqueID;
        this.label = app.getName();
        this.popularity = app.getPopularity();

        setPadding(0);

        this.backColor = Color.TRANSPARENT;
        setTextColorByBackColor();
    }

    Rect textBounds;
    public Rect bounds;
    void validateBounds(){
        float size = getPaintTextSize();
        textBounds = AppDrawer.getTextBounds(getLabel(), size);

        bounds = new Rect(textBounds);

        // region Paddings
        bounds.left -= horizontalPadding;
        bounds.right += horizontalPadding;

        bounds.top -= verticalPadding;
        bounds.bottom += verticalPadding;

        // endregion
    }

    private String label;
    private String getLabel(){
        if(label == null || label.length() < 1) {
            AppData data = AppManager.getAppByID(this.uniqueID);

            assert data != null;

            label = data.getName();
        }
        return label;
    }
}
