package io.agora.board.fast;

import androidx.annotation.NonNull;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.RoomPhase;

import io.agora.board.fast.internal.BoardStateObservable;
import io.agora.board.fast.internal.FastErrorHandler;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;

public class FastContext {
    @NonNull
    volatile FastErrorHandler errorHandler;
    BoardStateObservable observable = new BoardStateObservable();
    FastStyle fastStyle;

    void initFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
    }

    void updateFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
        this.observable.notifyGlobalStyleChanged(fastStyle);
    }

    FastStyle getFastStyle() {
        return fastStyle;
    }

    public void registerObserver(BoardStateObserver observer) {
        observable.registerObserver(observer);
    }

    public void unregisterObserver(BoardStateObserver observer) {
        observable.unregisterObserver(observer);
    }

    public void notifyRoomPhaseChanged(RoomPhase phase) {
        observable.notifyRoomPhaseChanged(phase);
    }

    public void notifyBroadcastStateChanged(BroadcastState broadcastState) {
        observable.notifyBroadcastStateChanged(broadcastState);
    }

    public void notifyMemberStateChanged(MemberState memberState) {
        observable.notifyMemberStateChanged(memberState);
    }

    public void notifyRedoUndoChanged(RedoUndoCount count) {
        observable.notifyRedoUndoChanged(count);
    }

    public void notifyFastRoomCreated(FastRoom fastRoom) {
        observable.notifyFastRoomCreated(fastRoom);
    }
}
