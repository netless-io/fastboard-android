package io.agora.board.fast.sample.cases.helper

import com.herewhite.sdk.domain.WindowParams
import io.agora.board.fast.Fastboard
import io.agora.board.fast.FastboardView
import io.agora.board.fast.model.FastRoomOptions

class RoomOptionsProcessorKT(
    private val fastboardView: FastboardView,
    private val roomOptions: FastRoomOptions
) {
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
        roomParams.windowParams = WindowParams()
            .setContainerSizeRatio(hwRatio)
            .setChessboard(false)
        roomOptions.roomParams = roomParams
        // before version 1.1.0 needs call this
        // fastboard.setWhiteboardRatio(hwRatio)
    }
}