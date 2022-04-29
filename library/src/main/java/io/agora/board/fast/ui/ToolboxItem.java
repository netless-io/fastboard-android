package io.agora.board.fast.ui;

import java.util.List;

import io.agora.board.fast.model.FastAppliance;

/**
 * Tablet toolbox model
 *
 * @author fenglibin
 */
class ToolboxItem {
    List<FastAppliance> appliances;
    int index;

    public ToolboxItem(List<FastAppliance> appliances) {
        this.appliances = appliances;
        this.index = 0;
    }

    public FastAppliance current() {
        return appliances.get(index);
    }

    public void update(FastAppliance appliance) {
        int i = appliances.indexOf(appliance);
        if (i != -1) {
            index = i;
        }
    }

    public boolean isExpandable() {
        if (appliances.size() == 1) {
            return appliances.get(0).hasProperties();
        }
        return appliances.size() > 1;
    }
}
