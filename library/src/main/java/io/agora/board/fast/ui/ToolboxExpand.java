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
    private final int OVERLAY_EXT_LAYOUT = OverlayManager.KEY_TOOL_EXTENSION;

    private static final List<ToolboxItem> TOOLBOX_ITEMS = new ArrayList<ToolboxItem>() {
        {
            add(new ToolboxItem(ToolboxItem.KEY_CLICK, FastAppliance.CLICKER, false));
            add(new ToolboxItem(ToolboxItem.KEY_SELECTOR, FastAppliance.SELECTOR, false));
            add(new ToolboxItem(ToolboxItem.KEY_PENCIL, FastAppliance.PENCIL, true));
            add(new ToolboxItem(ToolboxItem.KEY_TEXT, FastAppliance.TEXT, true));
            add(new ToolboxItem(ToolboxItem.KEY_ERASER, FastAppliance.ERASER, false));
            add(new ToolboxItem(ToolboxItem.KEY_SHAPE, FastAppliance.RECTANGLE, true));
            add(new ToolboxItem(ToolboxItem.KEY_CLEAR, FastAppliance.OTHER_CLEAR, false));
        }
    };

    private FastRoom fastRoom;
    private OverlayManager overlayManager;

    private RecyclerView toolsRecyclerView;
    private ToolboxAdapter toolboxAdapter;
    private DeleteButton toolboxDelete;
    private ExtensionLayout extensionLayout;

    @Override
    public void setupView(ToolboxLayout toolboxLayout) {
        Context context = toolboxLayout.getContext();

        View root = LayoutInflater.from(context).inflate(R.layout.layout_toolbox_tablet, toolboxLayout, true);
        toolsRecyclerView = root.findViewById(R.id.tools_recycler_view);
        extensionLayout = root.findViewById(R.id.extension_layout);
        toolboxDelete = root.findViewById(R.id.toolbox_sub_delete);

        toolboxDelete.setOnClickListener(v -> {
            fastRoom.getRoom().deleteOperation();
        });

        toolboxAdapter = new ToolboxAdapter(TOOLBOX_ITEMS);
        toolsRecyclerView.setAdapter(toolboxAdapter);
        toolsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        toolboxAdapter.setOnToolboxClickListener(new ToolboxAdapter.OnToolboxClickListener() {
            @Override
            public void onToolboxReClick(ToolboxItem item) {
                if (overlayManager.isShowing(OVERLAY_EXT_LAYOUT)) {
                    overlayManager.hide(OVERLAY_EXT_LAYOUT);
                    return;
                }
                showExtensionIfNeed(item.key);
                setApplianceIfNeed(item);
            }

            private void setApplianceIfNeed(ToolboxItem item) {
                switch (item.key) {
                    case ToolboxItem.KEY_CLICK:
                    case ToolboxItem.KEY_SELECTOR:
                    case ToolboxItem.KEY_ERASER:
                        fastRoom.setAppliance(item.appliance);
                        break;
                }
            }

            private void showExtensionIfNeed(int key) {
                boolean showExtension = true;
                if (key == ToolboxItem.KEY_PENCIL) {
                    extensionLayout.setType(ExtensionLayout.TYPE_PENCIL);
                } else if (key == ToolboxItem.KEY_TEXT) {
                    extensionLayout.setType(ExtensionLayout.TYPE_PENCIL);
                } else if (key == ToolboxItem.KEY_SHAPE) {
                    extensionLayout.setType(ExtensionLayout.TYPE_TABLET_SHAPE);
                } else {
                    showExtension = false;
                }
                if (showExtension) {
                    overlayManager.show(OVERLAY_EXT_LAYOUT);
                }
            }

            @Override
            public void onSwitchToolbox(ToolboxItem item, ToolboxItem oldItem) {
                overlayManager.hide(OVERLAY_EXT_LAYOUT);

                if (item.appliance == FastAppliance.OTHER_CLEAR) {
                    fastRoom.cleanScene();
                } else {
                    fastRoom.setAppliance(item.appliance);
                }
            }
        });

        extensionLayout.setOnStrokeChangedListener(width -> {
            fastRoom.setStokeWidth(width);
        });

        extensionLayout.setOnColorClickListener(color -> {
            fastRoom.setColor(color);
        });

        extensionLayout.setOnApplianceClickListener(item -> {
            fastRoom.setAppliance(item);
            toolboxAdapter.updateToolAppliance(ToolboxItem.KEY_SHAPE, item);
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
        toolboxAdapter.setAppliance(fastAppliance);
        toolboxDelete.setAppliance(fastAppliance);
        extensionLayout.setApplianceItem(fastAppliance);
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
    }
}
