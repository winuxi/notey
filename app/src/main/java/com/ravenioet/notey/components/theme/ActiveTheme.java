package com.ravenioet.notey.components.theme;

public class ActiveTheme {
    int primaryDark;
    int primaryLight;
    int textColor;
    public ActiveTheme(int primaryDark, int primaryLight, int textColor) {
        this.primaryDark = primaryDark;
        this.primaryLight = primaryLight;
        this.textColor = textColor;
    }

    public int getPrimaryDark() {
        return primaryDark;
    }

    public void setPrimaryDark(int primaryDark) {
        this.primaryDark = primaryDark;
    }

    public int getPrimaryLight() {
        return primaryLight;
    }

    public void setPrimaryLight(int primaryLight) {
        this.primaryLight = primaryLight;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
