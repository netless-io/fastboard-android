package io.agora.board.fast;

import com.herewhite.sdk.CommonCallback;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.SDKError;

import org.json.JSONObject;

import io.agora.board.fast.extension.ErrorHandler;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.extension.RoomPhaseHandler;
import io.agora.board.fast.model.FastPlayerOptions;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class FastSdk {
    final FastContext fastContext;
    private final CommonCallback commonCallback = new CommonCallback() {
        @Override
        public void throwError(Object args) {

        }

        @Override
        public void onMessage(JSONObject object) {

        }

        @Override
        public void sdkSetupFail(SDKError error) {
            fastContext.notifyFastError(FastException.createSdk(error.getMessage()));
        }

        @Override
        public void onLogger(JSONObject object) {

        }
    };
    WhiteSdk whiteSdk;
    FastRoom fastRoom;
    FastPlayer fastPlayer;

    public FastSdk(FastboardView fastboardView) {
        this.fastContext = fastboardView.fastContext;
    }

    void initSdk(FastSdkOptions options) {
        WhiteSdkConfiguration config = options.getConfiguration();
        config.setUseMultiViews(true);

        FastboardView fastboardView = fastContext.fastboardView;
        whiteSdk = new WhiteSdk(fastboardView.whiteboardView, fastboardView.getContext(), config, commonCallback);
    }

    public FastRoom joinRoom(FastRoomOptions options) {
        if (whiteSdk == null) {
            throw FastException.createSdk("sdk not init, call initSdk first");
        }
        fastRoom = fastContext.joinRoom(options);
        return fastRoom;
    }

    public FastRoom getFastRoom() {
        return fastRoom;
    }

    public FastPlayer joinPlayer(FastPlayerOptions options) {
        if (whiteSdk == null) {
            throw FastException.createSdk("sdk not init, call initSdk first");
        }
        fastPlayer = fastContext.joinPlayer(options);
        return fastPlayer;
    }

    public FastPlayer getFastPlayer() {
        return fastPlayer;
    }

    public FastboardView getFastboardView() {
        return fastContext.getFastboardView();
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        fastContext.setErrorHandler(errorHandler);
    }

    public void setRoomPhaseHandler(RoomPhaseHandler roomPhaseHandler) {
        fastContext.setRoomPhaseHandler(roomPhaseHandler);
    }

    public void setOverlayHandler(OverlayHandler overlayHandler) {
        fastContext.setOverlayHandler(overlayHandler);
    }

    public void setOverlayManager(OverlayManager overlayManager) {
        fastContext.setOverlayManager(overlayManager);
    }

    public OverlayManager getOverlayManger() {
        return fastContext.getOverlayManger();
    }

    public void addListener(FastListener listener) {
        fastContext.addListener(listener);
    }

    public void removeListener(FastListener listener) {
        fastContext.removeListener(listener);
    }

    /**
     * @return return a copy of current fastStyle
     */
    public FastStyle getFastStyle() {
        return fastContext.getFastStyle().copy();
    }

    public void setFastStyle(FastStyle style) {
        fastContext.setFastStyle(style);
    }

    public void destroy() {
        FastboardView fastboardView = fastContext.fastboardView;
        WhiteboardView whiteboardView = fastboardView.whiteboardView;
        whiteboardView.removeAllViews();
        whiteboardView.destroy();
    }
}
