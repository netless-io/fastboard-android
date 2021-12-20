package io.agora.board.fast.internal;

import io.agora.board.fast.FastException;

/**
 * @author fenglibin
 */
public interface FastErrorHandler {
    default void onSdkInitError(FastException e) {

    }

    void onJoinRoomError(FastException e);

    void onJoinPlayerError(FastException e);
}
