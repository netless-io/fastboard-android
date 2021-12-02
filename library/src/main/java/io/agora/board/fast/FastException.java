package io.agora.board.fast;

public class FastException extends RuntimeException {
    public FastException() {
        super();
    }

    public FastException(String message) {
        super(message);
    }

    public FastException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastException(Throwable cause) {
        super(cause);
    }
}
