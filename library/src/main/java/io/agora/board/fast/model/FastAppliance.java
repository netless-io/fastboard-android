package io.agora.board.fast.model;

import com.herewhite.sdk.domain.Appliance;
import com.herewhite.sdk.domain.ShapeType;

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
}
