package io.agora.board.fast;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.PageState;
import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public interface FastListener {
    default void onRoomReadyChanged(FastRoom fastRoom) {

    }

    default void onFastStyleChanged(FastStyle style) {

    }

    default void onSceneStateChanged(SceneState sceneState) {

    }

    default void onPageStateChanged(PageState pageState) {

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

    default void onReplayReadyChanged(FastReplay fastReplay) {

    }

    default void onFastError(FastException error) {

    }
}
