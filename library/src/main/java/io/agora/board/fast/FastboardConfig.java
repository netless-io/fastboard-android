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

    // Flag indicating whether auto preloading of WhiteboardViews is enabled.
    private final boolean autoPreload;

    private final boolean enableAssetsHttps;

    // Default value for enablePreload if not explicitly set.
    private static final boolean DEFAULT_ENABLE_PRELOAD = false;

    // Default value for preloadCount if not explicitly set.
    private static final int DEFAULT_PRELOAD_COUNT = 0;

    // Default value for autoPreload if not explicitly set.
    private static final boolean DEFAULT_AUTO_PRELOAD = true;


    private FastboardConfig(Builder builder) {
        this.context = builder.context;
        this.enablePreload = builder.enablePreload;
        this.preloadCount = builder.preloadCount;
        this.autoPreload = builder.autoPreload;
        this.enableAssetsHttps = builder.enableAssetsHttps;
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

    public boolean isAutoPreload() {
        return autoPreload;
    }

    public boolean isEnableAssetsHttps() {
        return enableAssetsHttps;
    }

    public static class Builder {

        private final Context context;

        private boolean enablePreload = DEFAULT_ENABLE_PRELOAD;

        private int preloadCount = DEFAULT_PRELOAD_COUNT;

        private boolean autoPreload = DEFAULT_AUTO_PRELOAD;

        private boolean enableAssetsHttps = false;

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
         * this value. Avoid setting it to more than three to prevent potential memory issues. Mostly, preloadCount is 1.
         *
         * @param preloadCount The preload count.
         * @return The Builder instance.
         */
        public Builder preloadCount(int preloadCount) {
            this.preloadCount = preloadCount;
            return this;
        }

        /**
         * Sets the autoPreload flag. If true, WhiteboardViews are preloaded automatically. If false, the WhiteboardViews
         * will not preload until the preload() method is explicitly called.
         *
         * @param autoPreload autoPreload Determines whether auto preloading is enabled.
         * @return The Builder instance.
         */
        public Builder autoPreload(boolean autoPreload) {
            this.autoPreload = autoPreload;
            return this;
        }

        /**
         * Sets whether to enable HTTPS resource loading for WhiteboardView.
         * By default, WhiteboardView uses the file protocol to load built-in whiteboard resources
         * which is the most stable mode of operation.
         * For special scenarios that require breaking the file protocol restrictions,
         * this method can be used to explicitly enable the HTTPS resource loading scheme based on WebViewAssetLoader.
         *
         * @param enableAssetsHttps
         * @return The Builder instance.
         */
        public Builder enableAssetsHttps(boolean enableAssetsHttps) {
            this.enableAssetsHttps = enableAssetsHttps;
            return this;
        }

        public FastboardConfig build() {
            return new FastboardConfig(this);
        }
    }
}