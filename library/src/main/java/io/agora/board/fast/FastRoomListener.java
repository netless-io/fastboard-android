package io.agora.board.fast;

import com.herewhite.sdk.domain.RoomState;

import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public interface FastRoomListener {
    default void onOverlayChanged(int key) {

    }

    default void onRoomReadyChanged(FastRoom fastRoom) {

    }

    default void onRoomStateChanged(RoomState state) {

    }

    default void onRedoUndoChanged(FastRedoUndo count) {

    }

    default void onFastStyleChanged(FastStyle style) {

    }

    default void onFastError(FastException error) {

    }
}
