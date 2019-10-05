package com.dilonexus.sigmalauncher.apps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AppGroup implements Serializable {
    private List<AppData> items;
    public List<AppData> getItems() {
        return items;
    }

    public AppGroup(){
        items = new ArrayList<>();
    }

    public AppGroup(List<AppData> list){
        items = list;
    }

    public void addItem(AppData item){
        items.add(item);
    }

    public boolean containsItem(AppData item){
        return items.contains(item);
    }

    public void removeDuplicates(){
        Set<AppData> set = new LinkedHashSet<>(items);
        items.clear();
        items.addAll(set);
    }
}
