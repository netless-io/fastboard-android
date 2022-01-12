package io.agora.board.fast.sample.cases.drawsth;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.ShapeType;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.model.ApplianceItem;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.ui.ExtensionButton;
import io.agora.board.fast.ui.ExtensionLayout;
import io.agora.board.fast.ui.RedoUndoLayout;
import io.agora.board.fast.ui.RoomControllerGroup;
import io.agora.board.fast.ui.ToolButton;
import io.agora.board.fast.ui.ToolLayout;

public class DrawSthController extends RoomControllerGroup {
    private static final List<ApplianceItem> DRAW_STH_APPLIANCES = new ArrayList<ApplianceItem>() {
        {
            add(ApplianceItem.PENCIL);
            add(ApplianceItem.RECTANGLE);
            add(ApplianceItem.ELLIPSE);
            add(ApplianceItem.STRAIGHT);
        }
    };

    private FastRoom fastRoom;
    private OverlayHandler overlayHandler;

    private ImageView eraserView;
    private RedoUndoLayout redoUndoLayout;
    private ToolButton toolButton;
    private ToolLayout toolsLayout;

    private ExtensionButton subToolButton;
    private ExtensionLayout extensionLayout;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == toolButton) {
                triggerShown(OverlayHandler.KEY_TOOL_LAYOUT);
            } else if (v == subToolButton) {
                triggerShown(OverlayHandler.KEY_TOOL_EXTENSION);
            } else if (v == eraserView) {
                fastRoom.setAppliance(ApplianceItem.ERASER);
                overlayHandler.hideAll();
            }
        }
    };

    public DrawSthController(ViewGroup root) {
        super(root);
    }

    private void triggerShown(int key) {
        if (overlayHandler.isShowing(key)) {
            overlayHandler.hide(key);
        } else {
            overlayHandler.show(key);
        }
    }

    @Override
    public void setupView() {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_draw_sth_controller, this.root, true);
        toolButton = root.findViewById(R.id.tools_button);
        subToolButton = root.findViewById(R.id.colors_button);
        eraserView = root.findViewById(R.id.eraserView);
        redoUndoLayout = root.findViewById(R.id.redo_undo_layout);

        toolsLayout = root.findViewById(R.id.tools_layout);
        toolsLayout.setAppliances(DRAW_STH_APPLIANCES);
        toolsLayout.setOnApplianceClickListener(item -> {
            toolButton.setApplianceItem(item);
            subToolButton.setApplianceItem(item);

            fastRoom.setAppliance(item);

            overlayHandler.hideAll();
        });
        extensionLayout = root.findViewById(R.id.sub_tools_layout);
        extensionLayout.setType(ExtensionLayout.TYPE_TEXT);
        extensionLayout.setOnColorClickListener(color -> {
            subToolButton.setColor(color);
            extensionLayout.setColor(color);

            fastRoom.setColor(color);
            overlayHandler.hideAll();
        });

        toolButton.setOnClickListener(onClickListener);
        subToolButton.setOnClickListener(onClickListener);
        eraserView.setOnClickListener(onClickListener);

        addController(redoUndoLayout);
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        super.setFastRoom(fastRoom);

        this.fastRoom = fastRoom;
        this.overlayHandler = fastRoom.getOverlayHandler();
        this.overlayHandler.addOverlay(OverlayHandler.KEY_TOOL_LAYOUT, toolsLayout);
        this.overlayHandler.addOverlay(OverlayHandler.KEY_TOOL_EXTENSION, extensionLayout);
    }

    @Override
    public void updateMemberState(MemberState memberState) {
        super.updateMemberState(memberState);

        updateAppliance(memberState.getCurrentApplianceName(), memberState.getShapeType());
        updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
    }

    public void updateAppliance(String appliance, ShapeType shapeType) {
        ApplianceItem item = ApplianceItem.of(appliance, shapeType);
        toolButton.setApplianceItem(item);
        toolsLayout.setApplianceItem(item);
    }

    public void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        subToolButton.setColor(color);
        extensionLayout.setColor(color);
    }
}
