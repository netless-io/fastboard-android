package io.agora.board.fast;

import android.os.Handler;
import android.os.Looper;
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
            FastLogger.error("replay sdk setup failed");
            onMain(() -> handleSdkSetupFailure(error));
        }

        @Override
        public void onLogger(JSONObject object) {
            // Raw SDK log payloads can contain room credentials. Applications may
            // attach their own redacted logging at the SDK layer when needed.
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
    private volatile Player player;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean destroyed;
    private boolean setupFailed;
    private boolean createPending;
    private boolean cancelPendingCreate;
    private boolean restartAfterPending;
    private long generation;
    private OnReplayReadyCallback pendingReadyCallback;
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

    private void onMain(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) action.run();
        else mainHandler.post(action);
    }

    private static void requireMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("FastReplay must be used on the main thread");
        }
    }

    public void join() {
        join(null);
    }

    public void join(@Nullable OnReplayReadyCallback onReplayReadyCallback) {
        requireMainThread();
        if (destroyed || setupFailed) return;
        if (player != null) {
            if (onReplayReadyCallback != null) onReplayReadyCallback.onReplayReady(this);
            return;
        }
        pendingReadyCallback = onReplayReadyCallback;
        if (createPending) {
            if (cancelPendingCreate) restartAfterPending = true;
            return;
        }
        startCreatePlayer();
    }

    private void startCreatePlayer() {
        initSdkIfNeed(fastReplayOptions.getSdkConfiguration());
        createPending = true;
        cancelPendingCreate = false;
        restartAfterPending = false;
        final long requestGeneration = ++generation;
        whiteSdk.createPlayer(fastReplayOptions.getPlayerConfiguration(), playerListener, new Promise<Player>() {
            @Override public void then(Player value) {
                onMain(() -> handlePlayerCreated(requestGeneration, value));
            }

            @Override public void catchEx(SDKError error) {
                onMain(() -> handlePlayerCreateError(requestGeneration, error));
            }
        });
    }

    private void handlePlayerCreated(long requestGeneration, Player value) {
        requireMainThread();
        if (destroyed) return;
        if (requestGeneration != generation || cancelPendingCreate) {
            createPending = false;
            cancelPendingCreate = false;
            value.stop();
            if (whiteSdk != null) whiteSdk.releasePlayer();
            restartCreateIfRequested();
            return;
        }
        createPending = false;
        player = value;
        fastReplayContext.notifyReplayReadyChanged(this);
        if (destroyed || generation != requestGeneration || player != value) return;
        OnReplayReadyCallback callback = pendingReadyCallback;
        pendingReadyCallback = null;
        if (callback != null) callback.onReplayReady(this);
    }

    private void handlePlayerCreateError(long requestGeneration, SDKError error) {
        requireMainThread();
        if (destroyed) return;
        if (requestGeneration != generation || cancelPendingCreate) {
            createPending = false;
            cancelPendingCreate = false;
            if (whiteSdk != null) whiteSdk.releasePlayer();
            restartCreateIfRequested();
            return;
        }
        createPending = false;
        pendingReadyCallback = null;
        fastReplayContext.notifyFastError(FastException.createSdk(error.getMessage()));
    }

    private void restartCreateIfRequested() {
        boolean shouldRestart = restartAfterPending;
        restartAfterPending = false;
        if (shouldRestart && !destroyed) startCreatePlayer();
        else pendingReadyCallback = null;
    }

    private void handleSdkSetupFailure(SDKError error) {
        requireMainThread();
        if (destroyed || setupFailed) return;
        // A setup failure invalidates the shared WebView bridge. This instance
        // cannot safely create another player while an older create callback may
        // still arrive and release the bridge's current player.
        setupFailed = true;
        generation++;
        createPending = false;
        cancelPendingCreate = false;
        restartAfterPending = false;
        pendingReadyCallback = null;
        boolean wasReady = player != null;
        player = null;
        if (whiteSdk != null) whiteSdk.releasePlayer();
        if (wasReady) fastReplayContext.notifyReplayReadyChanged(this);
        if (!destroyed) fastReplayContext.notifyFastError(FastException.createSdk(error.getMessage()));
    }

    public boolean isReady() {
        return !destroyed && player != null;
    }

    public Player getPlayer() {
        return destroyed ? null : player;
    }

    public void addListener(FastReplayListener listener) {
        fastReplayContext.addListener(listener);
    }

    public void removeListener(FastReplayListener listener) {
        fastReplayContext.removeListener(listener);
    }

    public void play() {
        requireMainThread();
        Player current = player;
        if (!destroyed && current != null) current.play();
    }

    public void pause() {
        requireMainThread();
        Player current = player;
        if (!destroyed && current != null) current.pause();
    }

    public void stop() {
        requireMainThread();
        if (destroyed) return;
        generation++;
        pendingReadyCallback = null;
        restartAfterPending = false;
        if (createPending) cancelPendingCreate = true;
        Player current = player;
        player = null;
        if (current != null) current.stop();
        if (whiteSdk != null) whiteSdk.releasePlayer();
        if (current != null) fastReplayContext.notifyReplayReadyChanged(this);
    }

    public void seekTo(long time) {
        requireMainThread();
        Player current = player;
        if (!destroyed && current != null) current.seekToScheduleTime(time);
    }

    public void setPlaybackSpeed(double playbackSpeed) {
        requireMainThread();
        Player current = player;
        if (!destroyed && current != null) current.setPlaybackSpeed(playbackSpeed);
    }

    public void destroy() {
        requireMainThread();
        if (destroyed) return;
        destroyed = true;
        generation++;
        cancelPendingCreate = true;
        restartAfterPending = false;
        pendingReadyCallback = null;
        Player current = player;
        player = null;
        if (current != null) current.stop();
        if (whiteSdk != null) whiteSdk.releasePlayer();
        fastReplayContext.clearListeners();
        WhiteboardView whiteboardView = fastboardView.whiteboardView;
        if (whiteboardView.getParent() == fastboardView) fastboardView.removeView(whiteboardView);
        WhiteboardViewManager.get().release(whiteboardView);
    }
}
