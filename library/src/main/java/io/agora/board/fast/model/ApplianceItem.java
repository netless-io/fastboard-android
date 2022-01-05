package io.agora.board.fast.model;

import androidx.annotation.DrawableRes;

import com.herewhite.sdk.domain.Appliance;
import com.herewhite.sdk.domain.ShapeType;

import io.agora.board.fast.library.R;

/**
 * @author fenglibin
 */
public enum ApplianceItem {
    CLICKER(R.drawable.fast_ic_tool_clicker, Appliance.CLICKER),
    SELECTOR(R.drawable.fast_ic_tool_selector, Appliance.SELECTOR),
    PENCIL(R.drawable.fast_ic_tool_pencil, Appliance.PENCIL),
    RECTANGLE(R.drawable.fast_ic_tool_rectangle, Appliance.RECTANGLE),
    ELLIPSE(R.drawable.fast_ic_tool_circle, Appliance.ELLIPSE),
    TEXT(R.drawable.fast_ic_tool_text, Appliance.TEXT),
    ERASER(R.drawable.fast_ic_tool_eraser, Appliance.ERASER),
    LASER_POINTER(R.drawable.fast_ic_tool_raser, Appliance.LASER_POINTER),
    ARROW(R.drawable.fast_ic_tool_arrow, Appliance.ARROW),
    STRAIGHT(R.drawable.fast_ic_tool_straight, Appliance.STRAIGHT),
    // HAND(R.drawable.ic_toolbox_hand_selector, Appliance.HAND),

    PENTAGRAM(R.drawable.fast_ic_shape_pentagram, Appliance.SHAPE, ShapeType.Pentagram),
    RHOMBUS(R.drawable.fast_ic_shape_rhombus, Appliance.SHAPE, ShapeType.Rhombus),
    TRIANGLE(R.drawable.fast_ic_shape_triangle, Appliance.SHAPE, ShapeType.Triangle),
    BUBBLE(R.drawable.fast_ic_shape_speechballoon, Appliance.SHAPE, ShapeType.SpeechBalloon),

    OTHER_CLEAR(R.drawable.fast_ic_tool_clear),
    ;

    @DrawableRes
    public final int icon;
    public final String appliance;
    public final ShapeType shapeType;

    ApplianceItem(@DrawableRes int icon) {
        this(icon, "", null);
    }

    ApplianceItem(@DrawableRes int icon, String appliance) {
        this(icon, appliance, null);
    }

    ApplianceItem(@DrawableRes int icon, String appliance, ShapeType shapeType) {
        this.icon = icon;
        this.appliance = appliance;
        this.shapeType = shapeType;
    }

    public static ApplianceItem of(String appliance, ShapeType shapeType) {
        for (ApplianceItem item : ApplianceItem.values()) {
            if (item.appliance.equals(appliance) && (item.shapeType == null || item.shapeType == shapeType)) {
                return item;
            }
        }
        return CLICKER;
    }
}
