package io.agora.board.fast.sample.cases.drawsth;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.herewhite.sdk.domain.MemberState;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.ui.ExtensionButton;
import io.agora.board.fast.ui.ExtensionLayout;
import io.agora.board.fast.ui.RedoUndoLayout;
import io.agora.board.fast.ui.ResourceFetcher;
import io.agora.board.fast.ui.RoomControllerGroup;

public class DrawSthController extends RoomControllerGroup {
    private static final List<FastAppliance> DRAW_STH_APPLIANCES = new ArrayList<FastAppliance>() {
        {
            add(FastAppliance.PENCIL);
            add(FastAppliance.RECTANGLE);
            add(FastAppliance.ELLIPSE);
            add(FastAppliance.STRAIGHT);
        }
    };

    private FastRoom fastRoom;
    private OverlayManager overlayManager;

    private ImageView eraserView;
    private ImageView pencilView;
    private ExtensionLayout colorsLayout;
    private ExtensionButton colorsButton;
    private RedoUndoLayout redoUndoLayout;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == colorsButton) {
                triggerShown(OverlayManager.KEY_TOOL_EXTENSION);
            } else if (v == pencilView) {
                fastRoom.setAppliance(FastAppliance.PENCIL);
                overlayManager.hideAll();
            } else if (v == eraserView) {
                fastRoom.setAppliance(FastAppliance.ERASER);
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
        pencilView = root.findViewById(R.id.pencil_view);
        eraserView = root.findViewById(R.id.eraser_view);
        colorsButton = root.findViewById(R.id.colors_button);
        redoUndoLayout = root.findViewById(R.id.redo_undo_layout);

        colorsLayout = root.findViewById(R.id.extension_layout);
        colorsLayout.setType(ExtensionLayout.TYPE_TEXT);
        colorsLayout.setOnColorClickListener(color -> {
            fastRoom.setColor(color);
            overlayManager.hideAll();
        });

        colorsButton.setAppliance(FastAppliance.PENCIL);

        pencilView.setOnClickListener(onClickListener);
        eraserView.setOnClickListener(onClickListener);
        colorsButton.setOnClickListener(onClickListener);

        addController(redoUndoLayout);
        addController(colorsLayout);
    }

    @Override
    public void updateMemberState(MemberState memberState) {
        super.updateMemberState(memberState);

        FastAppliance item = FastAppliance.of(memberState.getCurrentApplianceName(), memberState.getShapeType());
        pencilView.setSelected(item == FastAppliance.PENCIL);
        eraserView.setSelected(item == FastAppliance.ERASER);

        int[] strokeColor = memberState.getStrokeColor();
        colorsButton.setColor(Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]));
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        super.updateFastStyle(fastStyle);

        pencilView.setImageTintList(ResourceFetcher.get().getIconColor(fastStyle.isDarkMode(), true));
        eraserView.setImageTintList(ResourceFetcher.get().getIconColor(fastStyle.isDarkMode(), true));
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        super.setFastRoom(fastRoom);

        this.fastRoom = fastRoom;
        this.overlayManager = fastRoom.getOverlayManager();
        this.overlayManager.addOverlay(OverlayManager.KEY_TOOL_EXTENSION, colorsLayout);
    }
}
