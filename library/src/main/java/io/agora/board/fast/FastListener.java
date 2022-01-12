package io.agora.board.fast;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;

/**
 * @author fenglibin
 */
public interface FastListener {
    default void onSceneStateChanged(SceneState sceneState) {

    }

    default void onMemberStateChanged(MemberState memberState) {

    }

    default void onBroadcastStateChanged(BroadcastState broadcastState) {

    }

    default void onRedoUndoChanged(RedoUndoCount count) {

    }

    default void onOverlayChanged(int key) {
        
    }

    default void onFastStyleChanged(FastStyle style) {

    }

    default void onFastRoomCreated(FastRoom fastRoom) {

    }

    default void onFastSdkCreated(FastSdk fastSdk) {

    }

    default void onFastError(FastException error) {

    }
}
