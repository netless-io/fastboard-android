package io.agora.board.fast.sample.cases.helper

import com.herewhite.sdk.domain.WindowParams
import io.agora.board.fast.Fastboard
import io.agora.board.fast.FastboardView
import io.agora.board.fast.model.FastRoomOptions
import io.agora.board.fast.model.FastUserPayload
import io.agora.board.fast.sample.misc.Repository

class RoomOptionsProcessorKT(
    private val fastboardView: FastboardView,
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
        roomParams.windowParams = WindowParams()
            .setContainerSizeRatio(hwRatio)
            .setChessboard(false)
        roomOptions.roomParams = roomParams
        // before version 1.1.0 needs call this
        // fastboard.setWhiteboardRatio(hwRatio)
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
}

