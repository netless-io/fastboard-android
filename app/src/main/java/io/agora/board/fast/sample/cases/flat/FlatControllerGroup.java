package io.agora.board.fast.sample.cases.flat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import io.agora.board.fast.model.FastWindowBoxState;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.misc.Utils;
import io.agora.board.fast.ui.RedoUndoLayout;
import io.agora.board.fast.ui.RoomControllerGroup;
import io.agora.board.fast.ui.ScenesLayout;
import io.agora.board.fast.ui.ToolboxLayout;

/**
 * @author fenglibin
 */
public class FlatControllerGroup extends RoomControllerGroup {
    private RedoUndoLayout redoUndoLayout;
    private ScenesLayout scenesLayout;
    private ToolboxLayout toolboxLayout;

    public FlatControllerGroup(ViewGroup root) {
        super(root);
        setupView();
    }

    private void setupView() {
        LayoutInflater.from(context).inflate(R.layout.layout_flat_controller_group, root, true);

        redoUndoLayout = root.findViewById(R.id.redo_undo_layout);
        scenesLayout = root.findViewById(R.id.scenes_layout);
        toolboxLayout = root.findViewById(R.id.toolbox_layout);

        addController(redoUndoLayout);
        addController(scenesLayout);
        addController(toolboxLayout);

        if (Utils.isPhone(context)) {
            redoUndoLayout.hide();
            scenesLayout.hide();
        }
    }

    @Override
    public void updateWindowBoxState(String windowBoxState) {
        super.updateWindowBoxState(windowBoxState);
        if (Utils.isPhone(context)) {
            return;
        }
        FastWindowBoxState boxState = FastWindowBoxState.of(windowBoxState);
        switch (boxState) {
            case Maximized:
                redoUndoLayout.hide();
                scenesLayout.hide();
                break;
            case Minimized:
            case Normal:
                redoUndoLayout.show();
                scenesLayout.show();
                break;
        }
    }
}
