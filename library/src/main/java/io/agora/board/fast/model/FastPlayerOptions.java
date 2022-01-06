package io.agora.board.fast.model;

import com.herewhite.sdk.domain.PlayerConfiguration;

/**
 * @author fenglibin
 */
public class FastPlayerOptions {
    private final String uuid;
    private final String token;

    private PlayerConfiguration playerConfiguration;

    public FastPlayerOptions(String uuid, String token) {
        this.uuid = uuid;
        this.token = token;
        playerConfiguration = new PlayerConfiguration(uuid, token);
    }

    public PlayerConfiguration getPlayerConfiguration() {
        return playerConfiguration;
    }

    public void setPlayerConfiguration(PlayerConfiguration playerConfiguration) {
        this.playerConfiguration = playerConfiguration;
    }

    public String getUuid() {
        return uuid;
    }

    public String getToken() {
        return token;
    }
}
