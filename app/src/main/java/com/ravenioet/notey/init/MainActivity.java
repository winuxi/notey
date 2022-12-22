package com.ravenioet.notey.init;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ravenioet.notey.R;
import com.ravenioet.notey.components.provider.ThemeProvider;
import com.ravenioet.notey.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends ServiceProvider {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        initTheme();
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        initNav();
    }

    private void initTheme() {
        binding.toolbar.setBackgroundColor(ThemeProvider.getMainTheme().getPrimaryDark());
        binding.getRoot().setBackgroundColor(ThemeProvider.getMainTheme().getPrimaryLight());
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ThemeProvider.getMainTheme().getPrimaryDark());
        getWindow().setNavigationBarColor(ThemeProvider.getMainTheme().getPrimaryDark());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSystem();
        if (isPinCodeEnabled()) {
            requireBioPass();
        }
    }

    private void initNav() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    }

    private void initSystem() {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            String title = item.getTitle().toString();
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, s.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE); // provide whatever color you want here.
            item.setTitle(s);
        }
        // MenuItem mColorFullMenuBtn = menu.findItem(R.id.action_submit); // extract the menu item here


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_secured:
                if (navController.getCurrentDestination() != navController.findDestination(R.id.SecuredNotes)) {
                    navController.navigate(R.id.SecuredNotes);
                }
                break;
            case R.id.action_trash:
                if (navController.getCurrentDestination() != navController.findDestination(R.id.DeletedNotes)) {
                    navController.navigate(R.id.DeletedNotes);
                }
                break;
            case R.id.action_settings:
                if (navController.getCurrentDestination() != navController.findDestination(R.id.NavSettings)) {
                    navController.navigate(R.id.NavSettings);
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

    private void requireBioPass() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("update", false);
        bundle.putBoolean("enable", false);
        bundle.putBoolean("reset", false);
        navController.navigate(R.id.NavSecuredInput, bundle);
    }

}