package com.ravenioet.notey.init;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.ravenioet.core.manager.PrefManager;
import com.ravenioet.notey.components.theme.Themes;
import com.ravenioet.notey.guard.SecPack;
import com.ravenioet.notey.interfaces.SecureInputListener;
import com.ravenioet.notey.utils.NoteUtils;

public class ServiceProvider extends AppCompatActivity implements SecureInputListener {
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        initDependency();
        NoteUtils.getInstance().setSecKeyListener(this);

    }
    private void initTheme(){
        AppCompatDelegate.setDefaultNightMode
                (AppCompatDelegate.MODE_NIGHT_YES);
        //new ThemeColors(this);
        //toolBarTheme();
    }
    public void toolBarTheme(){
        if(getSupportActionBar() != null){
            getSupportActionBar().getCustomView().setBackgroundColor(Color.RED);
        }
    }

    private void initDependency() {
        prefManager = PrefManager.getPrefMan(this, "notey-pref");
    }

    public PrefManager getPrefMan() {
        if (prefManager == null) {
            prefManager = PrefManager.getPrefMan(this, "notey-pref");
        }
        return prefManager;
    }

    public boolean isBioAuthEnabled() {
        return getPrefMan().getBoolean("bio-pass");
    }
    public boolean isVibrationEnabled() {
        return getPrefMan().getBoolean("vibrate");
    }
    public boolean isAutoSaveEnabled() {
        return getPrefMan().getBoolean("auto-save");
    }

    public boolean isPinCodeEnabled() {
        return getPrefMan().getBoolean("pin-code");
    }

    public boolean isAnimationEnabled() {
        return getPrefMan().getBoolean("list-anim");
    }
    public boolean isGridEnabled() {
        return getPrefMan().getString("view-type").equals("Grid");
    }


    @Override
    public void pinPassed(SecPack secPack) {
        if (secPack != null && secPack.isForUpdate()) {
            if (getPrefMan().putBoolean("pin-code", secPack.isState())) {
                if (secPack.isState()) {
                    toast("Pin Lock enabled");
                } else {
                    toast("Pin Lock disabled");
                }
            }
        }
    }

    @Override
    public void bioSuccess(SecPack secPack) {
        if (secPack != null && secPack.isForUpdate()) {
            if (getPrefMan().putBoolean("pin-code", secPack.isState())) {
                if (secPack.isState()) {
                    toast("Pin Lock enabled");
                } else {
                    toast("Pin Lock disabled");
                }
            }
        }
    }

    @Override
    public void bioFailure(SecPack secPack) {
        if (secPack != null && secPack.isForUpdate()) {

        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public Themes getSavedTheme(){
        switch (getPrefMan().getString("active-theme")){
            case "dark":
            case "Default":
                return Themes.dark;
            case "blue":
                return Themes.blue;
            case "orange":
                return Themes.orange;
        }
        return Themes.blue;
    }
    public boolean setActiveTheme(String theme){
        return getPrefMan().putString("active-theme",theme);
    }
}
