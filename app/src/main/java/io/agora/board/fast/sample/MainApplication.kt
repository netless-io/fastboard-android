package io.agora.board.fast.sample

import android.app.Application
import android.webkit.WebView
import io.agora.board.fast.Fastboard
import io.agora.board.fast.FastboardConfig
import io.agora.board.fast.sample.misc.Repository

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.get().init(this)

        WebView.setWebContentsDebuggingEnabled(true)

        val config = FastboardConfig.Builder(this)
            .enablePreload(true)
            .preloadCount(1)
            .autoPreload(true)
            .enableAssetsHttps(true)
            .build()
        Fastboard.setConfig(config)
    }
}
