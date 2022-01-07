package io.agora.board.fast;

import android.content.Context;

import androidx.annotation.NonNull;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.internal.BoardStateObservable;
import io.agora.board.fast.internal.DefaultErrorHandler;
import io.agora.board.fast.internal.FastErrorHandler;
import io.agora.board.fast.internal.Util;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;
import io.agora.board.fast.ui.ResourceFetcher;

public class FastContext {
    @NonNull
    volatile FastErrorHandler errorHandler;
    @NonNull
    volatile ResourceFetcher resourceFetcher;
    BoardStateObservable observable = new BoardStateObservable();
    FastStyle fastStyle;
    FastRoom fastRoom;
    FastSdk fastSdk;

    public FastContext(Context context) {
        this.resourceFetcher = ResourceFetcher.get();
        this.resourceFetcher.init(context);
        this.errorHandler = new DefaultErrorHandler(Util.getActivity(context));
    }

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
        if (fastRoom != null) {
            observer.onFastRoomCreated(fastRoom);
        }
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

    public void notifySceneStateChanged(SceneState sceneState) {
        observable.notifySceneStateChanged(sceneState);
    }

    public void notifyRedoUndoChanged(RedoUndoCount count) {
        observable.notifyRedoUndoChanged(count);
    }

    public void notifyFastRoomCreated(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
        observable.notifyFastRoomCreated(fastRoom);
    }

    public void notifyFastSdkCreated(FastSdk fastSdk) {
        this.fastSdk = fastSdk;
        observable.notifyFastSdkCreated(fastSdk);
    }
}
