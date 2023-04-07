package io.agora.board.fast.model;

public class FastUserPayload {
    /**
     * name display on cursor
     */
    private final String nickName;
    /**
     * avatar display on cursor
     */
    private final String avatar;

    public FastUserPayload(String nickName) {
        this(nickName, null);
    }

    public FastUserPayload(String nickName, String avatar) {
        this.nickName = nickName;
        this.avatar = avatar;
    }
}
