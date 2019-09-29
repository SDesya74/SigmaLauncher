package com.dilonexus.sigmalauncher.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;

import com.dilonexus.sigmalauncher.misc.Screen;

public class RoundProgressBarView extends View {
    private Handler handler;
    private Paint paint;
    private RectF bounds;

    public RoundProgressBarView(Context context) {
        super(context);

        handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                invalidate();
                handler.postDelayed(this, 10);
            }
        });

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(Screen.dip(6));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setAlpha(200);

        bounds = new RectF();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        int size = getWidth();

        bounds.left = bounds.top = paint.getStrokeWidth();
        bounds.right = bounds.bottom = size - paint.getStrokeWidth();

        canvas.drawArc(bounds, 270, (float) (progressVisual * 360), false, paint);

        double dis = (progress - progressVisual);
        progressVisual += dis < 1d/360d ? dis : dis;
    }

    private double progress;
    private double progressVisual;
    public void setProgress(double progress){
        this.progress = progress;
    }
}
