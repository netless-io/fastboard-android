package io.agora.board.fast.sample.cases.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    public static void removeClearAppliance() {
        // Publish Collection is not safe, ensure call at Main Thread or Scope
        List<List<FastAppliance>> expandAppliances = FastUiSettings.getToolsExpandAppliances();
        Iterator<List<FastAppliance>> iterator = expandAppliances.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().contains(FastAppliance.OTHER_CLEAR)) {
                iterator.remove();
            }
        }

        List<FastAppliance> appliances = FastUiSettings.getToolsCollapseAppliances();
        appliances.remove(FastAppliance.OTHER_CLEAR);
        FastUiSettings.setToolsCollapseAppliances(appliances);
    }

    public static void setupSampleCollapseAppliances() {
        ArrayList<FastAppliance> collapseAppliances = new ArrayList<>();
        collapseAppliances.add(FastAppliance.CLICKER);
        collapseAppliances.add(FastAppliance.SELECTOR);
        collapseAppliances.add(FastAppliance.PENCIL);
        collapseAppliances.add(FastAppliance.ERASER);
        collapseAppliances.add(FastAppliance.ARROW);
        collapseAppliances.add(FastAppliance.RECTANGLE);
        collapseAppliances.add(FastAppliance.TEXT);
        collapseAppliances.add(FastAppliance.OTHER_CLEAR);
        FastUiSettings.setToolsCollapseAppliances(collapseAppliances);
    }
}
