package com.ravenioet.notey.components.theme;

import com.ravenioet.notey.init.ServiceProvider;

public class NoteyTheme extends ServiceProvider {
    NoteyBlue noteyBlue = new NoteyBlue();
    NoteyDark noteyDark = new NoteyDark();
    NoteyOrange noteyOrange = new NoteyOrange();
    ActiveTheme activeTheme;public static NoteyTheme noteyTheme;
    public static NoteyTheme getInstance(){
        if(noteyTheme == null){
            noteyTheme = new NoteyTheme();
        }
        return noteyTheme;
    }
    public ActiveTheme getActiveTheme() {
        switch (getSavedTheme()){
            case blue:
                return blueTheme();
            case orange:
                return orangeTheme();
            default:
                return darkTheme();
        }
    }

    public void setActiveTheme(Themes theme) {
        switch (theme){
            case dark:
                activeTheme = darkTheme();
                break;
            case blue:
                activeTheme = blueTheme();
                break;
            case orange:
                activeTheme = orangeTheme();
                break;
        }
    }
    public ActiveTheme darkTheme(){
        setActiveTheme("dark");
        return new ActiveTheme(noteyDark.primaryDark,noteyDark.primaryLight,noteyDark.textColor);
    }
    public ActiveTheme blueTheme(){
        setActiveTheme("blue");
        return new ActiveTheme(noteyBlue.primaryDark,noteyBlue.primaryLight,noteyBlue.textColor);
    }
    public ActiveTheme orangeTheme(){
        setActiveTheme("orange");
        return new ActiveTheme(noteyOrange.primaryDark,noteyOrange.primaryLight,noteyOrange.textColor);
    }


}

