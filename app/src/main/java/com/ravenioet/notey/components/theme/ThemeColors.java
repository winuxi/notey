package com.ravenioet.notey.components.theme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.ravenioet.notey.R;
import com.ravenioet.notey.init.ServiceProvider;

public class ThemeColors extends ServiceProvider{
        private static final String NAME = "ThemeColors", KEY = "color";

        @ColorInt
        private int color;

        public ThemeColors(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            String stringColor = sharedPreferences.getString(KEY, "004bff");
            color = Color.parseColor("#" + stringColor);

            if (isLightActionBar()) context.setTheme(R.style.Theme_Notey);
            context.setTheme(context.getResources().getIdentifier(sharedPreferences.getString("active-theme","dark"),
                    "style", context.getPackageName()));
        }

        public static void setNewThemeColor(AppCompatActivity activity, int red, int green, int blue) {
            red = Math.round(red / 15.0f) * 15;
            green = Math.round(green / 15.0f) * 15;
            blue = Math.round(blue / 15.0f) * 15;

            String stringColor = Integer.toHexString(Color.rgb(red, green, blue)).substring(2);
            SharedPreferences.Editor editor = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
            editor.putString(KEY, stringColor);
            editor.apply();
            Toast.makeText(activity.getApplicationContext(), stringColor,Toast.LENGTH_LONG).show();
            activity.recreate();
        }

        private boolean isLightActionBar() {// Checking if title text color will be black
            int rgb = (Color.red(color) + Color.green(color) + Color.blue(color)) / 3;
            return rgb > 210;
        }

}
