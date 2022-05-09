package io.agora.board.fast.sample.cases.helper

import io.agora.board.fast.model.FastAppliance
import io.agora.board.fast.ui.FastUiSettings

object UiConfigKT {
    @JvmStatic
    fun setupToolBox() {
        // config toolbox appliances
        val config = listOf(
            listOf(
                FastAppliance.CLICKER,
                FastAppliance.PENCIL,
                FastAppliance.TEXT,
                FastAppliance.SELECTOR,
                FastAppliance.ERASER
            ),
            listOf(FastAppliance.SELECTOR),
            listOf(FastAppliance.PENCIL),
            listOf(FastAppliance.TEXT),
            listOf(FastAppliance.ERASER),
            listOf(
                FastAppliance.STRAIGHT,
                FastAppliance.ARROW,
                FastAppliance.RECTANGLE,
                FastAppliance.ELLIPSE,
                FastAppliance.PENTAGRAM,
                FastAppliance.RHOMBUS,
                FastAppliance.BUBBLE,
                FastAppliance.TRIANGLE
            ),
            // listOf(FastAppliance.OTHER_CLEAR),
        )
        FastUiSettings.setToolsExpandAppliances(config)

        val collapseAppliances = listOf(
            FastAppliance.PENCIL,
            FastAppliance.ERASER,
            FastAppliance.ARROW,
            FastAppliance.SELECTOR,
            FastAppliance.TEXT,
            // FastAppliance.OTHER_CLEAR,
        )
        FastUiSettings.setToolsCollapseAppliances(collapseAppliances)
    }

    @JvmStatic
    fun removeClearAppliance() {
        // Publish Collection is not safe, ensure call at Main Thread or Scope
        val expandAppliances = FastUiSettings.getToolsExpandAppliances()
        expandAppliances.removeAll { it.contains(FastAppliance.OTHER_CLEAR) }
        FastUiSettings.setToolsExpandAppliances(expandAppliances);

        val appliances = FastUiSettings.getToolsCollapseAppliances()
        appliances.remove(FastAppliance.OTHER_CLEAR)
        FastUiSettings.setToolsCollapseAppliances(appliances)
    }
}