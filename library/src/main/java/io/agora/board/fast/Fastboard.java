package io.agora.board.fast;

import androidx.annotation.Nullable;

import com.herewhite.sdk.CommonCallback;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.SDKError;

import org.json.JSONObject;

import io.agora.board.fast.extension.ErrorHandler;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.extension.ResourceImpl;
import io.agora.board.fast.extension.RoomPhaseHandler;
import io.agora.board.fast.model.FastReplayOptions;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;

public class Fastboard {
    public static final String VERSION = "1.0.0-beta.4";

    private final FastContext fastContext;
    WhiteSdk whiteSdk;

    public Fastboard(FastboardView fastboardView) {
        this.fastContext = fastboardView.fastContext;
    }

    private void initSdkIfNeed(WhiteSdkConfiguration config) {
        if (whiteSdk == null) {
            WhiteboardView whiteboardView = fastContext.fastboardView.whiteboardView;
            whiteSdk = new WhiteSdk(whiteboardView, fastContext.context, config, commonCallback);
        }
    }

    public void joinRoom(FastRoomOptions roomOptions) {
        joinRoom(roomOptions, null);
    }

    public void joinRoom(FastRoomOptions roomOptions, @Nullable OnRoomReadyCallback onRoomReadyCallback) {
        initSdkIfNeed(roomOptions.getSdkConfiguration());
        fastContext.joinRoom(roomOptions, onRoomReadyCallback);
    }

    public FastRoom getFastRoom() {
        return fastContext.fastRoom;
    }

    public void joinPlayer(FastReplayOptions options) {
        joinPlayer(options, null);
    }

    public void joinPlayer(FastReplayOptions options, OnReplayReadyCallback onReplayReadyCallback) {
        initSdkIfNeed(options.getSdkConfiguration());
        fastContext.joinPlayer(options, onReplayReadyCallback);
    }

    public FastReplay getFastPlayer() {
        return fastContext.fastReplay;
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

    public void setResourceImpl(ResourceImpl resourceImpl) {
        fastContext.setResourceImpl(resourceImpl);
    }

    public void destroy() {
        FastboardView fastboardView = fastContext.fastboardView;
        WhiteboardView whiteboardView = fastboardView.whiteboardView;
        whiteboardView.removeAllViews();
        whiteboardView.destroy();
    }

    public String getVersion() {
        return VERSION;
    }

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
            FastLogger.info(object.toString());
        }
    };
}
