package io.agora.board.fast.ui;

import io.agora.board.fast.model.FastAppliance;

/**
 * Tablet toolbox model
 *
 * @author fenglibin
 */
class ToolboxItem {
    static final int KEY_CLICK = 0;
    static final int KEY_SELECTOR = 1;
    static final int KEY_PENCIL = 2;
    static final int KEY_TEXT = 3;
    static final int KEY_ERASER = 4;
    static final int KEY_SHAPE = 5;
    static final int KEY_CLEAR = 6;
    static final int KEY_APP = 7;

    int key;
    FastAppliance appliance;
    boolean expandable;

    public ToolboxItem(int key, FastAppliance applianceItem) {
        this(key, applianceItem, false);
    }

    public ToolboxItem(int key, FastAppliance appliance, boolean expandable) {
        this.key = key;
        this.appliance = appliance;
        this.expandable = expandable;
    }
}
