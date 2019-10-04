package com.dilonexus.sigmalauncher.apps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AppGroup {
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



    public void sortBy(final AppSorter.SortType type, boolean reverse){
        Collections.sort(items, new Comparator<AppData>() {
            public int compare(AppData a, AppData b) {
                return AppSorter.compareApps(type, a, b);
            }
        });
        if(reverse) Collections.reverse(items);
    }
}
