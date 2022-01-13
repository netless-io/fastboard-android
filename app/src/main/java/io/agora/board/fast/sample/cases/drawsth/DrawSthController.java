package io.agora.board.fast.sample.cases.drawsth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.extension.OverlayManager;
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
    private OverlayManager overlayManager;

    private ImageView eraserView;
    private ToolButton toolButton;
    private ToolLayout toolLayout;
    private ExtensionButton extensionButton;
    private ExtensionLayout extensionLayout;
    private RedoUndoLayout redoUndoLayout;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == toolButton) {
                triggerShown(OverlayManager.KEY_TOOL_LAYOUT);
            } else if (v == extensionButton) {
                triggerShown(OverlayManager.KEY_TOOL_EXTENSION);
            } else if (v == eraserView) {
                fastRoom.setAppliance(ApplianceItem.ERASER);
                overlayManager.hideAll();
            }
        }
    };

    public DrawSthController(ViewGroup root) {
        super(root);
    }

    private void triggerShown(int key) {
        if (overlayManager.isShowing(key)) {
            overlayManager.hide(key);
        } else {
            overlayManager.show(key);
        }
    }

    @Override
    public void setupView() {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_draw_sth_controller, this.root, true);
        toolButton = root.findViewById(R.id.tools_button);
        extensionButton = root.findViewById(R.id.colors_button);
        redoUndoLayout = root.findViewById(R.id.redo_undo_layout);
        eraserView = root.findViewById(R.id.eraserView);

        toolLayout = root.findViewById(R.id.tools_layout);
        toolLayout.setAppliances(DRAW_STH_APPLIANCES);
        toolLayout.setOnApplianceClickListener(item -> {
            fastRoom.setAppliance(item);

            overlayManager.hideAll();
        });
        extensionLayout = root.findViewById(R.id.sub_tools_layout);
        extensionLayout.setType(ExtensionLayout.TYPE_TEXT);
        extensionLayout.setOnColorClickListener(color -> {
            fastRoom.setColor(color);
            overlayManager.hideAll();
        });

        toolButton.setOnClickListener(onClickListener);
        extensionButton.setOnClickListener(onClickListener);
        eraserView.setOnClickListener(onClickListener);

        addController(redoUndoLayout);
        addController(toolButton);
        addController(toolLayout);
        addController(extensionButton);
        addController(extensionLayout);
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        super.setFastRoom(fastRoom);

        this.fastRoom = fastRoom;
        this.overlayManager = fastRoom.getOverlayManager();
        this.overlayManager.addOverlay(OverlayManager.KEY_TOOL_LAYOUT, toolLayout);
        this.overlayManager.addOverlay(OverlayManager.KEY_TOOL_EXTENSION, extensionLayout);
    }
}
