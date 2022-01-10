package io.agora.board.fast;

import android.content.Context;

import androidx.annotation.NonNull;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.SceneState;

import java.util.concurrent.CopyOnWriteArrayList;

import io.agora.board.fast.internal.DefaultErrorHandler;
import io.agora.board.fast.internal.FastErrorHandler;
import io.agora.board.fast.internal.Util;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;
import io.agora.board.fast.ui.ResourceFetcher;

public class FastContext {
    volatile FastErrorHandler errorHandler;
    @NonNull
    volatile ResourceFetcher resourceFetcher;
    FastStyle fastStyle;
    FastRoom fastRoom;
    FastSdk fastSdk;
    CopyOnWriteArrayList<FastListener> listeners = new CopyOnWriteArrayList<>();

    public FastContext(Context context) {
        this.resourceFetcher = ResourceFetcher.get();
        this.resourceFetcher.init(context);
        this.errorHandler = new DefaultErrorHandler(Util.getActivity(context));
    }

    void enableDefaultErrorHandler() {
        addListener(new FastListener() {
            @Override
            public void onFastError(FastException error) {
                errorHandler.handleError(error);
            }
        });
    }

    void initFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
    }

    void updateFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
        notifyListeners(listener -> listener.onFastStyleChanged(fastStyle));
    }

    FastStyle getFastStyle() {
        return fastStyle;
    }

    public void addListener(FastListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void removeListener(FastListener listener) {
        listeners.remove(listener);
    }

    public void notifyRoomPhaseChanged(RoomPhase phase) {
        notifyListeners(listener -> listener.onRoomPhaseChanged(phase));
    }

    public void notifyBroadcastStateChanged(BroadcastState broadcastState) {
        notifyListeners(listener -> listener.onBroadcastStateChanged(broadcastState));
    }

    public void notifyMemberStateChanged(MemberState memberState) {
        notifyListeners(listener -> listener.onMemberStateChanged(memberState));
    }

    public void notifySceneStateChanged(SceneState sceneState) {
        notifyListeners(listener -> listener.onSceneStateChanged(sceneState));
    }

    public void notifyRedoUndoChanged(RedoUndoCount count) {
        notifyListeners(listener -> listener.onRedoUndoChanged(count));
    }

    public void notifyFastRoomCreated(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
        notifyListeners(listener -> listener.onFastRoomCreated(fastRoom));
    }

    public void notifyFastSdkCreated(FastSdk fastSdk) {
        this.fastSdk = fastSdk;
        notifyListeners(listener -> listener.onFastSdkCreated(fastSdk));
    }

    public void notifyFastError(FastException error) {
        notifyListeners(listener -> listener.onFastError(error));
    }

    private void notifyListeners(ListenerInvocation listenerInvocation) {
        CopyOnWriteArrayList<FastListener> listenerSnapshot = new CopyOnWriteArrayList<>(listeners);
        for (FastListener listener : listenerSnapshot) {
            listenerInvocation.invokeListener(listener);
        }
    }

    protected interface ListenerInvocation {
        void invokeListener(FastListener listener);
    }
}
