package com.dilonexus.sigmalauncher.apps;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.dilonexus.sigmalauncher.misc.FontManager;
import com.dilonexus.sigmalauncher.misc.Options;

public class AppDrawer {
    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public static void drawApp(Canvas canvas, int x, int y, AppStyle style){
        if(Options.APP_BOUNDS_SHOWING){
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            int sx = x - 1;
            int sy = y - 1;

            int ex = x + 2 + style.backBounds.width();
            int ey = y + 2 + style.backBounds.height();
            canvas.drawRect(sx, sy, ex, ey, paint);
        }

        paint.setColor(style.backColor);

        textPaint.setTextSize(style.getPaintTextSize());
        textPaint.setColor(style.textColor);
        textPaint.setTypeface(FontManager.getCurrentFont());

        canvas.drawRect(
                x,
                y,
                x + style.backBounds.width(),
                y + style.backBounds.height(),
                paint);

        canvas.drawText(style.getLabel(),
                x + (style.backBounds.width() - style.textBounds.width()) / 2,
                y + (style.backBounds.height() + style.textBounds.height()) / 2, textPaint);
    }


    private static Paint boundsPaint = new Paint();
    static Rect getTextBounds(String text, float size){
        Rect bounds = new Rect();
        boundsPaint.setTextSize(size);
        boundsPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }
}