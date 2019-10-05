package com.dilonexus.sigmalauncher.views.widgets.AppGroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dilonexus.sigmalauncher.Launcher;
import com.dilonexus.sigmalauncher.apps.AppData;
import com.dilonexus.sigmalauncher.apps.AppDrawer;
import com.dilonexus.sigmalauncher.apps.AppManager;
import com.dilonexus.sigmalauncher.apps.AppSorter;
import com.dilonexus.sigmalauncher.apps.AppStyle;
import com.dilonexus.sigmalauncher.views.widgets.WidgetView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressLint("ViewConstructor")
public class AppGroupView extends WidgetView implements View.OnTouchListener {
    // region Constructor
    private int groupWidth;

    private AppGroupData data;
    AppGroupData getData(){
        return data;
    }

    public AppGroupView(Context context, AppGroupData data) {
        super(context);

        assert data != null;
        setOnTouchListener(this);

        touchBounds = new ArrayList<>();
        rows = new ArrayList<>();

        this.data = data;
    }
    // endregion

    //region Touch Bounds
    private List<Item> touchBounds;
    private List<Item> getTouchBounds(){
        return touchBounds;
    }
    // endregion

    // region Rows
    private List<Row> rows;
    public List<Row> getRows(){
        return rows;
    }
    // endregion

    // region Sorting
    public void sortApps(){
        final AppSorter.SortType type = getData().getSortType();
        Collections.sort(getData().getItems(), new Comparator<AppData>() {
            @Override
            public int compare(AppData o1, AppData o2) {
                return AppSorter.compareApps(type, o1, o2);
            }
        });
        if(getData().isReverseSorting()) Collections.reverse(getData().getItems());
    }
    // endregion

    // region Draw & Measure
    @Override
    public void invalidate() {
        requestLayout();
    }

    private Row currentRow;
    private void addItem(Item item){
        if(currentRow == null){
            currentRow = new Row();
            getRows().add(currentRow);
        }
        if(currentRow.canAddItem(item) || currentRow.getItems().size() < 1){
            currentRow.addItem(item);
        }else{
            currentRow = null;
            addItem(item);
        }
    }
    protected void onDraw(Canvas canvas) {
        getTouchBounds().clear();
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int y = 0;
        for(Row row : getRows()){
            row.draw(canvas, left, top + y);
            y += row.getHeight() + getData().getRowPadding();
        }
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        groupWidth = getMeasuredWidth();

        getTouchBounds().clear();
        getRows().clear();
        currentRow = null;

        sortApps();
        for(AppData app : getData().getItems()){
            @SuppressLint("DrawAllocation")
            Item item = new Item(app);
            addItem(item);
        }

        int height = 0;
        for(Row row : getRows()) height += row.getHeight() + getData().getRowMargin();
        height -= getData().getRowMargin();

        setMeasuredDimension(getMeasuredWidth(), height + getPaddingTop() + getPaddingBottom());
    }
    // endregion

    // region On Touch Listener
    private PointF touchStart;
    private PointF touchCurrent;
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


                    Toast.makeText(getContext(), data.uniqueID + "\n" + touchedItem.uniqueID + "\n" + data.getPopularity(), Toast.LENGTH_SHORT).show();
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

    private Item touchedItem;
    Item getTouchedItem(PointF touch){
        for(Item item : getTouchBounds()){
            if(item.contains(touch.x, touch.y)){
                return item;
            }
        }
        return null;
    }
    // endregion

    class Row{
        private List<Item> rowItems;
        List<Item> getItems(){
            return rowItems;
        }
        private int rowHeight;
        private int rowWidth;
        Row(){
            rowWidth = 0;
            rowHeight = 0;
            rowItems = new ArrayList<>();
        }

        private int getHeight(){
            return rowHeight;
        }
        boolean canAddItem(Item item){
            return rowWidth + item.bounds.width() + getData().getCellPadding() <
                    groupWidth - getPaddingRight() - getPaddingLeft();
        }

        void addItem(Item item){
            rowItems.add(item);
            rowWidth += item.bounds.width() + getData().getCellPadding();
            rowHeight = Math.max(item.bounds.height(), rowHeight);
        }

        void draw(Canvas canvas, int left, int top){
            int x = (groupWidth - rowWidth) / 2;
            for(Item item : rowItems){
                int itemX = left + x;
                int itemY = top + (rowHeight - item.bounds.height()) / 2;

                AppDrawer.drawApp(canvas, itemX, itemY, item);
                x += item.bounds.width() + getData().getCellMargin();

                item.calcBounds(itemX, itemY);
                getTouchBounds().add(item);
            }
        }
    }

    class Item extends AppStyle{
        Item(final AppData app) {
            super(app);
            setPadding(getData().getCellPadding(), getData().getRowPadding());
        }

        private Rect touchBounds;
        boolean contains(final float x, final float y){
            assert touchBounds != null;

            return touchBounds.contains((int) x, (int) y);
        }

        void calcBounds(int x, int y){
            touchBounds = new Rect(x, y, x + bounds.width(), y + bounds.height());
        }
    }
}