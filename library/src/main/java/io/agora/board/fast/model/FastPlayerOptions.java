package io.agora.board.fast.model;

import com.herewhite.sdk.domain.PlayerConfiguration;

/**
 * @author fenglibin
 */
public class FastPlayerOptions {
    private final String uuid;
    private final String token;

    private FastRegion fastRegion = FastRegion.CN_HZ;

    private PlayerConfiguration playerConfiguration;

    public FastPlayerOptions(String uuid, String token) {
        this.uuid = uuid;
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public String getToken() {
        return token;
    }

    public FastRegion getFastRegion() {
        return fastRegion;
    }

    public void setFastRegion(FastRegion fastRegion) {
        this.fastRegion = fastRegion;
    }

    public void setPlayerConfiguration(PlayerConfiguration playerConfiguration) {
        this.playerConfiguration = playerConfiguration;
    }

    public PlayerConfiguration getPlayerConfiguration() {
        return playerConfiguration;
    }
}
