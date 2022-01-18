package io.agora.board.fast.ui;

import androidx.appcompat.widget.LinearLayoutCompat;

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

    public void setToolboxExpand(boolean expand) {
        ToolboxLayout toolboxLayout = fastboardView.findViewById(R.id.fast_toolbox_layout);
        toolboxLayout.setLayoutMode(expand);
    }

    public void setRedoUndoOrientation(@LinearLayoutCompat.OrientationMode int orientation) {
        RedoUndoLayout redoUndoLayout = fastboardView.findViewById(R.id.fast_redo_undo_layout);
        redoUndoLayout.setOrientation(orientation);
    }

    /**
     * hide default controller
     */
    public void hideRoomControllerGroup() {
        RoomControllerGroup rootRoomController = fastboardView.getRootRoomController();
        rootRoomController.hide();
    }
}
