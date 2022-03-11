package io.agora.board.fast.model;

import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteSdkConfiguration;

import io.agora.board.fast.internal.FastConvertor;

/**
 * @author fenglibin
 */
public class FastRoomOptions {
    private final String appId;
    private final String uuid;
    private final String token;
    private final String uid;
    private final boolean writable;

    private FastRegion fastRegion = FastRegion.CN_HZ;

    private RoomParams roomParams;
    private WhiteSdkConfiguration sdkConfiguration;

    public FastRoomOptions(String appId, String uuid, String token, String uid) {
        this(appId, uuid, token, uid, true);
    }

    public FastRoomOptions(String appId, String uuid, String token, String uid, boolean writable) {
        this.appId = appId;
        this.uuid = uuid;
        this.token = token;
        this.uid = uid;
        this.writable = writable;
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

    public String getUid() {
        return uid;
    }

    public boolean isWritable() {
        return writable;
    }

    public FastRegion getFastRegion() {
        return fastRegion;
    }

    public void setFastRegion(FastRegion fastRegion) {
        this.fastRegion = fastRegion;
    }

    public RoomParams getRoomParams() {
        if (roomParams != null) {
            return roomParams;
        }
        return FastConvertor.convertRoomOptions(this);
    }

    public void setRoomParams(RoomParams roomParams) {
        this.roomParams = roomParams;
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
