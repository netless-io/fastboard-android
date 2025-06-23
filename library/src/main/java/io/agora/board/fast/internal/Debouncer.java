package io.agora.board.fast.internal;

import android.os.Handler;
import android.os.Looper;

/**
 * Debouncer class to delay the execution of a Runnable action.
 * It ensures that only the last action is executed after a specified delay.
 */
public class Debouncer {

    private final long delayMillis;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pendingRunnable;

    public Debouncer(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * 调用时传入新的任务，延迟执行，仅执行最后一次
     */
    public void apply(Runnable action) {
        if (pendingRunnable != null) {
            handler.removeCallbacks(pendingRunnable);
        }

        pendingRunnable = () -> {
            action.run();
            pendingRunnable = null;
        };

        handler.postDelayed(pendingRunnable, delayMillis);
    }

    /**
     * 可选：取消当前挂起任务
     */
    public void cancel() {
        if (pendingRunnable != null) {
            handler.removeCallbacks(pendingRunnable);
            pendingRunnable = null;
        }
    }
}