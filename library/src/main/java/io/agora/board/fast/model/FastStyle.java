package io.agora.board.fast.model;

import android.graphics.Color;

import androidx.annotation.ColorInt;

/**
 * @author fenglibin
 */
public class FastStyle {
    private int mainColor = Color.parseColor("#3381FF");
    private boolean darkMode = false;

    public FastStyle() {
    }

    public void setMainColor(@ColorInt int color) {
        this.mainColor = color;
    }

    public int getMainColor() {
        return mainColor;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public boolean isDarkMode() {
        return darkMode;
    }
}
