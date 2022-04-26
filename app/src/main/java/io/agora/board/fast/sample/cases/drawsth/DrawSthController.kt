package io.agora.board.fast.sample.cases.drawsth

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.herewhite.sdk.domain.MemberState
import io.agora.board.fast.FastRoom
import io.agora.board.fast.extension.OverlayManager
import io.agora.board.fast.model.FastAppliance
import io.agora.board.fast.model.FastStyle
import io.agora.board.fast.sample.R
import io.agora.board.fast.ui.*

class DrawSthController(root: ViewGroup?) : RoomControllerGroup(root) {
    private var fastRoom: FastRoom? = null
    private lateinit var overlayManager: OverlayManager
    private lateinit var eraserView: ImageView
    private lateinit var pencilView: ImageView
    private lateinit var colorsLayout: ExtensionLayout
    private lateinit var colorsButton: ExtensionButton
    private val onClickListener = View.OnClickListener { v ->
        when {
            v === colorsButton -> {
                triggerShown(OverlayManager.KEY_TOOL_EXTENSION)
            }
            v === pencilView -> {
                fastRoom!!.setAppliance(FastAppliance.PENCIL)
                overlayManager.hideAll()
            }
            v === eraserView -> {
                fastRoom!!.setAppliance(FastAppliance.ERASER)
                overlayManager.hideAll()
            }
        }
    }

    private fun triggerShown(key: Int) {
        if (overlayManager.isShowing(key)) {
            overlayManager.hide(key)
        } else {
            overlayManager.show(key)
        }
    }

    private fun setupView() {
        val root = LayoutInflater.from(context).inflate(
            R.layout.layout_draw_sth_controller,
            this.root,
            true,
        )
        pencilView = root.findViewById(R.id.pencil_view)
        eraserView = root.findViewById(R.id.eraser_view)
        colorsButton = root.findViewById(R.id.colors_button)
        val redoUndoLayout: RedoUndoLayout = root.findViewById(R.id.redo_undo_layout)
        colorsLayout = root.findViewById(R.id.extension_layout)
        colorsLayout.setType(ExtensionLayout.TYPE_TEXT)
        colorsLayout.setOnColorClickListener { color: Int? ->
            fastRoom!!.setStrokeColor(color!!)
            overlayManager.hideAll()
        }
        colorsButton.setAppliance(FastAppliance.PENCIL)
        pencilView.setOnClickListener(onClickListener)
        eraserView.setOnClickListener(onClickListener)
        colorsButton.setOnClickListener(onClickListener)
        addController(redoUndoLayout)
        addController(colorsLayout)
    }

    override fun updateMemberState(memberState: MemberState) {
        super.updateMemberState(memberState)
        val item = FastAppliance.of(memberState.currentApplianceName, memberState.shapeType)
        pencilView.isSelected = item == FastAppliance.PENCIL
        eraserView.isSelected = item == FastAppliance.ERASER
        val strokeColor = memberState.strokeColor
        colorsButton.setColor(Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]))
    }

    override fun updateFastStyle(fastStyle: FastStyle) {
        super.updateFastStyle(fastStyle)
        pencilView.imageTintList = ResourceFetcher.get().getIconColor(fastStyle.isDarkMode, true)
        eraserView.imageTintList = ResourceFetcher.get().getIconColor(fastStyle.isDarkMode, true)
    }

    override fun setFastRoom(fastRoom: FastRoom) {
        super.setFastRoom(fastRoom)
        this.fastRoom = fastRoom
        overlayManager = fastRoom.overlayManager
        overlayManager.addOverlay(OverlayManager.KEY_TOOL_EXTENSION, colorsLayout)
    }

    init {
        setupView()
    }
}