package io.agora.board.fast.model;

public enum FastWindowBoxState {
    Maximized("maximized"),
    Minimized("minimized"),
    Normal("normal"),
    ;

    private final String windowBoxState;

    FastWindowBoxState(String windowBoxState) {
        this.windowBoxState = windowBoxState;
    }

    public String value() {
        return windowBoxState;
    }

    /**
     * @param windowBoxState
     * @return FastWindowBoxState
     * @see com.herewhite.sdk.domain.RoomState
     */
    public static FastWindowBoxState of(String windowBoxState) {
        for (FastWindowBoxState item : FastWindowBoxState.values()) {
            if (item.windowBoxState.equals(windowBoxState)) {
                return item;
            }
        }
        return Normal;
    }
}
