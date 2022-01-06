package io.agora.board.fast.sample;

import android.app.Application;
import android.webkit.WebView;

import io.agora.board.fast.sample.misc.Repository;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.get().init(this);

        WebView.setWebContentsDebuggingEnabled(true);
    }
}
