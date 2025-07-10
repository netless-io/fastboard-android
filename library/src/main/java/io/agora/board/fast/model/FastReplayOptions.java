package io.agora.board.fast.model;

import androidx.annotation.Nullable;

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

    private Float containerSizeRatio;

    @Nullable
    private Long duration;

    @Nullable
    private Long beginTimestamp;

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

    public Float getContainerSizeRatio() {
        return containerSizeRatio;
    }

    public void setContainerSizeRatio(Float ratio) {
        this.containerSizeRatio = ratio;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getBeginTimestamp() {
        return beginTimestamp;
    }

    public void setBeginTimestamp(Long beginTimestamp) {
        this.beginTimestamp = beginTimestamp;
    }
}
