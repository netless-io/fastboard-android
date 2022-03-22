package io.agora.board.fast.ui;

import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;

import io.agora.board.fast.FastboardView;
import io.agora.board.fast.R;

/**
 * a class to controller ui
 *
 * @author fenglibin
 * @experiment
 */
public class FastUiSettings {
    private final FastboardView fastboardView;

    public FastUiSettings(FastboardView fastboardView) {
        this.fastboardView = fastboardView;
    }

    /**
     * change toolbox gravity
     *
     * @param gravity Gravity.LEFT : Gravity.RIGHT
     */
    public void setToolboxGravity(int gravity) {
        ToolboxLayout toolbox = fastboardView.findViewById(R.id.fast_toolbox_layout);
        toolbox.setLayoutGravity(gravity);
    }

    /**
     * change toolbox expand or not
     *
     * @param expand true display as expand mode
     */
    public void setToolboxExpand(boolean expand) {
        ToolboxLayout toolboxLayout = fastboardView.findViewById(R.id.fast_toolbox_layout);
        toolboxLayout.setLayoutMode(expand);
    }

    public void setRedoUndoOrientation(@LinearLayoutCompat.OrientationMode int orientation) {
        RedoUndoLayout redoUndoLayout = fastboardView.findViewById(R.id.fast_redo_undo_layout);
        redoUndoLayout.setOrientation(orientation);
    }

    /**
     * hide controllers
     * ids defined in ids.xml
     * <p>
     * R.id.fast_redo_undo_layout
     * R.id.fast_scenes_layout
     * R.id.fast_toolbox_layout
     *
     * @param ids
     */
    public void hideRoomController(int... ids) {
        for (int id : ids) {
            View view = fastboardView.findViewById(id);
            if (view instanceof RoomController) {
                ((RoomController) view).hide();
            }
        }
    }
}
