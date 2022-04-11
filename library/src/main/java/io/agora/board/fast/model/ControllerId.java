package io.agora.board.fast.model;

import io.agora.board.fast.R;

public enum ControllerId {
    RedoUndo(R.id.fast_redo_undo_layout),
    ToolBox(R.id.fast_toolbox_layout),
    PageIndicator(R.id.fast_scenes_layout),
    ;

    private int layoutId;

    ControllerId(int id) {
        layoutId = id;
    }

    public int getLayoutId() {
        return layoutId;
    }
}
