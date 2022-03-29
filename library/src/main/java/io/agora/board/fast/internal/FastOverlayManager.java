package io.agora.board.fast.internal;

import android.util.SparseArray;

import io.agora.board.fast.extension.FastVisiable;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.ui.OverlayLayout;

/**
 * @author fenglibin
 */
public class FastOverlayManager implements OverlayManager {

    private final SparseArray<FastVisiable> visiables = new SparseArray<>();
    private final OverlayLayout overlayLayer;
    private final OverlayHandler overlayHandler;

    private int showKey = OverlayManager.KEY_NO_OVERLAY;

    public FastOverlayManager(OverlayLayout overlayLayer, OverlayHandler overlayHandler) {
        this.overlayLayer = overlayLayer;
        this.overlayHandler = overlayHandler;
        this.overlayLayer.hide();
        this.overlayLayer.setOnClickListener(v -> {
            hideAll();
        });
    }

    @Override
    public void addOverlay(int key, FastVisiable visiable) {
        visiables.append(key, visiable);
    }

    @Override
    public void show(int key) {
        hideController(showKey);
        showController(key);
        handleChanged(key);
    }

    @Override
    public void hide(int key) {
        hideController(key);
        handleChanged(OverlayManager.KEY_NO_OVERLAY);
    }

    private void hideController(int key) {
        FastVisiable visiable = visiables.get(key);
        if (visiable != null) {
            visiable.hide();
        }
    }

    private void showController(int key) {
        FastVisiable visiable = visiables.get(key);
        if (visiable != null) {
            visiable.show();
        }
    }

    @Override
    public boolean isShowing(int key) {
        FastVisiable visiable = visiables.get(key);
        if (visiable != null) {
            return visiable.isShowing();
        }
        return false;
    }

    @Override
    public void hideAll() {
        for (int i = 0; i < visiables.size(); i++) {
            FastVisiable visiable = visiables.valueAt(i);
            visiable.hide();
        }
        handleChanged(OverlayManager.KEY_NO_OVERLAY);
    }

    void handleChanged(int key) {
        if (key != showKey) {
            showKey = key;
            if (key == KEY_NO_OVERLAY) {
                overlayLayer.hide();
            } else {
                overlayLayer.show();
            }
            overlayHandler.handleOverlayChanged(key);
        }
    }
}
