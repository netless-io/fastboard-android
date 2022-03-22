package io.agora.board.fast;

/**
 * @author fenglibin
 */
public interface FastReplayListener {
    default void onReplayReadyChanged(FastReplay fastReplay) {

    }

    default void onFastError(FastException error) {

    }
}
