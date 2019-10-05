package com.dilonexus.sigmalauncher.views.widgets.AppFolder;

import com.dilonexus.sigmalauncher.apps.AppData;
import com.dilonexus.sigmalauncher.views.widgets.WidgetData;

import java.io.Serializable;
import java.util.List;

public class AppFolderData extends WidgetData {
    List<AppData> list;
    public AppFolderData(List<AppData> list){
        this.list = list;
    }
}
