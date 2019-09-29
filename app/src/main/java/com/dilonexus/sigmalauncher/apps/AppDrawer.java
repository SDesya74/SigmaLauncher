package com.dilonexus.sigmalauncher.apps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class AppDrawer {
    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public static void drawApp(Canvas canvas, int x, int y, AppStyle style){
        paint.setColor(style.backColor);

        textPaint.setTextSize(style.getPaintTextSize());
        textPaint.setColor(style.textColor);

        canvas.drawRect(x, y, x + style.bounds.width(), y + style.bounds.height(), paint);
        canvas.drawText(
                style.getLabel(),
                x + (style.bounds.width() - style.textBounds.width()) / 2,
                y + (style.bounds.height() + style.textBounds.height()) / 2, textPaint);
    }


    private static Paint boundsPaint = new Paint();
    public static Rect getTextBounds(String text, float size){
        Rect bounds = new Rect();
        boundsPaint.setTextSize(size);
        boundsPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }
}
