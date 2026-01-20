package io.agora.board.fast.extension;

/**
 * Null Object implementation of FastResult.
 * Used to avoid null checks when callback is optional.
 */
public final class EmptyResult<T> implements FastResult<T> {
    @Override
    public void onSuccess(T value) {
        // No-op: intentionally empty
    }

    @Override
    public void onError(Exception exception) {
        // No-op: intentionally empty
    }
}
