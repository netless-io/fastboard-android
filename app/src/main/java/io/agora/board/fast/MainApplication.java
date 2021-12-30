package io.agora.board.fast;

import android.app.Application;

import io.agora.board.fast.misc.Repository;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.get().init(this);
    }
}
