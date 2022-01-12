package io.agora.board.fast.ui;

import io.agora.board.fast.FastboardView;
import io.agora.board.fast.R;

/**
 * @author fenglibin
 * @experiment
 */
public class FastUiSettings {
    private final FastboardView fastboardView;

    public FastUiSettings(FastboardView fastboardView) {
        this.fastboardView = fastboardView;
    }

    public void setToolboxGravity(int gravity) {
        ToolboxLayout toolbox = fastboardView.findViewById(R.id.fast_toolbox_layout);
        toolbox.setLayoutGravity(gravity);
    }

    /**
     * hide default controller
     */
    public void hideRoomControllerGroup() {
        RoomControllerGroup rootRoomController = fastboardView.getRootRoomController();
        rootRoomController.hide();
    }
}
