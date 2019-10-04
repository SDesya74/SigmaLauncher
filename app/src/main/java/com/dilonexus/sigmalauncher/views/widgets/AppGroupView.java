package com.dilonexus.sigmalauncher.views.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dilonexus.sigmalauncher.apps.AppData;
import com.dilonexus.sigmalauncher.apps.AppDrawer;
import com.dilonexus.sigmalauncher.apps.AppManager;
import com.dilonexus.sigmalauncher.apps.AppStyle;
import com.dilonexus.sigmalauncher.misc.Screen;

import java.util.ArrayList;
import java.util.List;

public class AppGroupView extends View implements View.OnTouchListener {
    private int ROW_PADDING;
    private int CELL_PADDING;

    private int CELL_MARGIN;
    private int ROW_MARGIN;



    private List<GroupItem> groupItemTouchBounds;
    private List<GroupRow> groupRows;
    public List<GroupRow> getRows(){
        return groupRows;
    }
    public AppGroupView(Context context) {
        super(context);
        setOnTouchListener(this);

        groupItemTouchBounds = new ArrayList<>();
        groupRows = new ArrayList<>();

        ROW_PADDING = Screen.dip(3);
        CELL_PADDING = Screen.dip(3);

        ROW_MARGIN = Screen.dip(3);
        CELL_MARGIN = Screen.dip(3);
    }




    private int groupWidth;
    private int groupHeight;


    private List<AppData> groupItems;
    public void setItems(List<AppData> list){
        groupItems = list;
    }

    private GroupRow currentRow;
    private void addItem(GroupItem item){
        if(currentRow == null){
            currentRow = new GroupRow();
            groupRows.add(currentRow);
        }
        if(currentRow.canAddItem(item) || currentRow.getItems().size() < 1){
            currentRow.addItem(item);
        }else{
            currentRow = null;
            addItem(item);
        }
    }

    protected void onDraw(Canvas canvas) {
        groupItemTouchBounds.clear();
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int y = 0;
        for(GroupRow row : getRows()){
            row.draw(canvas, left, top + y);
            y += row.getHeight() + ROW_PADDING;
        }
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        groupWidth = getMeasuredWidth();

        groupItemTouchBounds.clear();
        groupRows.clear();
        currentRow = null;
        for(AppData app : groupItems){
            @SuppressLint("DrawAllocation")
            GroupItem item = new GroupItem(app);
            addItem(item);
        }

        groupHeight = 0;
        for(GroupRow row : groupRows) groupHeight += row.getHeight() + ROW_MARGIN;
        groupHeight -= ROW_MARGIN;

        setMeasuredDimension(getMeasuredWidth(), groupHeight + getPaddingTop() + getPaddingBottom());
    }

    @Override
    public void invalidate() {
        requestLayout();
    }


    private PointF touchStart;
    private PointF touchCurrent;
    private GroupItem touchedItem;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchCurrent = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart = new PointF(event.getX(), event.getY());
                touchedItem = getTouchedItem(touchStart);
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if(touchedItem != null && touchedItem.equals(getTouchedItem(touchCurrent))){
                    AppData data = AppManager.getAppByID(touchedItem.uniqueID);

                    assert data != null;

                    data.addPopularity(1);
                    touchedItem.updatePopularity();
                    AppManager.startApp(data);
                    invalidate();
                }

                touchStart = null;
                touchCurrent = null;
                break;

            case MotionEvent.ACTION_MOVE:
                touchCurrent = new PointF(event.getX(), event.getY());
                break;
        }
        return true;
    }

    GroupItem getTouchedItem(PointF touch){
        for(GroupItem item : groupItemTouchBounds){
            if(item.contains(touch.x, touch.y)){
                return item;
            }
        }
        return null;
    }





    class GroupRow{
        private List<GroupItem> rowItems;
        List<GroupItem> getItems(){
            return rowItems;
        }
        private int rowHeight;
        private int rowWidth;
        GroupRow(){
            rowWidth = 0;
            rowHeight = 0;
            rowItems = new ArrayList<>();
        }

        private int getHeight(){
            return rowHeight;
        }
        boolean canAddItem(GroupItem app){
            return rowWidth + app.bounds.width() + CELL_PADDING <
                    groupWidth - getPaddingRight() - getPaddingLeft();
        }

        void addItem(GroupItem app){
            rowItems.add(app);
            rowWidth += app.bounds.width() + CELL_PADDING;
            rowHeight = Math.max(app.bounds.height(), rowHeight);
        }

        void draw(Canvas canvas, int left, int top){
            int x = (groupWidth - rowWidth) / 2;
            for(GroupItem item : rowItems){
                int itemX = left + x;
                int itemY = top + (rowHeight - item.bounds.height()) / 2;
                AppDrawer.drawApp(canvas, itemX, itemY, item);
                x += item.bounds.width() + CELL_MARGIN;


                item.validateCoords(itemX, itemY);
                groupItemTouchBounds.add(item);
            }
        }
    }

    class GroupItem extends AppStyle{
        GroupItem(AppData app) {
            super(app);
            setPadding(CELL_PADDING, ROW_PADDING);
        }

        private Rect touchBounds;
        boolean contains(float x, float y){
            assert touchBounds != null;

            return touchBounds.contains((int) x, (int) y);
        }

        void validateCoords(int x, int y){
            touchBounds = new Rect(x, y, x + bounds.width(), y + bounds.height());
        }
    }
}