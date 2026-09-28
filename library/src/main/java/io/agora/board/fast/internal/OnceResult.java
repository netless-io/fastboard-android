package io.agora.board.fast.internal;

import io.agora.board.fast.extension.FastResult;

/** Main-thread, exactly-once delivery, including reentrant callbacks. */
public final class OnceResult<T> {
    private FastResult<T> target;
    private boolean completed;
    public OnceResult(FastResult<T> target) { this.target = target; }
    public boolean isCompleted() { return completed; }
    public void finish(T value, Exception error, Runnable beforeCallback) {
        if (completed) return;
        completed = true;
        FastResult<T> callback = target;
        target = null;
        beforeCallback.run();
        if (callback != null) {
            if (error == null) callback.onSuccess(value);
            else callback.onError(error);
        }
    }
}
