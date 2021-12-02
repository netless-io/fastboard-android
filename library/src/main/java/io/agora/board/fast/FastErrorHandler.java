package io.agora.board.fast;

/**
 * @author fenglibin
 */
public interface FastErrorHandler {
    default void onSdkInitError() {

    }

    void onJoinRoomError();
}
