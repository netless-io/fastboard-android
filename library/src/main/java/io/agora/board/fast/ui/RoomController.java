package io.agora.board.fast.ui;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.FastSdk;
import io.agora.board.fast.model.FastStyle;

/**
 * support user customized views
 */
public interface RoomController {

    default void attachSdk(FastSdk fastSdk) {

    }

    default void attachRoom(FastRoom fastRoom) {
        
    }

    default void updateFastStyle(FastStyle style) {

    }
}