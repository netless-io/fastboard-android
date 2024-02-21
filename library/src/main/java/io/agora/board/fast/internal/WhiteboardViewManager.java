package io.agora.board.fast.internal;

import android.content.Context;
import android.os.Handler;
import com.herewhite.sdk.WhiteboardView;
import io.agora.board.fast.FastboardConfig;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Manages the lifecycle of WhiteboardView instances based on the provided configuration.
 */
public class WhiteboardViewManager {

    private boolean initialized;

    private WhiteboardViewAllocator allocator;

    private WhiteboardViewManager() {
    }

    /**
     * Gets the singleton instance of WhiteboardViewManager in a thread-safe manner.
     */
    public static synchronized WhiteboardViewManager get() {
        return Holder.instance;
    }

    /**
     * Acquires a WhiteboardView from the allocator.
     *
     * @return The obtained WhiteboardView.
     */
    public WhiteboardView obtain() {
        return allocator.obtain();
    }

    /**
     * Releases a WhiteboardView back to the allocator.
     *
     * @param whiteboardView The WhiteboardView to release.
     */
    public void release(WhiteboardView whiteboardView) {
        allocator.release(whiteboardView);
    }

    /**
     * Initializes the WhiteboardViewManager with the provided configuration. This method should be called before any
     * other methods.
     *
     * @param config The configuration for the WhiteboardViewManager.
     */
    public void init(FastboardConfig config) {
        if (initialized) {
            return;
        }
        initialized = true;

        // Choose the appropriate allocator based on the configuration.
        if (config.isEnablePreload()) {
            allocator = new PreloadAllocator(config.getContext(), config.getPreloadCount());
        } else {
            allocator = new DefaultAllocator(config.getContext());
        }
    }

    /**
     * Interface for allocating and releasing WhiteboardView instances.
     */
    interface WhiteboardViewAllocator {

        WhiteboardView obtain();

        void release(WhiteboardView whiteboardView);
    }

    /**
     * Holder class for lazy initialization of the singleton instance.
     */
    private static class Holder {

        private static final WhiteboardViewManager instance = new WhiteboardViewManager();
    }

    /**
     * Allocator for WhiteboardView instances without preloading.
     */
    static class DefaultAllocator implements WhiteboardViewAllocator {

        private final Context context;

        DefaultAllocator(Context context) {
            this.context = context;
        }

        @Override
        public WhiteboardView obtain() {
            return new WhiteboardView(context);
        }

        @Override
        public void release(WhiteboardView whiteboardView) {
            whiteboardView.removeAllViews();
            whiteboardView.destroy();
        }
    }

    /**
     * Allocator for preloading WhiteboardView instances with a delay.
     */
    static class PreloadAllocator implements WhiteboardViewAllocator {

        private static final int PRELOAD_DELAY_MS = 100;

        private final Context context;

        private final Handler handler;

        private final int count;

        private final LinkedBlockingQueue<WhiteboardView> preloadViews;

        PreloadAllocator(Context context, int count) {
            this.context = context;
            this.handler = new Handler(context.getMainLooper());
            this.count = count;
            this.preloadViews = new LinkedBlockingQueue<>(count);

            preload();
        }

        @Override
        public WhiteboardView obtain() {
            WhiteboardView result = preloadViews.poll();

            if (result == null) {
                result = new WhiteboardView(context);
            }

            preload();

            return result;
        }

        @Override
        public void release(WhiteboardView whiteboardView) {
            whiteboardView.removeAllViews();
            whiteboardView.destroy();
        }

        private void preload() {
            handler.postDelayed(() -> {
                WhiteboardView whiteboardView = new WhiteboardView(getContext());
                preloadViews.offer(whiteboardView);
                if (preloadViews.size() < count) {
                    preload();
                }
            }, PRELOAD_DELAY_MS);
        }

        private Context getContext() {
            return context;
        }
    }
}
