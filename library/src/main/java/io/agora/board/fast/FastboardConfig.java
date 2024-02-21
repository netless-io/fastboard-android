package io.agora.board.fast;

import android.content.Context;

/**
 * Configuration class for Fastboard, providing settings for Fastboard initialization.
 */
public class FastboardConfig {

    // The application context used for initializing Fastboard.
    private final Context context;

    // Flag indicating whether preloading of WhiteboardVies is enabled.
    private final boolean enablePreload;

    // The number of WebViews to preload. May be subject to memory constraints.
    private final int preloadCount;

    // Default value for enablePreload if not explicitly set.
    private static final boolean DEFAULT_ENABLE_PRELOAD = false;

    // Default value for preloadCount if not explicitly set.
    private static final int DEFAULT_PRELOAD_COUNT = 0;

    private FastboardConfig(Builder builder) {
        this.context = builder.context;
        this.enablePreload = builder.enablePreload;
        this.preloadCount = builder.preloadCount;
    }

    /**
     * Get the application context used for initializing Fastboard.
     *
     * @return The application context.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Check if preloading of WebViews is enabled.
     *
     * @return True if preloading is enabled, false otherwise.
     */
    public boolean isEnablePreload() {
        return enablePreload;
    }

    /**
     * Get the number of WebViews to preload. May be subject to memory constraints.
     *
     * @return The preload count.
     */
    public int getPreloadCount() {
        return preloadCount;
    }

    public static class Builder {

        private final Context context;

        private boolean enablePreload = DEFAULT_ENABLE_PRELOAD;

        private int preloadCount = DEFAULT_PRELOAD_COUNT;

        /**
         * Constructor for the Builder class.
         *
         * @param context The application context.
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Enable or disable preloading of WhiteboardViews.
         *
         * @param enablePreload True to enable preloading, false otherwise.
         * @return The Builder instance.
         */
        public Builder enablePreload(boolean enablePreload) {
            this.enablePreload = enablePreload;
            return this;
        }

        /**
         * Set the number of WhiteboardViews to preload. It is important to consider memory constraints when setting
         * this value. Avoid setting it to more than three to prevent potential memory issues.
         *
         * @param preloadCount The preload count.
         * @return The Builder instance.
         */
        public Builder preloadCount(int preloadCount) {
            this.preloadCount = preloadCount;
            return this;
        }

        public FastboardConfig build() {
            return new FastboardConfig(this);
        }
    }
}