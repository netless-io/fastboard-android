package io.agora.board.fast;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FastException extends RuntimeException {

    public static final int TYPE_SDK = 0;
    public static final int TYPE_ROOM = 1;
    public static final int TYPE_PLAYER = 2;
    private int type;

    private FastException(@Type int type, String message) {
        super(message);
        this.type = type;
    }

    private FastException(@Type int type, String message, Throwable throwable) {
        super(message, throwable);
        this.type = type;
    }

    public static FastException createSdk(String message) {
        return new FastException(TYPE_SDK, message);
    }

    public static FastException createRoom(String message, Throwable throwable) {
        return new FastException(TYPE_ROOM, message, throwable);
    }

    public static FastException createPlayer(String message, Throwable throwable) {
        return new FastException(TYPE_PLAYER, message, throwable);
    }

    public int getType() {
        return type;
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_SDK, TYPE_ROOM, TYPE_PLAYER})
    public @interface Type {
    }
}
