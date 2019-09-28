package com.dilonexus.sigmalauncher.views.widgets;

import android.content.Context;
import android.view.View;

import com.dilonexus.sigmalauncher.apps.AppData;
import com.dilonexus.sigmalauncher.apps.AppManager;

import java.util.ArrayList;
import java.util.List;

public class AppGroupView extends View {
    private List<GroupElement> items;
    public AppGroupView(Context context) {
        super(context);
        items = new ArrayList<>();
    }




    public void addApp(AppData app){
        GroupElement element = new GroupElement(app.uniqueID);
    }


    private class Row{
        public void addView(){

        }
    }


    private class AppDrawer{

    }

    private class GroupElement{
        private long uniqueID;
        GroupElement(long id){
            this.uniqueID = id;
        }

        public void start(){
            AppData app = AppManager.getAppByID(this.uniqueID);
        }
    }
}