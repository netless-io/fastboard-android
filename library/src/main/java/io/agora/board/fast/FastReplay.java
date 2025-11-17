package io.agora.board.fast;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.herewhite.sdk.CommonCallback;
import com.herewhite.sdk.Player;
import com.herewhite.sdk.PlayerListener;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.PlayerPhase;
import com.herewhite.sdk.domain.PlayerState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.SDKError;

import org.json.JSONObject;

import io.agora.board.fast.internal.FastReplayContext;
import io.agora.board.fast.internal.WhiteboardViewManager;
import io.agora.board.fast.model.FastReplayOptions;
import io.agora.board.fast.ui.ErrorHandleLayout;
import io.agora.board.fast.ui.LoadingLayout;
import io.agora.board.fast.ui.OverlayLayout;

public class FastReplay {
    private final FastboardView fastboardView;
    private final FastReplayContext fastReplayContext;
    private final FastReplayOptions fastReplayOptions;
    private final CommonCallback commonCallback = new CommonCallback() {
        @Override
        public void throwError(Object args) {

        }

        @Override
        public void onMessage(JSONObject object) {

        }

        @Override
        public void sdkSetupFail(SDKError error) {
            FastLogger.error("sdk setup fail", error);
            fastReplayContext.notifyFastError(FastException.createSdk(error.getMessage()));
        }

        @Override
        public void onLogger(JSONObject object) {
            FastLogger.info(object.toString());
        }
    };
    private final PlayerListener playerListener = new PlayerListener() {
        @Override
        public void onPhaseChanged(PlayerPhase phase) {

        }

        @Override
        public void onLoadFirstFrame() {

        }

        @Override
        public void onSliceChanged(String slice) {

        }

        @Override
        public void onPlayerStateChanged(PlayerState modifyState) {

        }

        @Override
        public void onStoppedWithError(SDKError error) {

        }

        @Override
        public void onScheduleTimeChanged(long time) {

        }

        @Override
        public void onCatchErrorWhenAppendFrame(SDKError error) {

        }

        @Override
        public void onCatchErrorWhenRender(SDKError error) {

        }
    };
    private final FastReplayListener interFastReplayListener = new FastReplayListener() {
        @Override
        public void onFastError(FastException error) {

        }

        @Override
        public void onReplayReadyChanged(FastReplay fastReplay) {
            if (fastReplay.isReady()) {
                loadingLayout.hide();
            } else {
                loadingLayout.show();
            }
        }
    };
    private LoadingLayout loadingLayout;
    private Player player;
    private OnReplayReadyCallback onReplayReadyCallback;
    private final Promise<Player> playerPromise = new Promise<Player>() {
        @Override
        public void then(Player player) {
            FastReplay.this.player = player;
            fastReplayContext.notifyReplayReadyChanged(FastReplay.this);
            if (onReplayReadyCallback != null) {
                onReplayReadyCallback.onReplayReady(FastReplay.this);
            }
        }

        @Override
        public void catchEx(SDKError t) {
            fastReplayContext.notifyFastError(FastException.createSdk(t.getMessage()));
        }
    };
    private WhiteSdk whiteSdk;

    public FastReplay(FastboardView fastboardView, FastReplayOptions options) {
        this.fastboardView = fastboardView;
        this.fastReplayOptions = options;
        this.fastReplayContext = new FastReplayContext(fastboardView);
        setupView();
        addListener(interFastReplayListener);
    }

    private void setupView() {
        ViewGroup root = fastboardView;

        loadingLayout = root.findViewById(R.id.fast_loading_layout);
        ViewGroup container = root.findViewById(R.id.fast_room_controller);
        ErrorHandleLayout errorHandleLayout = root.findViewById(R.id.fast_error_handle_layout);

        container.setVisibility(View.GONE);
        errorHandleLayout.setVisibility(View.GONE);
    }

    private void initSdkIfNeed(WhiteSdkConfiguration config) {
        if (whiteSdk == null) {
            WhiteboardView whiteboardView = fastboardView.whiteboardView;
            whiteSdk = new WhiteSdk(whiteboardView, fastboardView.getContext(), config, commonCallback);
        }
    }

    public void join() {
        join(null);
    }

    public void join(@Nullable OnReplayReadyCallback onReplayReadyCallback) {
        this.onReplayReadyCallback = onReplayReadyCallback;
        initSdkIfNeed(fastReplayOptions.getSdkConfiguration());
        whiteSdk.createPlayer(fastReplayOptions.getPlayerConfiguration(), playerListener, playerPromise);
    }

    public boolean isReady() {
        return player != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void addListener(FastReplayListener listener) {
        fastReplayContext.addListener(listener);
    }

    public void removeListener(FastReplayListener listener) {
        fastReplayContext.removeListener(listener);
    }

    public void play() {
        if (player != null) {
            player.play();
        }
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void seekTo(long time) {
        if (player != null) {
            player.seekToScheduleTime(time);
        }
    }

    public void destroy() {
        WhiteboardView whiteboardView = fastboardView.whiteboardView;
        fastboardView.removeView(whiteboardView);
        WhiteboardViewManager.get().release(whiteboardView);
    }
}
