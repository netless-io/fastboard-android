package io.agora.board.fast.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.R;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

class ToolboxCollapse extends RelativeLayout implements Toolbox {
    private ToolButton toolButton;
    private ToolLayout toolLayout;
    private ExtensionButton extensionButton;
    private ExtensionLayout extensionLayout;

    private FastRoom fastRoom;
    private OverlayManager overlayManager;
    private int gravity;
    private int edgeMargin;

    public ToolboxCollapse(@NonNull Context context) {
        this(context, null);
    }

    public ToolboxCollapse(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolboxCollapse(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
        edgeMargin = context.getResources().getDimensionPixelSize(R.dimen.fast_default_layout_margin);
    }

    public void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_toolbox_collapse, this, true);
        toolButton = root.findViewById(R.id.tool_button);
        extensionButton = root.findViewById(R.id.toolbox_sub_button);
        toolLayout = root.findViewById(R.id.tool_layout);
        toolLayout.setOnApplianceClickListener(item -> {
            if (item == FastAppliance.OTHER_CLEAR) {
                fastRoom.cleanScene();
            } else {
                fastRoom.setAppliance(item);
                toolButton.updateAppliance(item);
                extensionButton.setAppliance(item);
            }

            overlayManager.hideAll();
        });

        extensionLayout = root.findViewById(R.id.extension_layout);
        extensionLayout.setOnColorClickListener(color -> {
            fastRoom.setStrokeColor(color);

            extensionButton.setColor(color);
            extensionLayout.setColor(color);

            overlayManager.hideAll();
        });
        extensionLayout.setOnStrokeChangedListener(width -> {
            fastRoom.setStrokeWidth(width);
        });

        toolButton.setOnClickListener(v -> {
            boolean target = !overlayManager.isShowing(OverlayManager.KEY_TOOL_LAYOUT);
            if (target) {
                overlayManager.show(OverlayManager.KEY_TOOL_LAYOUT);
            } else {
                overlayManager.hide(OverlayManager.KEY_TOOL_LAYOUT);
            }

            toolButton.setSelected(target);
            extensionButton.setSelected(false);
        });

        extensionButton.setOnSubToolClickListener(new ExtensionButton.OnSubToolClickListener() {
            @Override
            public void onDeleteClick() {
                fastRoom.getRoom().deleteOperation();
            }

            @Override
            public void onColorClick() {
                boolean target = !overlayManager.isShowing(OverlayManager.KEY_TOOL_EXTENSION);

                if (target) {
                    overlayManager.show(OverlayManager.KEY_TOOL_EXTENSION);
                } else {
                    overlayManager.hide(OverlayManager.KEY_TOOL_EXTENSION);
                }

                extensionButton.setSelected(target);
                toolButton.setSelected(false);
            }
        });
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
        overlayManager = fastRoom.getOverlayManager();
        overlayManager.addOverlay(OverlayManager.KEY_TOOL_EXTENSION, extensionLayout);
        overlayManager.addOverlay(OverlayManager.KEY_TOOL_LAYOUT, toolLayout);
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        extensionButton.updateFastStyle(fastStyle);
        extensionLayout.updateFastStyle(fastStyle);
        toolButton.updateFastStyle(fastStyle);
        toolLayout.updateFastStyle(fastStyle);
    }

    @Override
    public void updateAppliance(FastAppliance fastAppliance) {
        toolButton.updateAppliance(fastAppliance);
        toolLayout.updateAppliance(fastAppliance);
        extensionButton.setAppliance(fastAppliance);
        extensionLayout.updateAppliance(fastAppliance);
    }

    @Override
    public void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        extensionButton.setColor(color);
        extensionLayout.setColor(color);
        extensionLayout.setStrokeWidth((int) strokeWidth);
    }

    @Override
    public void updateOverlayChanged(int key) {
        toolButton.setSelected(key == OverlayManager.KEY_TOOL_LAYOUT);
        extensionButton.setSelected(key == OverlayManager.KEY_TOOL_EXTENSION);
    }

    @Override
    public void setLayoutGravity(int gravity) {
        this.gravity = gravity;
        updateLayout();
    }

    @Override
    public void setEdgeMargin(int edgeMargin) {
        this.edgeMargin = edgeMargin;
        updateLayout();
    }

    private void updateLayout() {
        boolean isLeft = (gravity & Gravity.LEFT) == Gravity.LEFT;
        boolean isRight = (gravity & Gravity.RIGHT) == Gravity.RIGHT;

        if (isLeft) {
            RelativeLayout.LayoutParams toolBtnLp = (RelativeLayout.LayoutParams) toolButton.getLayoutParams();
            toolBtnLp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolBtnLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolBtnLp.leftMargin = edgeMargin;
            toolBtnLp.rightMargin = 0;

            RelativeLayout.LayoutParams toolsLp = (RelativeLayout.LayoutParams) toolLayout.getLayoutParams();
            toolsLp.removeRule(RelativeLayout.LEFT_OF);
            toolsLp.addRule(RelativeLayout.RIGHT_OF, toolButton.getId());

            RelativeLayout.LayoutParams subToolsLp = (RelativeLayout.LayoutParams) extensionLayout.getLayoutParams();
            subToolsLp.removeRule(RelativeLayout.LEFT_OF);
            subToolsLp.addRule(RelativeLayout.RIGHT_OF, extensionButton.getId());
        }

        if (isRight) {
            RelativeLayout.LayoutParams toolBtnLp = (RelativeLayout.LayoutParams) toolButton.getLayoutParams();
            toolBtnLp.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolBtnLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolBtnLp.leftMargin = 0;
            toolBtnLp.rightMargin = edgeMargin;

            RelativeLayout.LayoutParams toolsLp = (RelativeLayout.LayoutParams) toolLayout.getLayoutParams();
            toolsLp.removeRule(RelativeLayout.RIGHT_OF);
            toolsLp.addRule(RelativeLayout.LEFT_OF, toolButton.getId());

            RelativeLayout.LayoutParams subToolsLp = (RelativeLayout.LayoutParams) extensionLayout.getLayoutParams();
            subToolsLp.removeRule(RelativeLayout.RIGHT_OF);
            subToolsLp.addRule(RelativeLayout.LEFT_OF, extensionButton.getId());
        }
        requestLayout();
    }
}
