package com.dilonexus.sigmalauncher.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

public class CustomDialog{
    private PopupWindow window;
    CustomDialog(Context context){
        window = new PopupWindow(context);
    }

    void setView(View view){
        window.setContentView(view);
    }

    void show(){
        // window.showAtLocation();
    }

    void hide(){

    }
}
