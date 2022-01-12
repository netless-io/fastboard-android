package io.agora.board.fast.internal;

import android.view.View;

import com.herewhite.sdk.domain.RoomPhase;

import io.agora.board.fast.extension.RoomPhaseHandler;
import io.agora.board.fast.ui.LoadingLayout;

public class FastRoomPhaseHandler implements RoomPhaseHandler {

    private final LoadingLayout loadingLayout;

    public FastRoomPhaseHandler(LoadingLayout loadingLayout) {
        this.loadingLayout = loadingLayout;
        this.loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void handleRoomPhase(RoomPhase roomPhase) {
        loadingLayout.updateRoomPhase(roomPhase);
    }
}