package io.agora.board.fast;

import android.content.Context;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.PageState;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.SceneState;

import java.util.concurrent.CopyOnWriteArrayList;

import io.agora.board.fast.extension.ErrorHandler;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.extension.ResourceImpl;
import io.agora.board.fast.extension.RoomPhaseHandler;
import io.agora.board.fast.internal.FastOverlayHandler;
import io.agora.board.fast.model.FastReplayOptions;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.ResourceFetcher;

public class FastContext {
    final FastboardView fastboardView;
    Context context;
    Fastboard fastboard;
    FastRoom fastRoom;
    FastReplay fastReplay;
    FastStyle fastStyle;

    ErrorHandler errorHandler;
    RoomPhaseHandler roomPhaseHandler;
    OverlayHandler overlayHandler;
    OverlayManager overlayManager;
    ResourceFetcher resourceFetcher;
    CopyOnWriteArrayList<FastListener> listeners = new CopyOnWriteArrayList<>();

    public FastContext(FastboardView fastboardView) {
        this.fastboardView = fastboardView;
        this.context = fastboardView.getContext();
        this.resourceFetcher = ResourceFetcher.get();
        this.resourceFetcher.init(context);
        this.overlayHandler = new FastOverlayHandler(this);
    }

    public FastboardView getFastboardView() {
        return fastboardView;
    }

    public Fastboard getFastboard() {
        if (fastboard == null) {
            fastboard = new Fastboard(fastboardView);
        }
        return fastboard;
    }

    public void joinRoom(FastRoomOptions options, OnRoomReadyCallback onRoomReadyCallback) {
        fastRoom = new FastRoom(this, options, onRoomReadyCallback);
        fastRoom.join();
    }

    public void joinPlayer(FastReplayOptions options, OnPlayerReadyCallback onPlayerReadyCallback) {
        fastReplay = new FastReplay(this, options);
        fastReplay.join();
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void setRoomPhaseHandler(RoomPhaseHandler phaseHandler) {
        this.roomPhaseHandler = phaseHandler;
    }

    public void setOverlayHandler(OverlayHandler overlayHandler) {
        this.overlayHandler = overlayHandler;
    }

    public void setOverlayManager(OverlayManager overlayManager) {
        this.overlayManager = overlayManager;
    }

    public OverlayManager getOverlayManger() {
        return overlayManager;
    }

    FastStyle getFastStyle() {
        return fastStyle;
    }

    void setFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
        notifyListeners(listener -> listener.onFastStyleChanged(fastStyle));
    }

    public void setResourceImpl(ResourceImpl resourceImpl) {
        resourceFetcher.setResourceImpl(resourceImpl);
    }

    public void addListener(FastListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void removeListener(FastListener listener) {
        listeners.remove(listener);
    }

    public void handleOverlayChanged(int key) {
        overlayHandler.handleOverlayChanged(key);
    }

    public void notifyRoomPhaseChanged(RoomPhase phase) {
        roomPhaseHandler.handleRoomPhase(phase);
    }

    public void notifyRoomStateChanged(RoomState roomState) {
        notifyListeners(listener -> listener.onRoomStateChanged(roomState));
    }

    public void notifyRedoUndoChanged(FastRedoUndo count) {
        notifyListeners(listener -> listener.onRedoUndoChanged(count));
    }

    public void notifyRoomReadyChanged(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
        notifyListeners(listener -> listener.onRoomReadyChanged(fastRoom));
    }

    public void notifyReplayReadyChanged(FastReplay fastReplay) {
        this.fastReplay = fastReplay;
        notifyListeners(listener -> listener.onReplayReadyChanged(fastReplay));
    }

    public void notifyFastError(FastException error) {
        errorHandler.handleError(error);
        notifyListeners(listener -> listener.onFastError(error));
    }

    public void notifyOverlayChanged(int key) {
        notifyListeners(listener -> listener.onOverlayChanged(key));
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
