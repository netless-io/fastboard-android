package io.agora.board.fast.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.SparseArray;
import android.util.TypedValue;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import io.agora.board.fast.library.R;

public class ResourceFetcher {
    private static ResourceFetcher instance;
    private Context context;
    // fast color drawable cache
    private SparseArray<ColorDrawable> drawables = new SparseArray<>();

    public static synchronized ResourceFetcher get() {
        if (instance == null) {
            instance = new ResourceFetcher();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public Drawable getLayoutBackground(boolean darkMode) {
        GradientDrawable drawable = new GradientDrawable();
        if (darkMode) {
            drawable.setStroke(dp2px(1), color(R.color.fast_dark_mode_border_bg));
            drawable.setColor(color(R.color.fast_dark_mode_bg));
        } else {
            drawable.setStroke(dp2px(1f), color(R.color.fast_light_mode_border_bg));
            drawable.setColor(color(R.color.fast_light_mode_bg));
        }
        drawable.setCornerRadius(dp2px(8));
        return drawable;
    }

    public Drawable getButtonBackground(boolean darkMode) {
        GradientDrawable pressedOrSelected = createBtnBgSelected(darkMode);
        GradientDrawable normal = createBtnBgNormal(darkMode);

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressedOrSelected);
        drawable.addState(new int[]{android.R.attr.state_selected}, pressedOrSelected);
        drawable.addState(new int[0], normal);
        return drawable;
    }

    @NonNull
    private GradientDrawable createBtnBgSelected(boolean darkMode) {
        GradientDrawable selected = new GradientDrawable();
        if (darkMode) {
            selected.setStroke(dp2px(1), color(R.color.fast_dark_mode_border_bg));
            selected.setColor(color(R.color.fast_dark_mode_bg_selected));
        } else {
            selected.setStroke(dp2px(1f), color(R.color.fast_light_mode_border_bg));
            selected.setColor(color(R.color.fast_light_mode_bg_selected));
        }
        selected.setCornerRadius(dp2px(8));
        return selected;
    }

    @NonNull
    private GradientDrawable createBtnBgNormal(boolean darkMode) {
        GradientDrawable selected = new GradientDrawable();
        if (darkMode) {
            selected.setStroke(dp2px(1), color(R.color.fast_dark_mode_border_bg));
            selected.setColor(color(R.color.fast_dark_mode_bg));
        } else {
            selected.setStroke(dp2px(1), color(R.color.fast_light_mode_border_bg));
            selected.setColor(color(R.color.fast_light_mode_bg));
        }
        selected.setCornerRadius(dp2px(8));
        return selected;
    }

    public ColorStateList getIconColor(boolean darkMode) {
        int color = color(darkMode
                ? R.color.fast_dark_mode_icon_color
                : R.color.fast_light_mode_icon_color
        );
        return ColorStateList.valueOf(color);
    }

    public Drawable createColorBackground(int mainColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setStroke(dimenPx(R.dimen.fast_bg_color_item_stroke_width), mainColor);
        shape.setCornerRadius(dimenPx(R.dimen.fast_bg_color_item_radius));

        StateListDrawable state = new StateListDrawable();
        state.addState(new int[]{android.R.attr.state_selected}, shape);
        return state;
    }

    public Drawable createApplianceBackground(boolean darkMode) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color(R.color.fast_day_night_bg_selected));
        shape.setCornerRadius(dimenPx(R.dimen.fast_bg_color_item_radius));

        StateListDrawable state = new StateListDrawable();
        state.addState(new int[]{android.R.attr.state_selected}, shape);
        return state;
    }

    public ColorDrawable getColorDrawable(int color) {
        ColorDrawable drawable = drawables.get(color);
        if (drawable == null) {
            drawable = createColorDrawable(color);
            drawables.append(color, drawable);
        }
        return drawable;
    }

    @NonNull
    private ColorDrawable createColorDrawable(int color) {
        int borderWidth = dimenPx(R.dimen.fast_color_border_width);
        int borderRadius = dimenPx(R.dimen.fast_color_border_radius);

        return new ColorDrawable(color, borderWidth, borderRadius);
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                context.getResources().getDisplayMetrics()
        );
    }

    private int dimenPx(@DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    private int color(@ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }
}
