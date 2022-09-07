package io.agora.board.fast.extension;

public interface OverlayManager {
    // reserve 0-10
    int KEY_NO_OVERLAY = 0;
    int KEY_TOOL_LAYOUT = 1;
    int KEY_TOOL_EXTENSION = 2;
    int KEY_TOOL_EXPAND_EXTENSION = 3;

    void addOverlay(int key, FastVisiable visiable);

    void show(int key);

    void hide(int key);

    boolean isShowing(int key);

    void hideAll();
}
