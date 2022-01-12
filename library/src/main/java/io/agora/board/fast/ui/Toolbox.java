package io.agora.board.fast.ui;

import com.herewhite.sdk.domain.ShapeType;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.model.FastStyle;

public interface Toolbox {
    void setupView(ToolboxLayout toolboxLayout);

    void setFastRoom(FastRoom fastRoom);

    void setFastStyle(FastStyle fastStyle);

    void updateAppliance(String appliance, ShapeType shapeType);

    void updateStroke(int[] strokeColor, double strokeWidth);

    void updateOverlayChanged(int key);

    void setLayoutGravity(int gravity);
}
