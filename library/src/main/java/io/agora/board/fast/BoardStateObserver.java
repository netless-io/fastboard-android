package io.agora.board.fast;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;

/**
 * @author fenglibin
 */
public interface BoardStateObserver {
    default void onSceneStateChanged(SceneState sceneState) {

    }

    default void onMemberStateChanged(MemberState memberState) {

    }

    default void onBroadcastStateChanged(BroadcastState broadcastState) {

    }

    default void onRoomPhaseChanged(RoomPhase roomPhase) {

    }

    default void onRedoUndoChanged(RedoUndoCount count) {

    }

    default void onGlobalStyleChanged(FastStyle style) {

    }

    default void onFastRoomCreated(FastRoom fastRoom) {

    }
}
