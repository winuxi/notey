package com.ravenioet.notey.init;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ravenioet.notey.R;
import com.ravenioet.notey.databinding.ActivityMainBinding;
import com.ravenioet.notey.guard.SecPack;
import com.ravenioet.notey.interfaces.SecureInputListener;
import com.ravenioet.notey.utils.NoteUtils;
import com.ravenioet.notey.utils.PrefManager;

public class ServiceProvider extends AppCompatActivity implements SecureInputListener {
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode
                (AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        initDependency();
        NoteUtils.getInstance().setSecKeyListener(this);

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

    public boolean isPinCodeEnabled() {
        return getPrefMan().getBoolean("pin-code");
    }

    public boolean isAnimationEnabled() {
        return getPrefMan().getBoolean("pin-code");
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
}
