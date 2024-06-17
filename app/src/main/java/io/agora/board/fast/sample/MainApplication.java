package io.agora.board.fast.sample;

import android.app.Application;
import android.webkit.WebView;

import io.agora.board.fast.Fastboard;
import io.agora.board.fast.FastboardConfig;
import io.agora.board.fast.sample.misc.Repository;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.get().init(this);

        WebView.setWebContentsDebuggingEnabled(true);

        FastboardConfig config = new FastboardConfig.Builder(this)
            .enablePreload(true)
            .preloadCount(1)
            .autoPreload(true)
            .build();
        Fastboard.setConfig(config);
    }
}
