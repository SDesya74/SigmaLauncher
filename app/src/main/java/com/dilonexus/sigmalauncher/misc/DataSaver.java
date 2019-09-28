package com.dilonexus.sigmalauncher.misc;

import android.content.Context;

import com.dilonexus.sigmalauncher.apps.AppManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataSaver {
    private static String internalDirectory;
    public static void init(Context context){
        internalDirectory = context.getFilesDir().toString();
    }

    public static void saveObject(String path, Object data){
        File directory = getInternalDirectory();

        try{
            File file = new File(directory, path);
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
            output.writeObject(data);
            output.flush();
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Object readObject(String path){
        File directory = getInternalDirectory();

        File file = new File(directory, path);
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            Object result = input.readObject();
            input.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static File getInternalDirectory(){
        return new File(internalDirectory);
    }

}
