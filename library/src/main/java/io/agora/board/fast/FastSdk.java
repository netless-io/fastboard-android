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

    WhiteSdk whiteSdk;
    FastRoom fastRoom;
    FastPlayer fastPlayer;

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

    public FastSdk(FastboardView fastboardView) {
        this.fastboardView = fastboardView;
        this.fastContext = fastboardView.fastContext;
    }

    void initSdk(FastSdkOptions options) {
        WhiteSdkConfiguration conf = options.getConfiguration();
        whiteSdk = new WhiteSdk(fastboardView.whiteboardView, fastboardView.getContext(), conf, commonCallback);

        if (options.isUseErrorHandler()) {
            fastContext.enableDefaultErrorHandler();
        }
    }

    public FastRoom joinRoom(FastRoomOptions options) {
        if (whiteSdk == null) {
            throw FastException.createSdk("sdk not init, call initSdk first");
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
            throw FastException.createSdk("sdk not init, call initSdk first");
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
        fastContext.updateFastStyle(style);
    }

    public FastContext getFastContext() {
        return fastContext;
    }
}
