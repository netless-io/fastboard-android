package io.agora.board.fast.ui;

import android.app.Application;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

import io.agora.board.fast.extension.FastResource;
import io.agora.board.fast.model.FastAppliance;

public class ResourceFetcher {
    private static ResourceFetcher instance;
    private FastResource resource;
    private Application context;

    private ResourceFetcher() {
        resource = new FastResource();
    }

    public static synchronized ResourceFetcher get() {
        if (instance == null) {
            instance = new ResourceFetcher();
        }
        return instance;
    }

    public void init(Application context) {
        this.context = context;
        resource.init(context);
    }

    public void setResource(FastResource resource) {
        resource.init(context);
        this.resource = resource;
    }

    public Drawable getBackground(boolean darkMode) {
        return resource.getBackground(darkMode);
    }

    public Drawable getLayoutBackground(boolean darkMode) {
        return resource.getLayoutBackground(darkMode);
    }

    public Drawable getButtonBackground(boolean darkMode) {
        return resource.getButtonBackground(darkMode);
    }

    @ColorInt
    public int getBackgroundColor(boolean darkMode) {
        return resource.getBackgroundColor(darkMode);
    }

    public ColorStateList getIconColor(boolean darkMode) {
        return resource.getIconColor(darkMode);
    }

    public ColorStateList getIconColor(boolean darkMode, boolean changeEnable) {
        return resource.getIconColor(darkMode, changeEnable);
    }

    public ColorStateList getTextColor(boolean darkMode) {
        return resource.getTextColor(darkMode);
    }

    public Drawable createColorBackground(int mainColor) {
        return resource.createColorBackground(mainColor);
    }

    public Drawable createApplianceBackground(boolean darkMode) {
        return resource.createApplianceBackground(darkMode);
    }

    public Drawable getColorDrawable(int color) {
        return resource.getColorDrawable(color);
    }

    public int getApplianceIcon(FastAppliance fastAppliance) {
        return resource.getApplianceIcon(fastAppliance);
    }
}
