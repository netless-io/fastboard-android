package io.agora.board.fast.model;

import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.domain.PlayerConfiguration;

import io.agora.board.fast.internal.FastConvertor;

/**
 * @author fenglibin
 */
public class FastReplayOptions {
    private final String appId;
    private final String uuid;
    private final String token;

    private FastRegion fastRegion = FastRegion.CN_HZ;

    private PlayerConfiguration playerConfiguration;

    private WhiteSdkConfiguration sdkConfiguration;

    public FastReplayOptions(String appId, String uuid, String token) {
        this.appId = appId;
        this.uuid = uuid;
        this.token = token;
    }

    public String getAppId() {
        return appId;
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

    public PlayerConfiguration getPlayerConfiguration() {
        if (playerConfiguration != null) {
            return playerConfiguration;
        }
        return FastConvertor.convertReplayOptions(this);
    }

    public void setPlayerConfiguration(PlayerConfiguration playerConfiguration) {
        this.playerConfiguration = playerConfiguration;
    }

    public WhiteSdkConfiguration getSdkConfiguration() {
        if (sdkConfiguration != null) {
            return sdkConfiguration;
        }
        return FastConvertor.convertSdkOptions(this);
    }

    public void setSdkConfiguration(WhiteSdkConfiguration sdkConfiguration) {
        this.sdkConfiguration = sdkConfiguration;
    }
}
