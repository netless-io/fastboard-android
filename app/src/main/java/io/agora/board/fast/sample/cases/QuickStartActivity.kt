package io.agora.board.fast.sample.cases

import android.content.pm.ActivityInfo
import android.os.Bundle
import io.agora.board.fast.FastRoom
import io.agora.board.fast.FastboardView
import io.agora.board.fast.model.FastRegion
import io.agora.board.fast.model.FastRoomOptions
import io.agora.board.fast.sample.Constants
import io.agora.board.fast.sample.R
import io.agora.board.fast.sample.cases.base.BaseActivity
import io.agora.board.fast.sample.cases.helper.UiConfig
import io.agora.board.fast.sample.misc.Repository

class QuickStartActivity : BaseActivity {
    private val repository: Repository = Repository.get()
    private var fastRoom: FastRoom? = null

    constructor() : super()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_start)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        UiConfig.setupSampleCollapseAppliances()
        setupFastboard()
    }

    private fun setupFastboard() {
        // step 1
        val fastboardView = findViewById<FastboardView>(R.id.fastboard_view)
        // step 2: obtain fastboard
        val fastboard = fastboardView.fastboard
        // step 3: join room
        val roomOptions = FastRoomOptions(
            Constants.SAMPLE_APP_ID,
            intent.getStringExtra(Constants.KEY_ROOM_UUID),
            intent.getStringExtra(Constants.KEY_ROOM_TOKEN),
            repository.userId,
            FastRegion.CN_HZ
        )

        fastRoom = fastboard.createFastRoom(roomOptions)

        fastRoom!!.join()
        fastRoom?.room
    }

    override fun onResume() {
        super.onResume()
        setupFullScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fastRoom != null) {
            fastRoom!!.destroy()
        }
    }
}