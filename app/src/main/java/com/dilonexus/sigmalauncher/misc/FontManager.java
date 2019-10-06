package com.dilonexus.sigmalauncher.misc;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FontManager {
    private static AssetManager assets;

    // region Font List
    private static List<Font> list;
    public static List<Font> getFonts(){
        return list;
    }
    private static Font getFontByName(String name){
        if(name.toLowerCase().equals("default")) return getDefaultFont();
        for (Font font : list) {
            if(font.getName().equals(name)) return font;
        }
        return null;
    }
    private static Font getDefaultFont(){
        return new Font("DEFAULT", Typeface.DEFAULT);
    }
    // endregion

    // region Current Font
    private static Font currentFont;
    public static Typeface getCurrentFont(){
        return currentFont == null ? getDefaultFont().get() : currentFont.get();
    }
    private static String getCurrentFontName(){
        return currentFont.getName();
    }
    public static void setCurrentFont(Font font){
        assert font != null;
        currentFont = font;
        save();
    }
    // endregionы

    public static void init(Context context){
        assets = context.getAssets();
    }

    public static void load(){
        list = new ArrayList<>();

        // region Load From Assets
        try {
            String[] folder = assets.list("fonts");
            assert folder != null;

            for(String file : folder){
                Typeface typeface = Typeface.createFromAsset(assets, "fonts/" + file);

                Font font = new Font(file, typeface);
                list.add(font);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // endregion

        // region Load Current Font
        String name = (String) DataSaver.readObject("currentFont");
        if(name != null){
            setCurrentFont(getFontByName(name));
        }else setCurrentFont(getDefaultFont());
        // endregion
    }
    private static void save(){
        DataSaver.saveObject("currentFont", getCurrentFontName());
    }

    public static class Font implements Serializable {
        private Typeface typeface;
        Typeface get(){
            return typeface;
        }

        private String name;
        Font(String name, Typeface typeface){
            this.name = name;
            this.typeface = typeface;
        }

        String getName() {
            return name;
        }
    }
}
