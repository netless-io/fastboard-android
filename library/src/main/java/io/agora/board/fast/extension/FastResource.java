package io.agora.board.fast.extension;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.SparseArray;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;

import java.util.HashMap;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.ui.FastColorDrawable;

public class FastResource {
    private static final HashMap<FastAppliance, Integer> iconMap;

    static {
        iconMap = new HashMap<>();
        iconMap.put(FastAppliance.CLICKER, R.drawable.fast_ic_tool_clicker_selector);
        iconMap.put(FastAppliance.SELECTOR, R.drawable.fast_ic_tool_selector_selector);
        iconMap.put(FastAppliance.PENCIL, R.drawable.fast_ic_tool_pencil_selector);
        iconMap.put(FastAppliance.RECTANGLE, R.drawable.fast_ic_tool_rectangle_selector);
        iconMap.put(FastAppliance.ELLIPSE, R.drawable.fast_ic_tool_circle_selector);
        iconMap.put(FastAppliance.TEXT, R.drawable.fast_ic_tool_text_selector);
        iconMap.put(FastAppliance.ERASER, R.drawable.fast_ic_tool_eraser_selector);
        iconMap.put(FastAppliance.LASER_POINTER, R.drawable.fast_ic_tool_raser);
        iconMap.put(FastAppliance.ARROW, R.drawable.fast_ic_tool_arrow_selector);
        iconMap.put(FastAppliance.STRAIGHT, R.drawable.fast_ic_tool_straight_selector);
        iconMap.put(FastAppliance.PENTAGRAM, R.drawable.fast_ic_tool_pentagram_selector);
        iconMap.put(FastAppliance.RHOMBUS, R.drawable.fast_ic_tool_rhombus_selector);
        iconMap.put(FastAppliance.TRIANGLE, R.drawable.fast_ic_tool_triangle_selector);
        iconMap.put(FastAppliance.BUBBLE, R.drawable.fast_ic_tool_balloon_selector);
        iconMap.put(FastAppliance.OTHER_CLEAR, R.drawable.fast_ic_tool_clear);
    }

    // fast color drawable cache
    private final SparseArray<FastColorDrawable> drawables = new SparseArray<>();

    protected Context context;

    public void init(Context context) {
        this.context = context;
    }

    public int getApplianceIcon(FastAppliance fastAppliance) {
        return iconMap.get(fastAppliance);
    }

    public Drawable getBackground(boolean darkMode) {
        ColorDrawable drawable = new ColorDrawable(darkMode ?
                color(R.color.fast_dark_mode_bg) :
                color(R.color.fast_light_mode_bg)
        );
        return drawable;
    }

    public Drawable getLayoutBackground(boolean darkMode) {
        GradientDrawable drawable = new GradientDrawable();
        if (darkMode) {
            drawable.setStroke(dp2px(1f), color(R.color.fast_dark_mode_border_bg));
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

    @ColorInt
    public int getBackgroundColor(boolean darkMode) {
        return color(darkMode
                ? R.color.fast_dark_mode_bg
                : R.color.fast_light_mode_bg
        );
    }

    /**
     * config icon image tint color
     *
     * @param darkMode
     * @return
     */
    public ColorStateList getIconColor(boolean darkMode) {
        int color = color(darkMode
                ? R.color.fast_dark_mode_icon_color
                : R.color.fast_light_mode_icon_color
        );
        return ColorStateList.valueOf(color);
    }

    public ColorStateList getIconColor(boolean darkMode, boolean changeEnable) {
        int color = color(darkMode
                ? R.color.fast_dark_mode_icon_color
                : R.color.fast_light_mode_icon_color
        );
        int colorDisable = color(darkMode
                        ? R.color.fast_dark_mode_icon_color
                        : R.color.fast_light_mode_icon_color
                , R.dimen.fast_default_disable_alpha
        );

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{-android.R.attr.state_pressed},
                new int[]{},
        };
        int[] colors = new int[]{
                changeEnable ? colorDisable : color,
                color,
                color
        };

        return new ColorStateList(states, colors);
    }

    public ColorStateList getTextColor(boolean darkMode) {
        int color = color(darkMode
                ? R.color.fast_dark_mode_text_on_bg
                : R.color.fast_light_mode_text_on_bg
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
        shape.setCornerRadius(dimenPx(R.dimen.fast_bg_color_item_radius));
        shape.setColor(darkMode ?
                color(R.color.fast_dark_mode_bg_selected) :
                color(R.color.fast_light_mode_bg_selected)
        );

        StateListDrawable state = new StateListDrawable();
        state.addState(new int[]{android.R.attr.state_selected}, shape);
        return state;
    }

    public Drawable getColorDrawable(int color) {
        FastColorDrawable drawable = drawables.get(color);
        if (drawable == null) {
            drawable = createColorDrawable(color);
            drawables.append(color, drawable);
        }
        return drawable;
    }

    @NonNull
    private FastColorDrawable createColorDrawable(int color) {
        int borderWidth = dimenPx(R.dimen.fast_color_border_width);
        int borderRadius = dimenPx(R.dimen.fast_color_border_radius);

        return new FastColorDrawable(color, borderWidth, borderRadius);
    }

    protected int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                context.getResources().getDisplayMetrics()
        );
    }

    protected int dimenPx(@DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    protected int color(@ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    protected int color(@ColorRes int id, @DimenRes int alphaId) {
        float alphaF = ResourcesCompat.getFloat(context.getResources(), alphaId);
        return ColorUtils.setAlphaComponent(color(id), (int) (alphaF * 255));
    }
}
