package com.ravenioet.notey.init;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ravenioet.notey.R;
import com.ravenioet.notey.databinding.ActivityMainBinding;
import com.ravenioet.notey.utils.PrefManager;

public class ServiceProvider extends AppCompatActivity {
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode
                (AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        initDependency();
    }
    private void initDependency(){
        prefManager = PrefManager.getPrefMan(this,"notey-pref");
    }
    public PrefManager getPrefMan(){
        if(prefManager == null){
            prefManager = PrefManager.getPrefMan(this,"notey-pref");
        }
        return prefManager;
    }
    public boolean isBioAuthEnabled(){
        return getPrefMan().getBoolean("bio-pass");
    }
    public boolean isPinCodeEnabled(){
        return getPrefMan().getBoolean("pin-code");
    }

}
