package io.agora.board.fast.ui;

import android.graphics.Color;
import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.agora.board.fast.FastboardView;
import io.agora.board.fast.R;
import io.agora.board.fast.model.ControllerId;
import io.agora.board.fast.model.FastAppliance;

/**
 * a class to controller ui
 *
 * @author fenglibin
 * @experiment
 */
public class FastUiSettings {
    private static List<List<FastAppliance>> toolsExpandAppliances = new ArrayList<List<FastAppliance>>() {
        {
            add(Arrays.asList(FastAppliance.CLICKER));
            add(Arrays.asList(FastAppliance.SELECTOR));
            add(Arrays.asList(FastAppliance.PENCIL));
            add(Arrays.asList(FastAppliance.TEXT));
            add(Arrays.asList(FastAppliance.ERASER));
            add(Arrays.asList(
                    FastAppliance.RECTANGLE,
                    FastAppliance.ELLIPSE,
                    FastAppliance.STRAIGHT,
                    FastAppliance.ARROW,
                    FastAppliance.PENTAGRAM,
                    FastAppliance.RHOMBUS,
                    FastAppliance.BUBBLE,
                    FastAppliance.TRIANGLE
            ));
            add(Arrays.asList(FastAppliance.OTHER_CLEAR));
        }
    };

    private static List<FastAppliance> toolsCollapseAppliances = new ArrayList<FastAppliance>() {
        {
            add(FastAppliance.CLICKER);
            add(FastAppliance.SELECTOR);
            add(FastAppliance.PENCIL);
            add(FastAppliance.ERASER);
            add(FastAppliance.ARROW);
            add(FastAppliance.RECTANGLE);
            add(FastAppliance.ELLIPSE);
            add(FastAppliance.OTHER_CLEAR);
        }
    };

    private static List<Integer> toolsColors = new ArrayList<Integer>() {
        {
            add(Color.parseColor("#EC3455"));
            add(Color.parseColor("#F5AD46"));
            add(Color.parseColor("#68AB5D"));
            add(Color.parseColor("#32C5FF"));
            add(Color.parseColor("#005BF6"));
            add(Color.parseColor("#6236FF"));
            add(Color.parseColor("#9E51B6"));
            add(Color.parseColor("#6D7278"));
        }
    };
    private final FastboardView fastboardView;

    public FastUiSettings(FastboardView fastboardView) {
        this.fastboardView = fastboardView;
    }

    public static List<List<FastAppliance>> getToolsExpandAppliances() {
        return toolsExpandAppliances;
    }

    /**
     * config expanded toolbox tools
     *
     * @param toolsExpandAppliances
     */
    public static void setToolsExpandAppliances(List<List<FastAppliance>> toolsExpandAppliances) {
        FastUiSettings.toolsExpandAppliances = toolsExpandAppliances;
    }

    public static List<FastAppliance> getToolsCollapseAppliances() {
        return toolsCollapseAppliances;
    }

    /**
     * config collapsed toolbox tools
     *
     * @param toolsCollapseAppliances
     */
    public static void setToolsCollapseAppliances(List<FastAppliance> toolsCollapseAppliances) {
        FastUiSettings.toolsCollapseAppliances = toolsCollapseAppliances;
    }

    public static List<Integer> getToolsColors() {
        return toolsColors;
    }

    /**
     * change toolbox colors
     *
     * @param toolsColors
     */
    public static void setToolsColors(List<Integer> toolsColors) {
        FastUiSettings.toolsColors = toolsColors;
    }

    /**
     * change toolbox gravity
     *
     * @param gravity Gravity.LEFT : Gravity.RIGHT
     */
    public void setToolboxGravity(int gravity) {
        ToolboxLayout toolbox = fastboardView.findViewById(R.id.fast_toolbox_layout);
        toolbox.setLayoutGravity(gravity);
    }

    /**
     * change toolbox expand or not
     *
     * @param expand true display as expand mode
     */
    public void setToolboxExpand(boolean expand) {
        ToolboxLayout toolboxLayout = fastboardView.findViewById(R.id.fast_toolbox_layout);
        toolboxLayout.setLayoutMode(expand);
    }

    /**
     * change redoUndoLayout as HORIZONTAL, VERTICAL
     *
     * @param orientation HORIZONTAL or VERTICAL
     */
    public void setRedoUndoOrientation(@LinearLayoutCompat.OrientationMode int orientation) {
        RedoUndoLayout redoUndoLayout = fastboardView.findViewById(R.id.fast_redo_undo_layout);
        redoUndoLayout.setOrientation(orientation);
    }

    /**
     * hide controllers
     * ids defined in {@link ControllerId}
     *
     * @param ids
     */
    public void hideRoomController(ControllerId... ids) {
        for (ControllerId id : ids) {
            View view = fastboardView.findViewById(id.getLayoutId());
            if (view instanceof RoomController) {
                ((RoomController) view).hide();
            }
        }
    }

    /**
     * show controllers
     * ids defined in {@link ControllerId}
     *
     * @param ids
     */
    public void showRoomController(ControllerId... ids) {
        for (ControllerId id : ids) {
            View view = fastboardView.findViewById(id.getLayoutId());
            if (view instanceof RoomController) {
                ((RoomController) view).show();
            }
        }
    }
}
