package io.agora.board.fast.sample.cases

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.os.MessageQueue
import android.view.Gravity
import android.widget.Button
import io.agora.board.fast.FastLogger
import io.agora.board.fast.FastRoom
import io.agora.board.fast.FastRoomListener
import io.agora.board.fast.Fastboard
import io.agora.board.fast.FastboardView
import io.agora.board.fast.extension.FastResource
import io.agora.board.fast.model.FastRegion
import io.agora.board.fast.model.FastRoomOptions
import io.agora.board.fast.sample.Constants
import io.agora.board.fast.sample.R
import io.agora.board.fast.sample.cases.base.BaseActivity
import io.agora.board.fast.sample.cases.helper.RoomOperationsKT
import io.agora.board.fast.sample.cases.helper.UiConfig
import io.agora.board.fast.sample.misc.Repository
import io.agora.board.fast.sample.misc.Utils

/**
 * @author fenglibin
 */
class RoomActivity : BaseActivity() {
    private val repository: Repository = Repository.get()

    private var fastRoom: FastRoom? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setupWindowInsets()

        UiConfig.setupSampleCollapseAppliances()
        setupFastboard()
        preloadWebViewOnIdle()
    }

    private fun setupFastboard() {
        val fastboardView = findViewById<FastboardView>(R.id.fastboard_view)
        // create fastSdk
        val fastboard = fastboardView.getFastboard()

        // join room
        val roomOptions = FastRoomOptions(
            Constants.SAMPLE_APP_ID,
            intent.getStringExtra(Constants.KEY_ROOM_UUID),
            intent.getStringExtra(Constants.KEY_ROOM_TOKEN),
            repository.userId,
            FastRegion.CN_HZ
        )

        // config room options
        roomOptions.roomParams = roomOptions.roomParams.apply {
            windowParams.collectorStyles = hashMapOf(
                "right" to "12px",
                "bottom" to "60px",
                "position" to "fixed"
            )
        }

        fastRoom = fastboard.createFastRoom(roomOptions)
        fastRoom!!.setResource(object : FastResource() {
            override fun getBackgroundColor(darkMode: Boolean): Int {
                return Color.TRANSPARENT
            }
        })
        fastRoom!!.addListener(object : FastRoomListener {
            override fun onRoomReadyChanged(fastRoom: FastRoom) {
                if (fastRoom.isReady) {
                    // call room when room is ready
                }
            }
        })
        RoomOperationsKT(fastRoom!!).registerApps()
        fastRoom!!.join()

        fastboardView.fastboard.setWhiteboardRatio(null)

        // global style change
        val fastStyle = fastRoom!!.fastStyle
        fastStyle.isDarkMode = Utils.isDarkMode(this)
        fastStyle.mainColor = Utils.getThemePrimaryColor(this)
        fastRoom!!.fastStyle = fastStyle

        // change ui
        val uiSettings = fastboardView.getUiSettings()
        uiSettings.setToolboxGravity(Gravity.RIGHT)

        // dev tools display
        Utils.setupDevTools(this, fastRoom)

        // set custom logger
        FastLogger.setLogger(object : FastLogger.Logger {
            override fun debug(msg: String?) {
            }

            override fun info(msg: String?) {
            }

            override fun warn(msg: String?) {
            }

            override fun error(msg: String?) {
            }

            override fun error(msg: String?, throwable: Throwable?) {
            }
        })

        // 最小化按钮自定义
        // val controller = MinimizedWindowController(findViewById(R.id.minimize_menu));
        // fastRoom?.rootRoomController?.addController(controller);
    }

    override fun onConfigurationChanged(config: Configuration) {
        super.onConfigurationChanged(config)

        updateDayNightStyle(config)
    }

    private fun updateDayNightStyle(config: Configuration) {
        val nightMode = config.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val fastStyle = fastRoom!!.fastStyle
        fastStyle.isDarkMode = nightMode == Configuration.UI_MODE_NIGHT_YES
        fastRoom!!.fastStyle = fastStyle
    }

    override fun onResume() {
        super.onResume()
        setupFullScreen()
    }

    private fun preloadWebViewOnIdle() {
        Looper.myQueue().addIdleHandler(MessageQueue.IdleHandler {
            Fastboard.preloadWhiteboardView()
            false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (fastRoom != null) {
            fastRoom!!.destroy()
        }
    }
}