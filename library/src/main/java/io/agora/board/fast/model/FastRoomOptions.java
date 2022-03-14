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

    private final FastRegion fastRegion;

    private RoomParams roomParams;
    private WhiteSdkConfiguration sdkConfiguration;

    public FastRoomOptions(String appId, String uuid, String token, String uid, FastRegion fastRegion) {
        this(appId, uuid, token, uid, fastRegion, true);
    }

    public FastRoomOptions(String appId, String uuid, String token, String uid, FastRegion fastRegion, boolean writable) {
        this.appId = appId;
        this.uuid = uuid;
        this.token = token;
        this.uid = uid;
        this.fastRegion = fastRegion;
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
