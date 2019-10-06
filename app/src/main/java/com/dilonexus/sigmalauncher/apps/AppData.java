package com.dilonexus.sigmalauncher.apps;

import java.io.Serializable;

public class AppData implements Serializable {
    private String key;
    public String getKey(){
        return key;
    }

    public int popularity;
    public boolean hidden;


    AppData(String key){
        this.key = key;
        popularity = 0;
        hidden = false;
    }
}
