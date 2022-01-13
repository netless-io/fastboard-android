package io.agora.board.fast.extension;

import com.herewhite.sdk.domain.RoomPhase;

import org.jetbrains.annotations.NotNull;

public interface RoomPhaseHandler {
    void handleRoomPhase(@NotNull RoomPhase roomPhase);
}
