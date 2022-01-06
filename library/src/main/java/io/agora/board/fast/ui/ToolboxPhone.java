package io.agora.board.fast.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.herewhite.sdk.domain.ShapeType;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.R;
import io.agora.board.fast.model.ApplianceItem;
import io.agora.board.fast.model.FastStyle;

class ToolboxPhone implements ToolboxImpl {
    private View overlayView;

    private ToolButton toolButton;
    private ToolsLayout toolsLayout;
    private SubToolButton subToolButton;
    private SubToolsLayout subToolsLayout;

    private Context context;
    private FastRoom fastRoom;

    @Override
    public void setupView(ToolboxLayout toolboxLayout) {
        this.context = toolboxLayout.getContext();

        View root = LayoutInflater.from(context).inflate(R.layout.layout_toolbox, toolboxLayout, true);
        overlayView = root.findViewById(R.id.overlay_handle_view);
        toolButton = root.findViewById(R.id.tool_button);
        subToolButton = root.findViewById(R.id.toolbox_sub_button);
        toolsLayout = root.findViewById(R.id.tools_layout);
        toolsLayout.setOnApplianceClickListener(item -> {
            if (item == ApplianceItem.OTHER_CLEAR) {
                fastRoom.cleanScene();
            } else {
                fastRoom.setAppliance(item);

                toolButton.setApplianceItem(item);
                subToolButton.setApplianceItem(item);
            }

            hideAllOverlay();
        });

        subToolsLayout = root.findViewById(R.id.sub_tools_layout);
        subToolsLayout.setType(SubToolsLayout.TYPE_PHONE);
        subToolsLayout.setOnColorClickListener(color -> {
            fastRoom.setColor(color);

            subToolButton.setColor(color);
            subToolsLayout.setColor(color);

            hideAllOverlay();
        });

        subToolsLayout.setOnStrokeChangedListener(width -> {
            fastRoom.setStokeWidth(width);
        });

        toolButton.setOnClickListener(v -> {
            boolean target = !toolButton.isSelected();
            toolButton.setSelected(target);
            toolsLayout.setShown(target);

            subToolButton.setSelected(false);
            subToolsLayout.setShown(false);

            updateOverlay();
        });

        subToolButton.setOnSubToolClickListener(new SubToolButton.OnSubToolClickListener() {
            @Override
            public void onDeleteClick() {
                fastRoom.getRoom().deleteOperation();
            }

            @Override
            public void onColorClick() {
                boolean target = !subToolButton.isSelected();
                subToolButton.setSelected(target);
                subToolsLayout.setShown(target);

                toolButton.setSelected(false);
                toolsLayout.setShown(false);

                updateOverlay();
            }
        });

        overlayView.setOnClickListener(v ->
                hideAllOverlay()
        );
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    private void hideAllOverlay() {
        if (toolButton.isSelected()) {
            toolButton.setSelected(false);
            toolsLayout.setShown(false);
        }

        if (subToolButton.isSelected()) {
            subToolButton.setSelected(false);
            subToolsLayout.setShown(false);
        }

        overlayView.setVisibility(GONE);
    }

    private void updateOverlay() {
        boolean showOverlay = toolButton.isSelected() || subToolButton.isSelected();
        overlayView.setVisibility(showOverlay ? VISIBLE : GONE);
    }

    @Override
    public void setFastStyle(FastStyle fastStyle) {
        subToolButton.setFastStyle(fastStyle);
        subToolsLayout.setFastStyle(fastStyle);
        toolButton.setFastStyle(fastStyle);
        toolsLayout.setFastStyle(fastStyle);
    }

    @Override
    public void updateAppliance(String appliance, ShapeType shapeType) {
        ApplianceItem item = ApplianceItem.of(appliance, shapeType);
        toolButton.setApplianceItem(item);
        toolsLayout.setApplianceItem(item);
        subToolButton.setApplianceItem(item);
    }

    @Override
    public void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        subToolButton.setColor(color);
        subToolsLayout.setColor(color);
        subToolsLayout.setStrokeWidth((int) strokeWidth);
    }
}
