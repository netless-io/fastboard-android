package io.agora.board.fast.ui;

import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.extension.FastVisiable;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastStyle;

/**
 * support user customized views
 */
public interface RoomController extends FastVisiable {

    default void setFastRoom(FastRoom fastRoom) {

    }

    default void updateSceneState(SceneState sceneState) {

    }

    default void updateMemberState(MemberState memberState) {

    }

    default void updateWindowBoxState(String windowBoxState) {

    }

    default void updateRedoUndo(FastRedoUndo count) {

    }

    default void updateOverlayChanged(int key) {

    }

    default void updateFastStyle(FastStyle style) {

    }

    @Override
    default void show() {

    }

    @Override
    default void hide() {

    }

    @Override
    default boolean isShowing() {
        return false;
    }
}