package com.ravenioet.notey.components.provider;

import com.ravenioet.notey.components.theme.ActiveTheme;
import com.ravenioet.notey.components.theme.NoteyTheme;

public class ThemeProvider {
    public ThemeProvider(){

    }
    public static ActiveTheme getMainTheme(){
        return NoteyTheme.getInstance().getActiveTheme();
    }
}
