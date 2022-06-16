package io.agora.board.fast.sample.cases.drawsth

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.herewhite.sdk.domain.WindowParams
import io.agora.board.fast.FastRoom
import io.agora.board.fast.Fastboard
import io.agora.board.fast.FastboardView
import io.agora.board.fast.model.FastRegion
import io.agora.board.fast.model.FastRoomOptions
import io.agora.board.fast.sample.Constants
import io.agora.board.fast.sample.R
import io.agora.board.fast.sample.cases.base.BaseActivity
import io.agora.board.fast.sample.cases.helper.UiConfig
import io.agora.board.fast.sample.misc.Repository
import io.agora.board.fast.sample.misc.Utils

open class DrawSthActivity : BaseActivity() {
    private val repository = Repository.get()
    private lateinit var fastboard: Fastboard
    private lateinit var fastRoom: FastRoom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_sth)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        UiConfig.setupSampleCollapseAppliances()
        setupFastboard()
    }

    private fun setupFastboard() {
        val fastboardView = findViewById<FastboardView>(R.id.fastboard_view)
        fastboard = fastboardView.fastboard
        val roomOptions = FastRoomOptions(
            Constants.SAMPLE_APP_ID,
            intent.getStringExtra(Constants.KEY_ROOM_UUID),
            intent.getStringExtra(Constants.KEY_ROOM_TOKEN),
            repository.userId,
            FastRegion.CN_HZ
        )

        fastRoom = fastboard.createFastRoom(roomOptions)
        fastRoom.join()
        fastRoom.rootRoomController = DrawSthController(findViewById(R.id.draw_sth_controller))
        updateFastStyle()
    }

    private fun updateFastStyle() {
        // global style change
        val fastStyle = fastRoom.fastStyle
        fastStyle.isDarkMode = Utils.isDarkMode(this)
        fastStyle.mainColor = Utils.getThemePrimaryColor(this)
        fastRoom.fastStyle = fastStyle
    }

    private fun updateRatio(roomOptions: FastRoomOptions) {
        val hwRatio = 16f / 9
        // 加入房间时需要设置 [FastRoomOptions] 多窗口属性
        val roomParams = roomOptions.roomParams
        roomParams.windowParams = WindowParams()
            .setContainerSizeRatio(hwRatio)
            .setChessboard(false)
        roomOptions.roomParams = roomParams
        // 设置内部白板整体大小
        fastboard.setWhiteboardRatio(hwRatio)
    }
}