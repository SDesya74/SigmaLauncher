package com.dilonexus.sigmalauncher.misc;

import android.graphics.Typeface;

import java.io.Serializable;
import java.util.List;

public class FontManager {
    private static List<Font> list;
    private static Typeface currentFont;


    public static void setCurrentFont(){

    }

    public static void loadFonts(){

    }

    private class Font implements Serializable {
        private Typeface font;
        private String name;
    }
}
