package io.agora.board.fast.internal;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Handler;
import android.view.View;

import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.WhiteboardViewOptions;

import java.util.concurrent.LinkedBlockingQueue;

import io.agora.board.fast.FastboardConfig;

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

    public void attachToHost(WhiteboardView webView, View host) {
        Context ctx = webView.getContext();
        if (ctx instanceof MutableContextWrapper) {
            ((MutableContextWrapper) ctx).setBaseContext(host.getContext());
        }
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

        WhiteboardViewOptions whiteboardViewOptions = null;
        if (config.isEnableAssetsHttps()) {
            whiteboardViewOptions = new WhiteboardViewOptions().setEnableAssetsHttps(true);
        }

        // Wrap the context to allow for future modifications if needed.
        Context contextWrapper = new MutableContextWrapper(config.getContext());
        // Choose the appropriate allocator based on the configuration.
        if (config.isEnablePreload()) {
            allocator = new PreloadAllocator(contextWrapper, config.getPreloadCount(), config.isAutoPreload(), whiteboardViewOptions);
        } else {
            allocator = new DefaultAllocator(contextWrapper, whiteboardViewOptions);
        }
    }

    public void preload() {
        if (allocator instanceof PreloadAllocator) {
            ((PreloadAllocator) allocator).preload();
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

        private final WhiteboardViewOptions whiteboardViewOptions;

        DefaultAllocator(Context context, WhiteboardViewOptions whiteboardViewOptions) {
            this.context = context;
            this.whiteboardViewOptions = whiteboardViewOptions;
        }

        @Override
        public WhiteboardView obtain() {
            return new WhiteboardView(context, whiteboardViewOptions);
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

        private final WhiteboardViewOptions whiteboardViewOptions;

        PreloadAllocator(Context context, int count, boolean autoPreload, WhiteboardViewOptions whiteboardViewOptions) {
            this.context = context;
            this.handler = new Handler(context.getMainLooper());
            this.count = count;
            this.preloadViews = new LinkedBlockingQueue<>(count);
            this.whiteboardViewOptions = whiteboardViewOptions;

            if (autoPreload) {
                preload();
            }
        }

        @Override
        public WhiteboardView obtain() {
            WhiteboardView result = preloadViews.poll();

            if (result == null) {
                result = new WhiteboardView(context, whiteboardViewOptions);
            }

            if (preloadViews.size() < count) {
                preloadWithDelay();
            }

            return result;
        }

        @Override
        public void release(WhiteboardView whiteboardView) {
            whiteboardView.removeAllViews();
            whiteboardView.destroy();
        }

        public void preload() {
            if (preloadViews.size() < count) {
                preloadViews.offer(new WhiteboardView(context, whiteboardViewOptions));
                preloadWithDelay();
            }
        }

        private void preloadWithDelay() {
            handler.postDelayed(this::preload, PRELOAD_DELAY_MS);
        }
    }
}
