package com.dilonexus.sigmalauncher;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Launcher.show(this);
        Launcher.startLoading();
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return Launcher.onMenuOpened();
    }

    @Override
    public void onBackPressed() {
        Launcher.onBackPressed();
    }
}
