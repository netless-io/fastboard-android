package io.agora.board.fast.model;

import com.herewhite.sdk.RoomParams;

/**
 * @author fenglibin
 */
public class FastRoomOptions {
    private final String uuid;
    private final String token;
    private final String uid;
    private final boolean writable;

    private RoomParams roomParams;

    public FastRoomOptions(String uuid, String token, String uid) {
        this(uuid, token, uid, true);
    }

    public FastRoomOptions(String uuid, String token, String uid, boolean writable) {
        this.uuid = uuid;
        this.token = token;
        this.uid = uid;
        this.writable = writable;
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

    public RoomParams getRoomParams() {
        return roomParams;
    }

    public void setRoomParams(RoomParams roomParams) {
        this.roomParams = roomParams;
    }
}
