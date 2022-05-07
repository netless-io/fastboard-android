package io.agora.board.fast.model;

import com.herewhite.sdk.domain.Appliance;
import com.herewhite.sdk.domain.ShapeType;

import java.util.HashMap;

/**
 * @author fenglibin
 */
public enum FastAppliance {
    CLICKER(Appliance.CLICKER),
    SELECTOR(Appliance.SELECTOR),
    PENCIL(Appliance.PENCIL),
    RECTANGLE(Appliance.RECTANGLE),
    ELLIPSE(Appliance.ELLIPSE),
    TEXT(Appliance.TEXT),
    ERASER(Appliance.ERASER),
    LASER_POINTER(Appliance.LASER_POINTER),
    ARROW(Appliance.ARROW),
    STRAIGHT(Appliance.STRAIGHT),

    PENTAGRAM(Appliance.SHAPE, ShapeType.Pentagram),
    RHOMBUS(Appliance.SHAPE, ShapeType.Rhombus),
    TRIANGLE(Appliance.SHAPE, ShapeType.Triangle),
    BUBBLE(Appliance.SHAPE, ShapeType.SpeechBalloon),

    OTHER_CLEAR(),
    ;

    private static final HashMap<FastAppliance, Boolean> hasPropertiesMap;

    static {
        hasPropertiesMap = new HashMap<>();
        hasPropertiesMap.put(FastAppliance.CLICKER, false);
        hasPropertiesMap.put(FastAppliance.SELECTOR, false);
        hasPropertiesMap.put(FastAppliance.ERASER, false);
        hasPropertiesMap.put(FastAppliance.LASER_POINTER, false);
        hasPropertiesMap.put(FastAppliance.OTHER_CLEAR, false);

        hasPropertiesMap.put(FastAppliance.PENCIL, true);
        hasPropertiesMap.put(FastAppliance.RECTANGLE, true);
        hasPropertiesMap.put(FastAppliance.ELLIPSE, true);
        hasPropertiesMap.put(FastAppliance.TEXT, true);
        hasPropertiesMap.put(FastAppliance.ARROW, true);
        hasPropertiesMap.put(FastAppliance.STRAIGHT, true);
        hasPropertiesMap.put(FastAppliance.PENTAGRAM, true);
        hasPropertiesMap.put(FastAppliance.RHOMBUS, true);
        hasPropertiesMap.put(FastAppliance.TRIANGLE, true);
        hasPropertiesMap.put(FastAppliance.BUBBLE, true);
    }

    public final String appliance;
    public final ShapeType shapeType;

    FastAppliance() {
        this("", null);
    }

    FastAppliance(String appliance) {
        this(appliance, null);
    }

    FastAppliance(String appliance, ShapeType shapeType) {
        this.appliance = appliance;
        this.shapeType = shapeType;
    }

    public static FastAppliance of(String appliance, ShapeType shapeType) {
        for (FastAppliance item : FastAppliance.values()) {
            if (item.appliance.equals(appliance) && (item.shapeType == null || item.shapeType == shapeType)) {
                return item;
            }
        }
        return CLICKER;
    }

    public boolean hasProperties() {
        if (hasPropertiesMap.containsKey(this)) {
            return hasPropertiesMap.get(this);
        }
        return false;
    }
}
