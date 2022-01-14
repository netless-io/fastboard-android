package io.agora.board.fast;

import com.herewhite.sdk.Player;
import com.herewhite.sdk.PlayerListener;
import com.herewhite.sdk.domain.PlayerConfiguration;
import com.herewhite.sdk.domain.PlayerPhase;
import com.herewhite.sdk.domain.PlayerState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.SDKError;

import io.agora.board.fast.internal.FastConvertor;
import io.agora.board.fast.model.FastPlayerOptions;

public class FastPlayer {

    private final FastSdk fastSdk;
    private final PlayerConfiguration playerConf;
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
            FastPlayer.this.player = player;
        }

        @Override
        public void catchEx(SDKError t) {
            fastSdk.fastContext.notifyFastError(FastException.createSdk(t.getMessage()));
        }
    };

    public FastPlayer(FastSdk sdk, FastPlayerOptions options) {
        this.fastSdk = sdk;
        this.playerConf = FastConvertor.convertPlayerOptions(options);
    }

    public void join() {
        fastSdk.whiteSdk.createPlayer(playerConf, playerListener, playerPromise);
    }

    public Player getPlayer() {
        return player;
    }
}
