package io.agora.board.fast;

import com.herewhite.sdk.CommonCallback;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.SDKError;

import org.json.JSONObject;

import io.agora.board.fast.internal.FastErrorHandler;
import io.agora.board.fast.model.FastPlayerOptions;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class FastSdk {
    final FastboardView fastboardView;
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
            FastErrorHandler errorHandler = fastContext.errorHandler;
            errorHandler.onJoinRoomError(new FastException(error.getMessage(), error));
        }

        @Override
        public void onLogger(JSONObject object) {

        }
    };
    WhiteSdk whiteSdk;
    FastRoom fastRoom;
    FastPlayer fastPlayer;

    public FastSdk(FastboardView fastBoardView) {
        this.fastboardView = fastBoardView;
        this.fastContext = fastBoardView.fastContext;
    }

    void initSdk(FastSdkOptions options) {
        WhiteSdkConfiguration conf = options.getConfiguration();
        whiteSdk = new WhiteSdk(fastboardView.whiteboardView, fastboardView.getContext(), conf, commonCallback);
    }

    public FastRoom joinRoom(FastRoomOptions options) {
        if (whiteSdk == null) {
            throw new FastException("sdk not init, call initSdk first");
        }
        fastRoom = new FastRoom(this, options);
        fastRoom.join();
        fastContext.notifyFastRoomCreated(fastRoom);
        return fastRoom;
    }

    public FastRoom getFastRoom() {
        return fastRoom;
    }

    public FastPlayer joinPlayer(FastPlayerOptions options) {
        if (whiteSdk == null) {
            throw new FastException("sdk not init, call initSdk first");
        }
        fastPlayer = new FastPlayer(this, options);
        fastPlayer.join();
        return fastPlayer;
    }

    public void destroy() {
        WhiteboardView whiteboardView = fastboardView.whiteboardView;
        whiteboardView.removeAllViews();
        whiteboardView.destroy();
    }

    public void setErrorHandler(FastErrorHandler errorHandler) {
        if (errorHandler == null) {
            throw new FastException("The errorHandler is null.");
        }
        fastContext.errorHandler = errorHandler;
    }

    public void registerObserver(BoardStateObserver observer) {
        fastContext.registerObserver(observer);
    }

    public void unregisterObserver(BoardStateObserver observer) {
        fastContext.unregisterObserver(observer);
    }

    /**
     * @return return a copy of current fastStyle
     */
    public FastStyle getFastStyle() {
        return fastContext.getFastStyle().copy();
    }

    public void setFastStyle(FastStyle style) {
        fastContext.updateFastStyle(style);
    }

    public FastContext getFastContext() {
        return fastContext;
    }
}
