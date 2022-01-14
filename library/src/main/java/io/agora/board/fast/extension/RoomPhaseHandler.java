package io.agora.board.fast.extension;

import com.herewhite.sdk.domain.RoomPhase;

import org.jetbrains.annotations.NotNull;

/**
 * A RoomPhaseHandler is a class enable caller to redefine handle when Room Phase Changed.
 */
public interface RoomPhaseHandler {
    void handleRoomPhase(@NotNull RoomPhase roomPhase);
}
