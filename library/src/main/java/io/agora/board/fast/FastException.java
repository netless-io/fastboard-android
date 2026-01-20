package io.agora.board.fast;

import androidx.annotation.IntDef;

import com.herewhite.sdk.domain.SDKError;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FastException extends RuntimeException {

    public static final int TYPE_SDK = 0;

    public static final int TYPE_ROOM = 1;

    public static final int TYPE_PLAYER = 2;

    public static final int SDK_ERROR = 100;

    public static final int SDK_SETUP_ERROR = 101;

    public static final int ROOM_JOIN_ERROR = 200;

    public static final int ROOM_DISCONNECT_ERROR = 201;

    public static final int ROOM_KICKED = 202;

    public static final int PLAYER_JOIN_ERROR = 200;

    private int type;

    private int code;

    private FastException(@Type int type, int code, String message) {
        super(message);
        this.type = type;
        this.code = code;
    }

    private FastException(@Type int type, int code, String message, Throwable throwable) {
        super(message, throwable);
        this.type = type;
        this.code = code;
    }

    public static FastException wrap(SDKError error) {
        return new FastException(TYPE_SDK, SDK_ERROR, error.getMessage());
    }

    public static FastException createSdk(String message) {
        return new FastException(TYPE_SDK, SDK_ERROR, message);
    }

    public static FastException createSdk(int code, String message) {
        return new FastException(TYPE_SDK, code, message);
    }

    public static FastException createRoom(int code, String message) {
        return new FastException(TYPE_ROOM, code, message);
    }

    public static FastException createRoom(int code, String message, Throwable throwable) {
        return new FastException(TYPE_ROOM, code, message, throwable);
    }

    public static FastException createPlayer(int code, String message, Throwable throwable) {
        return new FastException(TYPE_PLAYER, code, message, throwable);
    }

    public int getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_SDK, TYPE_ROOM, TYPE_PLAYER})
    public @interface Type {

    }
}
