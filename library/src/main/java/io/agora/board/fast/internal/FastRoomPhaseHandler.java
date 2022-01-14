package io.agora.board.fast.internal;

import com.herewhite.sdk.domain.RoomPhase;

import io.agora.board.fast.extension.RoomPhaseHandler;
import io.agora.board.fast.ui.LoadingLayout;

public class FastRoomPhaseHandler implements RoomPhaseHandler {

    private final LoadingLayout loadingLayout;

    public FastRoomPhaseHandler(LoadingLayout loadingLayout) {
        this.loadingLayout = loadingLayout;
        this.loadingLayout.hide();
    }

    @Override
    public void handleRoomPhase(RoomPhase roomPhase) {
        switch (roomPhase) {
            case connecting:
            case reconnecting:
                loadingLayout.show();
                break;
            case connected:
                loadingLayout.hide();
                break;
            case disconnected:
                loadingLayout.showRetry();
            default:
                break;
        }
    }
}