package com.dilonexus.sigmalauncher.views.widgets.AppGroup;

import com.dilonexus.sigmalauncher.apps.AppData;
import com.dilonexus.sigmalauncher.apps.AppSorter;
import com.dilonexus.sigmalauncher.misc.Screen;
import com.dilonexus.sigmalauncher.views.widgets.WidgetData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppGroupData extends WidgetData implements Serializable {
    // region Items
    private List<AppData> items;
    List<AppData> getItems(){
        return items;
    }
    // endregion

    // region Paddings & Margins
    private int ROW_PADDING;
    private int CELL_PADDING;

    private int CELL_MARGIN;
    private int ROW_MARGIN;

    public void setPadding(int row, int cell){
        ROW_PADDING = row;
        CELL_PADDING = cell;
    }

    public void setMargin(int row, int cell){
        ROW_MARGIN = row;
        CELL_MARGIN = cell;
    }

    int getRowPadding() {
        return ROW_PADDING;
    }

    int getCellPadding() {
        return CELL_PADDING;
    }

    int getRowMargin() {
        return ROW_MARGIN;
    }

    int getCellMargin() {
        return CELL_MARGIN;
    }
    // endregion

    public AppGroupData(List<AppData> list){
        items = new ArrayList<>(list);

        ROW_PADDING = Screen.dip(3);
        CELL_PADDING = Screen.dip(3);

        ROW_MARGIN = Screen.dip(3);
        CELL_MARGIN = Screen.dip(3);

        sortType = null;
        reverseSorting = false;
    }

    // region Sorting
    private AppSorter.SortType sortType;
    public void setSortType(AppSorter.SortType type){
        sortType = type;
    }
    AppSorter.SortType getSortType(){
        return sortType;
    }
    private boolean reverseSorting;
    public void setReverseSorting(boolean value){
        reverseSorting = value;
    }
    boolean isReverseSorting(){
        return reverseSorting;
    }
    // endregion
}
