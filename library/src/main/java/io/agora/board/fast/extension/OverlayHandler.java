package io.agora.board.fast.extension;

import io.agora.board.fast.ui.RoomController;

public interface OverlayHandler {
    // reserve 0-10
    int KEY_NO_OVERLAY = 0;
    int KEY_TOOL_LAYOUT = 1;
    int KEY_TOOL_EXTENSION = 2;

    void addOverlay(int key, RoomController controller);

    void show(int key);

    void hide(int key);

    boolean isShowing(int key);

    void hideAll();
}
