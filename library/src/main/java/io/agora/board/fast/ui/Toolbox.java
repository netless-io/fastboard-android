package io.agora.board.fast.ui;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

public interface Toolbox {
    void setupView(ToolboxLayout toolboxLayout);

    void setFastRoom(FastRoom fastRoom);

    void updateFastStyle(FastStyle fastStyle);

    void updateAppliance(FastAppliance fastAppliance);

    void updateStroke(int[] strokeColor, double strokeWidth);

    void updateOverlayChanged(int key);

    void setLayoutGravity(int gravity);
}
