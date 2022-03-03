package io.agora.board.fast.extension;

public interface FastResult<T> {
    void onSuccess(T value);

    void onError(Exception exception);
}
