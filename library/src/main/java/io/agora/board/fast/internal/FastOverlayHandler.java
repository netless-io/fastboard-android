package io.agora.board.fast.internal;

import io.agora.board.fast.extension.OverlayHandler;

public class FastOverlayHandler implements OverlayHandler {
    private final FastRoomContext fastContext;

    public FastOverlayHandler(FastRoomContext fastContext) {
        this.fastContext = fastContext;
    }

    @Override
    public void handleOverlayChanged(int key) {
        fastContext.notifyOverlayChanged(key);
    }
}
