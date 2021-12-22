package io.agora.board.fast.model;

import androidx.annotation.DrawableRes;

import com.herewhite.sdk.domain.Appliance;

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
    // TEXT(R.drawable.ic_toolbox_text_selector, Appliance.TEXT),
    ERASER(R.drawable.fast_ic_tool_eraser, Appliance.ERASER),
    // LASER_POINTER(R.drawable.ic_toolbox_laser_selector, Appliance.LASER_POINTER),
    ARROW(R.drawable.fast_ic_tool_arrow, Appliance.ARROW),
    // STRAIGHT(R.drawable.ic_toolbox_line_selector, Appliance.STRAIGHT),
    // HAND(R.drawable.ic_toolbox_hand_selector, Appliance.HAND),

    OTHER_CLEAR(R.drawable.fast_ic_tool_clear, ""),
    ;

    @DrawableRes
    public final int icon;
    public final String appliance;

    ApplianceItem(@DrawableRes int icon, String appliance) {
        this.icon = icon;
        this.appliance = appliance;
    }

    public static ApplianceItem of(String appliance) {
        for (ApplianceItem item : ApplianceItem.values()) {
            if (item.appliance.equals(appliance)) {
                return item;
            }
        }
        return CLICKER;
    }
}
