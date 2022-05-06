package io.agora.board.fast.sample.cases.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.ui.FastUiSettings;

public class UiConfig {
    public static void setupToolBox() {
        // config toolbox appliances
        ArrayList<List<FastAppliance>> config = new ArrayList<>();
        config.add(Arrays.asList(
                FastAppliance.CLICKER,
                FastAppliance.PENCIL,
                FastAppliance.TEXT,
                FastAppliance.SELECTOR,
                FastAppliance.ERASER
        ));
        config.add(Arrays.asList(FastAppliance.SELECTOR));
        config.add(Arrays.asList(FastAppliance.PENCIL));
        config.add(Arrays.asList(FastAppliance.TEXT));
        config.add(Arrays.asList(FastAppliance.ERASER));
        config.add(Arrays.asList(
                FastAppliance.STRAIGHT,
                FastAppliance.ARROW,
                FastAppliance.RECTANGLE,
                FastAppliance.ELLIPSE,
                FastAppliance.PENTAGRAM,
                FastAppliance.RHOMBUS,
                FastAppliance.BUBBLE,
                FastAppliance.TRIANGLE
        ));
        config.add(Arrays.asList(FastAppliance.OTHER_CLEAR));
        FastUiSettings.setToolsExpandAppliances(config);

        ArrayList<FastAppliance> collapseAppliances = new ArrayList<>();
        collapseAppliances.add(FastAppliance.PENCIL);
        collapseAppliances.add(FastAppliance.ERASER);
        collapseAppliances.add(FastAppliance.ARROW);
        collapseAppliances.add(FastAppliance.SELECTOR);
        collapseAppliances.add(FastAppliance.TEXT);
        collapseAppliances.add(FastAppliance.OTHER_CLEAR);
        FastUiSettings.setToolsCollapseAppliances(collapseAppliances);
    }

}
