package com.dilonexus.sigmalauncher;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.dilonexus.sigmalauncher.misc.DataSaver;
import com.dilonexus.sigmalauncher.misc.FontManager;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Launcher.show(this);
        Launcher.startLoadingApps();
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return Launcher.onMenuOpened();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
