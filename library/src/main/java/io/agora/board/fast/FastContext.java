package io.agora.board.fast;

import android.content.Context;

import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.SceneState;

import java.util.concurrent.CopyOnWriteArrayList;

import io.agora.board.fast.extension.ErrorHandler;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.extension.ResourceImpl;
import io.agora.board.fast.extension.RoomPhaseHandler;
import io.agora.board.fast.internal.FastErrorHandler;
import io.agora.board.fast.internal.FastOverlayHandler;
import io.agora.board.fast.internal.Util;
import io.agora.board.fast.model.FastPlayerOptions;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.ResourceFetcher;

public class FastContext {
    final FastboardView fastboardView;
    Context context;
    FastSdk fastSdk;
    FastRoom fastRoom;
    FastPlayer fastPlayer;
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
        this.errorHandler = new FastErrorHandler(Util.getActivity(context));
        this.overlayHandler = new FastOverlayHandler(this);
    }

    public FastboardView getFastboardView() {
        return fastboardView;
    }

    public FastSdk getFastSdk(FastSdkOptions options) {
        if (fastSdk == null) {
            fastSdk = new FastSdk(fastboardView);
            fastSdk.initSdk(options);
            notifyFastSdkCreated(fastSdk);
        }
        return fastSdk;
    }

    public FastRoom joinRoom(FastRoomOptions options) {
        fastRoom = new FastRoom(fastSdk, options);
        fastRoom.join();
        notifyFastRoomCreated(fastRoom);
        return fastRoom;
    }

    public FastPlayer joinPlayer(FastPlayerOptions options) {
        fastPlayer = new FastPlayer(fastSdk, options);
        fastPlayer.join();
        notifyFastPlayerCreated(fastPlayer);
        return fastPlayer;
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

    void setFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
        notifyListeners(listener -> listener.onFastStyleChanged(fastStyle));
    }

    FastStyle getFastStyle() {
        return fastStyle;
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

    public void notifyBroadcastStateChanged(BroadcastState broadcastState) {
        notifyListeners(listener -> listener.onBroadcastStateChanged(broadcastState));
    }

    public void notifyMemberStateChanged(MemberState memberState) {
        notifyListeners(listener -> listener.onMemberStateChanged(memberState));
    }

    public void notifySceneStateChanged(SceneState sceneState) {
        notifyListeners(listener -> listener.onSceneStateChanged(sceneState));
    }

    public void notifyWindowBoxStateChanged(String windowBoxState) {
        notifyListeners(listener -> listener.onWindowBoxStateChanged(windowBoxState));
    }

    public void notifyRedoUndoChanged(FastRedoUndo count) {
        notifyListeners(listener -> listener.onRedoUndoChanged(count));
    }

    public void notifyFastRoomCreated(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
        notifyListeners(listener -> listener.onFastRoomCreated(fastRoom));
    }

    public void notifyFastPlayerCreated(FastPlayer fastPlayer) {
        this.fastPlayer = fastPlayer;
        notifyListeners(listener -> listener.onFastPlayerCreated(fastPlayer));
    }

    public void notifyFastSdkCreated(FastSdk fastSdk) {
        this.fastSdk = fastSdk;
        notifyListeners(listener -> listener.onFastSdkCreated(fastSdk));
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
