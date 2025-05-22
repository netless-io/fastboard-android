package io.agora.board.fast.sample.cases.helper

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.herewhite.sdk.domain.RoomState
import io.agora.board.fast.FastRoom
import io.agora.board.fast.FastRoomListener
import io.agora.board.fast.Fastboard
import io.agora.board.fast.FastboardView
import io.agora.board.fast.model.ControllerId
import io.agora.board.fast.model.FastRoomOptions
import io.agora.board.fast.model.FastUserPayload
import io.agora.board.fast.model.FastWindowBoxState
import io.agora.board.fast.sample.R
import io.agora.board.fast.sample.misc.Repository


class RoomOptionsProcessorKT(
    private val fastboardView: FastboardView,
    private val fastRoom: FastRoom,
    private val roomOptions: FastRoomOptions
) {
    private val repository = Repository.get()
    private val fastboard: Fastboard = fastboardView.fastboard

    /**
     * [use] false to close useMultiViews
     */
    fun useMultiViews(use: Boolean) {
        val sdkConfiguration = roomOptions.sdkConfiguration
        sdkConfiguration.useMultiViews = use
        roomOptions.sdkConfiguration = sdkConfiguration
    }

    /**
     * [hwRatio] h:w ratio
     */
    fun updateRatio(hwRatio: Float) {
        val roomParams = roomOptions.roomParams
        roomParams.windowParams.setContainerSizeRatio(hwRatio)
        roomOptions.roomParams = roomParams
        // before version 1.1.0 needs call this
        // fastboard.setWhiteboardRatio(hwRatio)
    }

    fun updateFullscreen() {
        val roomParams = roomOptions.roomParams
        roomParams.windowParams.setFullscreen(true)
        roomOptions.roomParams = roomParams
    }

    fun enableUserCursor() {
        // before version 1.3.2 call this
        // val sdkConf = roomOptions.sdkConfiguration
        // sdkConf.isUserCursor = true
        // roomOptions.sdkConfiguration = sdkConf
        //
        // val roomParams = roomOptions.roomParams
        // roomParams.userPayload = FastUserPayload("nickName")
        // roomOptions.roomParams = roomParams
        roomOptions.userPayload = FastUserPayload("Hi Nick")
    }

    // 设置窗口最小化位置
    fun updateWindowMinimizePosition() {
        val roomParams = roomOptions.roomParams.apply {
            windowParams.collectorStyles = hashMapOf(
                "right" to "12px", "bottom" to "120px", "position" to "fixed"
            )
        }
        roomOptions.roomParams = roomParams
    }

    /**
     * 示例调整 redo/undo 控件的位置，使其在界面左上方并设置顶部间距。
     */
    fun adjustRedoUndoLayout() {
        // 查找 redo/undo 的布局视图
        val view = fastboardView.findViewById<View>(R.id.fast_redo_undo_layout)
        (view.layoutParams as? FrameLayout.LayoutParams)?.apply {
            gravity = Gravity.TOP or Gravity.START
            topMargin = 100
            view.layoutParams = this
        }
    }

    /**
     * 监听窗口盒子状态变更，并根据状态显示或隐藏房间控制器。
     */
    fun observeWindowBoxState() {
        fastRoom.addListener(object : FastRoomListener {
            // 根据窗口盒子状态调整 redo/undo 与页面指示器的显示状态
            private fun handleWindowBoxStateChanged(windowBoxState: String) {
                when (FastWindowBoxState.of(windowBoxState)) {
                    FastWindowBoxState.Maximized -> {
                        fastboardView.uiSettings.hideRoomController(
                            ControllerId.RedoUndo, ControllerId.PageIndicator
                        )
                    }

                    FastWindowBoxState.Minimized, FastWindowBoxState.Normal -> {
                        fastboardView.uiSettings.showRoomController(
                            ControllerId.RedoUndo, ControllerId.PageIndicator
                        )
                    }
                }
            }

            // 房间准备状态发生变化时回调
            override fun onRoomReadyChanged(fastRoom: FastRoom) {
                if (fastRoom.isReady) {
                    fastRoom.room?.roomState?.windowBoxState?.let {
                        handleWindowBoxStateChanged(it)
                    }
                }
            }

            // 房间状态发生变化时回调
            override fun onRoomStateChanged(state: RoomState) {
                state.windowBoxState?.let {
                    handleWindowBoxStateChanged(it)
                }
            }
        })
    }
}

