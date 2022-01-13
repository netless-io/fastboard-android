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

import com.herewhite.sdk.domain.ShapeType;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.R;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.model.ApplianceItem;
import io.agora.board.fast.model.FastStyle;

class ToolboxTablet implements Toolbox {
    private static final List<ToolboxItem> TOOLBOX_ITEMS = new ArrayList<ToolboxItem>() {
        {
            add(new ToolboxItem(ToolboxItem.KEY_CLICK, ApplianceItem.CLICKER, false));
            add(new ToolboxItem(ToolboxItem.KEY_SELECTOR, ApplianceItem.SELECTOR, false));
            add(new ToolboxItem(ToolboxItem.KEY_PENCIL, ApplianceItem.PENCIL, true));
            add(new ToolboxItem(ToolboxItem.KEY_TEXT, ApplianceItem.TEXT, true));
            add(new ToolboxItem(ToolboxItem.KEY_ERASER, ApplianceItem.ERASER, false));
            add(new ToolboxItem(ToolboxItem.KEY_SHAPE, ApplianceItem.RECTANGLE, true));
            add(new ToolboxItem(ToolboxItem.KEY_CLEAR, ApplianceItem.OTHER_CLEAR, false));
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
        extensionLayout = root.findViewById(R.id.sub_tools_layout);
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
                if (overlayManager.isShowing(OverlayManager.KEY_TOOL_EXTENSION)) {
                    overlayManager.hide(OverlayManager.KEY_TOOL_EXTENSION);
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
                        fastRoom.setAppliance(item.applianceItem);
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
                    overlayManager.show(OverlayManager.KEY_TOOL_EXTENSION);
                }
            }

            @Override
            public void onSwitchToolbox(ToolboxItem item, ToolboxItem oldItem) {
                overlayManager.hide(OverlayManager.KEY_TOOL_EXTENSION);

                if (item.applianceItem == ApplianceItem.OTHER_CLEAR) {
                    fastRoom.cleanScene();
                } else {
                    fastRoom.setAppliance(item.applianceItem);
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
            toolboxAdapter.updateSubTool(ToolboxItem.KEY_SHAPE, item);
            overlayManager.hide(OverlayManager.KEY_TOOL_EXTENSION);
        });
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
        this.overlayManager = fastRoom.getOverlayManager();
        this.overlayManager.addOverlay(OverlayManager.KEY_TOOL_EXTENSION, extensionLayout);
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        toolsRecyclerView.setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        toolboxAdapter.setStyle(fastStyle);
        toolboxDelete.setFastStyle(fastStyle);
        extensionLayout.updateFastStyle(fastStyle);
    }

    @Override
    public void updateAppliance(String appliance, ShapeType shapeType) {
        ApplianceItem item = ApplianceItem.of(appliance, shapeType);
        toolboxAdapter.setApplianceItem(item);
        toolboxDelete.setApplianceItem(item);
        extensionLayout.setApplianceItem(item);
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
