package io.agora.board.fast.sample.cases

import android.content.pm.ActivityInfo
import android.os.Bundle
import io.agora.board.fast.FastReplay
import io.agora.board.fast.FastboardView
import io.agora.board.fast.model.FastReplayOptions
import io.agora.board.fast.sample.Constants
import io.agora.board.fast.sample.R
import io.agora.board.fast.sample.cases.base.BaseActivity

class QuickReplayActivity : BaseActivity() {
    private var fastReplay: FastReplay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_start)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setupFastboard()
    }

    private fun setupFastboard() {
        // step 1
        val fastboardView = findViewById<FastboardView>(R.id.fastboard_view)
        // step 2: obtain fastboard
        val fastboard = fastboardView.fastboard
        // step 3: join room
        val replayOptions = FastReplayOptions(
            Constants.SAMPLE_APP_ID,
            intent.getStringExtra(Constants.KEY_ROOM_UUID),
            intent.getStringExtra(Constants.KEY_ROOM_TOKEN),
        ).apply {
            duration = 120_000 // 2 minutes
        }

        fastReplay = fastboard.createFastReplay(replayOptions)
        fastReplay!!.join {
            fastReplay?.play()
            fastReplay?.seekTo(20_000)
        }
    }

    override fun onResume() {
        super.onResume()
        setupFullScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        fastReplay?.destroy()
    }
}