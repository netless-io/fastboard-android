package io.agora.board.fast.internal;

import android.util.SparseArray;

import io.agora.board.fast.FastContext;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.ui.OverlayLayout;
import io.agora.board.fast.ui.RoomController;

/**
 * @author fenglibin
 */
public class FastOverlayHandler implements OverlayHandler {

    private final SparseArray<RoomController> controllers = new SparseArray<>();
    private final OverlayLayout overlayLayer;
    private final FastContext fastContext;

    private int showKey = OverlayHandler.KEY_NO_OVERLAY;

    public FastOverlayHandler(FastContext fastContext, OverlayLayout overlayLayer) {
        this.fastContext = fastContext;
        this.overlayLayer = overlayLayer;
        this.overlayLayer.hide();
        this.overlayLayer.setOnClickListener(v -> {
            hideAll();
        });
    }

    @Override
    public void addOverlay(int key, RoomController controller) {
        controllers.append(key, controller);
    }

    @Override
    public void show(int key) {
        hideController(showKey);
        showController(key);
        notifyShowChanged(key);
    }

    @Override
    public void hide(int key) {
        hideController(key);
        notifyShowChanged(OverlayHandler.KEY_NO_OVERLAY);
    }

    private void hideController(int key) {
        RoomController controller = controllers.get(key);
        if (controller != null) {
            controller.hide();
        }
    }

    private void showController(int key) {
        RoomController controller = controllers.get(key);
        if (controller != null) {
            controller.show();
        }
    }

    @Override
    public boolean isShowing(int key) {
        RoomController controller = controllers.get(key);
        if (controller != null) {
            return controller.isShowing();
        }
        return false;
    }

    @Override
    public void hideAll() {
        for (int i = 0; i < controllers.size(); i++) {
            RoomController controller = controllers.valueAt(i);
            controller.hide();
        }
        notifyShowChanged(OverlayHandler.KEY_NO_OVERLAY);
    }

    void notifyShowChanged(int key) {
        if (key != showKey) {
            showKey = key;
            if (key == KEY_NO_OVERLAY) {
                overlayLayer.hide();
            } else {
                overlayLayer.show();
            }
            fastContext.notifyOverlayChanged(key);
        }
    }
}
