package io.agora.board.fast;

import com.herewhite.sdk.Player;
import com.herewhite.sdk.PlayerListener;
import com.herewhite.sdk.domain.PlayerPhase;
import com.herewhite.sdk.domain.PlayerState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.SDKError;

import io.agora.board.fast.model.FastReplayOptions;

public class FastReplay {
    private final FastContext fastContext;
    private final FastReplayOptions fastReplayOptions;

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
    private Player player;
    private final Promise<Player> playerPromise = new Promise<Player>() {
        @Override
        public void then(Player player) {
            FastReplay.this.player = player;
            fastContext.notifyReplayReadyChanged(FastReplay.this);
        }

        @Override
        public void catchEx(SDKError t) {
            fastContext.notifyFastError(FastException.createSdk(t.getMessage()));
        }
    };

    public FastReplay(FastContext fastContext, FastReplayOptions options) {
        this.fastContext = fastContext;
        this.fastReplayOptions = options;
    }

    public void join() {
        Fastboard fastboard = fastContext.getFastboard();
        fastboard.whiteSdk.createPlayer(fastReplayOptions.getPlayerConfiguration(), playerListener, playerPromise);
    }

    public boolean isReady() {
        return player != null;
    }

    public Player getPlayer() {
        return player;
    }
}
