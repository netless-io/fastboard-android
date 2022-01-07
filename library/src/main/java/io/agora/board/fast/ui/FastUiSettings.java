package io.agora.board.fast.ui;

import android.view.View;
import android.widget.FrameLayout;

import com.herewhite.sdk.WhiteboardView;

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
    public void hideRoomController() {
        FastRoomController roomController = fastboardView.findViewById(R.id.fast_room_controller);
        roomController.setVisibility(View.GONE);
    }

    /**
     * @param ratio w / h
     */
    public void setWhiteboardRatio(float ratio) {
        WhiteboardView whiteboardView = fastboardView.findViewById(R.id.fast_whiteboard_view);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) whiteboardView.getLayoutParams();
        lp.height = (int) (lp.width / ratio);
    }
}
