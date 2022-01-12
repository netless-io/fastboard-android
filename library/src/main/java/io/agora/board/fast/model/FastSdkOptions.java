package io.agora.board.fast.model;

import com.herewhite.sdk.WhiteSdkConfiguration;

/**
 * @author fenglibin
 */
public class FastSdkOptions {
    private final String appId;
    private WhiteSdkConfiguration configuration;

    public FastSdkOptions(String appId) {
        this.appId = appId;

        configuration = new WhiteSdkConfiguration(appId);
    }

    public String getAppId() {
        return appId;
    }

    public WhiteSdkConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(WhiteSdkConfiguration configuration) {
        this.configuration = configuration;
    }
}
