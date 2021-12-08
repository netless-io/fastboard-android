package io.agora.board.fast.model;

import androidx.annotation.ColorInt;

/**
 * @author fenglibin
 */
public class FastStyle {
    private int mainColor;
    private boolean darkMode;

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
