package io.agora.board.fast;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public interface FastListener {
    default void onFastSdkCreated(FastSdk fastSdk) {

    }

    default void onFastStyleChanged(FastStyle style) {

    }

    default void onSceneStateChanged(SceneState sceneState) {

    }

    default void onMemberStateChanged(MemberState memberState) {

    }

    default void onBroadcastStateChanged(BroadcastState broadcastState) {

    }

    default void onWindowBoxStateChanged(String windowBoxState) {

    }

    default void onRedoUndoChanged(FastRedoUndo count) {

    }

    default void onOverlayChanged(int key) {

    }

    default void onFastRoomCreated(FastRoom fastRoom) {

    }

    default void onFastPlayerCreated(FastPlayer fastPlayer) {

    }

    default void onFastError(FastException error) {

    }
}
