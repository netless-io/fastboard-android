package io.agora.board.fast.internal;

import android.content.Context;

import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;

import java.util.concurrent.CopyOnWriteArrayList;

import io.agora.board.fast.FastException;
import io.agora.board.fast.FastRoom;
import io.agora.board.fast.FastRoomListener;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.extension.ErrorHandler;
import io.agora.board.fast.extension.FastResource;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.extension.RoomPhaseHandler;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.ResourceFetcher;

public class FastRoomContext {
    final FastboardView fastboardView;
    Context context;
    FastRoom fastRoom;
    FastStyle fastStyle;

    ErrorHandler errorHandler;
    RoomPhaseHandler roomPhaseHandler;
    OverlayHandler overlayHandler;
    OverlayManager overlayManager;
    ResourceFetcher resourceFetcher;
    CopyOnWriteArrayList<FastRoomListener> listeners = new CopyOnWriteArrayList<>();

    public FastRoomContext(FastboardView fastboardView) {
        this.fastboardView = fastboardView;
        this.context = fastboardView.getContext();
        this.fastStyle = fastboardView.getFastboard().getFastStyle();
        this.resourceFetcher = ResourceFetcher.get();
    }

    public FastboardView getFastboardView() {
        return fastboardView;
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

    public FastStyle getFastStyle() {
        return fastStyle;
    }

    public void setFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
        notifyListeners(listener -> listener.onFastStyleChanged(fastStyle));
    }

    public void setResource(FastResource resource) {
        resourceFetcher.setResource(resource);
    }

    public void addListener(FastRoomListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void removeListener(FastRoomListener listener) {
        listeners.remove(listener);
    }

    public void notifyRoomPhaseChanged(RoomPhase phase) {
        roomPhaseHandler.handleRoomPhase(phase);
        notifyListeners(listener -> listener.onRoomPhaseChanged(phase));
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

    public void notifyFastError(FastException error) {
        errorHandler.handleError(error);
        notifyListeners(listener -> listener.onFastError(error));
    }

    public void notifyOverlayChanged(int key) {
        notifyListeners(listener -> listener.onOverlayChanged(key));
    }

    private void notifyListeners(ListenerInvocation listenerInvocation) {
        CopyOnWriteArrayList<FastRoomListener> listenerSnapshot = new CopyOnWriteArrayList<>(listeners);
        for (FastRoomListener listener : listenerSnapshot) {
            listenerInvocation.invokeListener(listener);
        }
    }

    protected interface ListenerInvocation {
        void invokeListener(FastRoomListener listener);
    }
}
