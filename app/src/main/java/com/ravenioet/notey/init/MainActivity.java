package com.ravenioet.notey.init;

import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ravenioet.notey.R;
import com.ravenioet.notey.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends ServiceProvider {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if(isBioAuthEnabled()){
            requireBioPass();
        }else {
            initSystem();
        }

    }
    private void initSystem(){
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_secured:
                if(navController.getCurrentDestination() != navController.findDestination(R.id.SecuredNotes)){
                    navController.navigate(R.id.SecuredNotes);
                }
                break;
            case R.id.action_trash:
                if(navController.getCurrentDestination() != navController.findDestination(R.id.DeletedNotes)){
                    navController.navigate(R.id.DeletedNotes);
                }
                break;
            case R.id.action_settings:
                if(navController.getCurrentDestination() != navController.findDestination(R.id.NavSettings)){
                    navController.navigate(R.id.NavSettings);
                }
                break;
            case R.id.action_secured_input:
                if(navController.getCurrentDestination() != navController.findDestination(R.id.NavSecuredInput)){
                    navController.navigate(R.id.NavSecuredInput);
                }
                break;

        }

        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private BiometricPrompt biometricPrompt = null;
    private Executor executor = Executors.newSingleThreadExecutor();

    private void requireBioPass() {
        if (biometricPrompt == null) {
            biometricPrompt = new BiometricPrompt(this, executor, callback);
        }
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                BiometricPrompt.PromptInfo promptInfo = buildBiometricPrompt();
                biometricPrompt.authenticate(promptInfo);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                break;
        }
    }

    private BiometricPrompt.PromptInfo buildBiometricPrompt() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication")
                //.setSubtitle("FingerPrint Authentication")
                .setDescription("Please place your finger on the sensor to unlock")
                .setNegativeButtonText("Cancel")
                .build();

    }


    private BiometricPrompt.AuthenticationCallback callback = new
            BiometricPrompt.AuthenticationCallback() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    if (errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                        biometricPrompt.cancelAuthentication();
                    finish();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    runOnUiThread(() -> initSystem());
                }

                @Override
                public void onAuthenticationFailed() {
                    return;
                }
            };

}