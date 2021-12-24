package io.agora.board.fast.internal;

import android.database.Observable;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.BoardStateObserver;
import io.agora.board.fast.FastRoom;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;

/**
 * @author fenglibin
 */
public class BoardStateObservable extends Observable<BoardStateObserver> {
    public void notifyMemberStateChanged(MemberState memberState) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onMemberStateChanged(memberState);
        }
    }

    public void notifyBroadcastStateChanged(BroadcastState broadcastState) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onBroadcastStateChanged(broadcastState);
        }
    }

    public void notifySceneStateChanged(SceneState sceneState) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onSceneStateChanged(sceneState);
        }
    }


    public void notifyRoomPhaseChanged(RoomPhase roomPhase) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onRoomPhaseChanged(roomPhase);
        }
    }

    public void notifyRedoUndoChanged(RedoUndoCount count) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onRedoUndoChanged(count);
        }
    }

    public void notifyGlobalStyleChanged(FastStyle fastStyle) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onGlobalStyleChanged(fastStyle);
        }
    }

    public void notifyFastRoomCreated(FastRoom fastRoom) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onFastRoomCreated(fastRoom);
        }
    }
}
