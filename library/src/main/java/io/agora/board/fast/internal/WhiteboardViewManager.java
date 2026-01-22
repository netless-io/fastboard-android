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
     * Acquires a WhiteboardView from the allocator using the provided context.
     *
     * @param context The activity context to use for the WhiteboardView.
     * @return The obtained WhiteboardView.
     */
    public WhiteboardView obtain(Context context) {
        return allocator.obtain(context);
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
     * Manually attaches a WhiteboardView to a host view by switching its context.
     * <p>
     * Note: This method is now optional since {@link #obtain(Context)} already handles
     * context switching automatically. It is kept for backward compatibility or manual context updates.
     *
     * @param webView The WhiteboardView to attach.
     * @param host    The host view whose context will be used.
     */
    public void attachToHost(WhiteboardView webView, View host) {
        Context ctx = webView.getContext();
        if (ctx instanceof MutableContextWrapper) {
            ((MutableContextWrapper) ctx).setBaseContext(host.getContext());
        }
    }

    /**
     * Creates a WhiteboardView with a MutableContextWrapper to allow context switching.
     */
    private static WhiteboardView createWhiteboardView(Context context, WhiteboardViewOptions options) {
        Context contextWrapper = new MutableContextWrapper(context);
        return new WhiteboardView(contextWrapper, options);
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

        // Choose the appropriate allocator based on the configuration.
        // Note: MutableContextWrapper is created in allocator when obtaining WhiteboardView,
        // to avoid context leaks caused by sharing a single wrapper.
        if (config.isEnablePreload()) {
            allocator = new PreloadAllocator(config.getContext(), config.getPreloadCount(), config.isAutoPreload(), whiteboardViewOptions);
        } else {
            allocator = new DefaultAllocator(config.getContext(), whiteboardViewOptions);
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

        WhiteboardView obtain(Context context);

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
     * Uses the provided activity context directly for creating WhiteboardView.
     */
    static class DefaultAllocator implements WhiteboardViewAllocator {

        private final WhiteboardViewOptions whiteboardViewOptions;

        DefaultAllocator(Context context, WhiteboardViewOptions whiteboardViewOptions) {
            this.whiteboardViewOptions = whiteboardViewOptions;
        }

        @Override
        public WhiteboardView obtain(Context context) {
            // For DefaultAllocator, use the provided context directly (typically activity context)
            return createWhiteboardView(context, whiteboardViewOptions);
        }

        @Override
        public void release(WhiteboardView whiteboardView) {
            whiteboardView.removeAllViews();
            whiteboardView.destroy();
        }
    }

    /**
     * Allocator for preloading WhiteboardView instances with a delay.
     * Preloaded views are created with application context and switched to activity context on obtain.
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
        public WhiteboardView obtain(Context context) {
            WhiteboardView result = preloadViews.poll();

            if (result == null) {
                // No preloaded view available, create one with the provided context directly
                result = createWhiteboardView(context, whiteboardViewOptions);
            } else {
                // Switch the preloaded view's context from application context to the provided context
                Context ctx = result.getContext();
                if (ctx instanceof MutableContextWrapper) {
                    ((MutableContextWrapper) ctx).setBaseContext(context);
                }
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
                preloadViews.offer(createWhiteboardView(context, whiteboardViewOptions));
                preloadWithDelay();
            }
        }

        private void preloadWithDelay() {
            handler.postDelayed(this::preload, PRELOAD_DELAY_MS);
        }
    }
}
