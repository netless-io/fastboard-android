package io.agora.board.fast.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.R;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

class ToolboxExpand implements Toolbox {
    private static final int OVERLAY_EXT_LAYOUT = OverlayManager.KEY_TOOL_EXTENSION;

    private FastRoom fastRoom;
    private OverlayManager overlayManager;

    private ToolboxLayout toolboxLayout;
    private RecyclerView toolsRecyclerView;
    private ToolboxAdapter toolboxAdapter;
    private DeleteButton toolboxDelete;
    private ExtensionLayout extensionLayout;

    @Override
    public void setupView(ToolboxLayout toolboxLayout) {
        this.toolboxLayout = toolboxLayout;
        Context context = toolboxLayout.getContext();

        View root = LayoutInflater.from(context).inflate(R.layout.layout_toolbox_expand, toolboxLayout, true);
        toolsRecyclerView = root.findViewById(R.id.tools_recycler_view);
        extensionLayout = root.findViewById(R.id.extension_layout);
        toolboxDelete = root.findViewById(R.id.toolbox_sub_delete);

        toolboxDelete.setOnClickListener(v -> {
            fastRoom.getRoom().deleteOperation();
        });

        toolboxAdapter = new ToolboxAdapter(getToolboxItems());
        toolsRecyclerView.setAdapter(toolboxAdapter);
        toolsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        toolboxAdapter.setOnToolboxClickListener(new ToolboxAdapter.OnToolboxClickListener() {
            @Override
            public void onToolboxReClick(ToolboxItem item) {
                if (overlayManager.isShowing(OVERLAY_EXT_LAYOUT)) {
                    overlayManager.hide(OVERLAY_EXT_LAYOUT);
                    return;
                }
                showExtensionIfNeed(item);
                setApplianceIfNeed(item);
            }

            private void setApplianceIfNeed(ToolboxItem item) {
                switch (item.current()) {
                    case CLICKER:
                    case SELECTOR:
                    case ERASER:
                        fastRoom.setAppliance(item.current());
                        break;
                    default:
                        break;
                }
            }

            private void showExtensionIfNeed(ToolboxItem item) {
                extensionLayout.updateToolboxItem(item);
                if (item.isExpandable()) {
                    overlayManager.show(OVERLAY_EXT_LAYOUT);
                }
            }

            @Override
            public void onSwitchToolbox(ToolboxItem item, ToolboxItem oldItem) {
                overlayManager.hide(OVERLAY_EXT_LAYOUT);

                if (item.current() == FastAppliance.OTHER_CLEAR) {
                    fastRoom.cleanScene();
                } else {
                    fastRoom.setAppliance(item.current());
                }
            }
        });

        extensionLayout.setOnStrokeChangedListener(width -> {
            fastRoom.setStrokeWidth(width);
        });

        extensionLayout.setOnColorClickListener(color -> {
            fastRoom.setStrokeColor(color);
        });

        extensionLayout.setOnApplianceClickListener(item -> {
            fastRoom.setAppliance(item);
            // hide ext layout when pick a new appliance
            overlayManager.hide(OVERLAY_EXT_LAYOUT);
        });
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
        this.overlayManager = fastRoom.getOverlayManager();
        this.overlayManager.addOverlay(OVERLAY_EXT_LAYOUT, extensionLayout);
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        toolsRecyclerView.setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        toolboxAdapter.updateFastStyle(fastStyle);
        toolboxDelete.updateFastStyle(fastStyle);
        extensionLayout.updateFastStyle(fastStyle);
    }

    @Override
    public void updateAppliance(FastAppliance fastAppliance) {
        toolboxAdapter.updateAppliance(fastAppliance);
        toolboxDelete.updateAppliance(fastAppliance);
        extensionLayout.updateAppliance(fastAppliance);
    }

    @Override
    public void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        extensionLayout.setColor(color);
        extensionLayout.setStrokeWidth((int) strokeWidth);
    }

    @Override
    public void updateOverlayChanged(int key) {

    }

    private List<ToolboxItem> getToolboxItems() {
        List<ToolboxItem> result = new ArrayList<>();
        for (List<FastAppliance> appliances : FastUiSettings.getToolsExpandAppliances()) {
            result.add(new ToolboxItem(appliances));
        }
        return result;
    }

    @Override
    public void setLayoutGravity(int gravity) {
        boolean isLeft = (gravity & Gravity.LEFT) == Gravity.LEFT;
        boolean isRight = (gravity & Gravity.RIGHT) == Gravity.RIGHT;

        if (isLeft) {
            LayoutParams toolsLp = (LayoutParams) toolsRecyclerView.getLayoutParams();
            toolsLp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolsLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            LayoutParams subToolsLp = (LayoutParams) extensionLayout.getLayoutParams();
            subToolsLp.removeRule(RelativeLayout.LEFT_OF);
            subToolsLp.addRule(RelativeLayout.RIGHT_OF, toolsRecyclerView.getId());
        }

        if (isRight) {
            LayoutParams toolsLp = (LayoutParams) toolsRecyclerView.getLayoutParams();
            toolsLp.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolsLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            LayoutParams subToolsLp = (LayoutParams) extensionLayout.getLayoutParams();
            subToolsLp.removeRule(RelativeLayout.RIGHT_OF);
            subToolsLp.addRule(RelativeLayout.LEFT_OF, toolsRecyclerView.getId());
        }
        toolboxLayout.requestLayout();
    }
}
