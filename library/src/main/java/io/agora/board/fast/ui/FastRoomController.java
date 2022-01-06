package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import io.agora.board.fast.FastSdk;
import io.agora.board.fast.R;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class FastRoomController extends RelativeLayout implements RoomController {
    private RedoUndoLayout redoUndoLayout;
    private ScenesLayout scenesLayout;
    private ToolboxLayout toolboxLayout;

    public FastRoomController(Context context) {
        this(context, null);
    }

    public FastRoomController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastRoomController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fast_room_controller, this, true);

        redoUndoLayout = root.findViewById(R.id.fast_redo_undo_layout);
        scenesLayout = root.findViewById(R.id.fast_scenes_layout);
        toolboxLayout = root.findViewById(R.id.fast_toolbox_layout);
    }

    @Override
    public void attachSdk(FastSdk fastSdk) {
        redoUndoLayout.attachSdk(fastSdk);
        scenesLayout.attachSdk(fastSdk);
        toolboxLayout.attachSdk(fastSdk);
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        redoUndoLayout.setFastStyle(fastStyle);
        scenesLayout.setFastStyle(fastStyle);
        toolboxLayout.setFastStyle(fastStyle);
    }
}
