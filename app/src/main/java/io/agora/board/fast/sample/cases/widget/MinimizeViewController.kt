package io.agora.board.fast.sample.cases.widget

import android.view.View
import com.herewhite.sdk.WhiteboardView
import io.agora.board.fast.FastRoom
import io.agora.board.fast.model.FastWindowBoxState
import io.agora.board.fast.ui.RoomController

class MinimizedWindowController(private val indicatorView: View) : RoomController {
    private var whiteboard: WhiteboardView? = null

    override fun setFastRoom(fastRoom: FastRoom) {
        super.setFastRoom(fastRoom)
        if (fastRoom.isReady) {
            whiteboard = fastRoom.fastboardView.whiteboardView
            updateWindowBoxState(fastRoom.room.roomState.windowBoxState)
        }

        indicatorView.setOnClickListener {
            restoreWindowState()
        }
    }

    private fun restoreWindowState() {
        whiteboard?.evaluateJavascript("manager.setMinimized(false)")
    }

    override fun updateWindowBoxState(windowBoxState: String?) {
        when (FastWindowBoxState.of(windowBoxState)) {
            FastWindowBoxState.Minimized -> indicatorView.visibility = View.VISIBLE
            else -> indicatorView.visibility = View.GONE
        }
    }

    override fun getBindView(): View {
        return indicatorView
    }
}