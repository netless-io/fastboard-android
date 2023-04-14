package io.agora.board.fast.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.R;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

class ToolboxExpand extends RelativeLayout implements Toolbox {
    private static final int OVERLAY_EXT_LAYOUT = OverlayManager.KEY_TOOL_EXPAND_EXTENSION;

    private FastRoom fastRoom;
    private OverlayManager overlayManager;

    private ViewGroup toolsLayout;
    private ViewGroup toolsAdditionLayout;
    private RecyclerView toolsRecyclerView;
    private ToolboxAdapter toolboxAdapter;
    private DeleteButton toolboxDelete;
    private ExtensionLayout extensionLayout;
    private int edgeMargin;
    private int gravity;

    public ToolboxExpand(Context context) {
        this(context, null);
    }

    public ToolboxExpand(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolboxExpand(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
        edgeMargin = context.getResources().getDimensionPixelSize(R.dimen.fast_default_layout_margin);
    }

    public void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_toolbox_expand, this, true);
        toolsLayout = root.findViewById(R.id.tools_layout);
        toolsAdditionLayout = root.findViewById(R.id.fast_tools_addition_layout);
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
        toolsLayout.setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        // toolsRecyclerView.setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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

        View layoutView = toolsLayout;
        if (isLeft) {
            LayoutParams toolsLp = (LayoutParams) layoutView.getLayoutParams();
            toolsLp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolsLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolsLp.leftMargin = edgeMargin;
            toolsLp.rightMargin = 0;

            LayoutParams subToolsLp = (LayoutParams) extensionLayout.getLayoutParams();
            subToolsLp.removeRule(RelativeLayout.LEFT_OF);
            subToolsLp.addRule(RelativeLayout.RIGHT_OF, layoutView.getId());
        }

        if (isRight) {
            LayoutParams toolsLp = (LayoutParams) layoutView.getLayoutParams();
            toolsLp.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolsLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolsLp.leftMargin = 0;
            toolsLp.rightMargin = edgeMargin;

            LayoutParams subToolsLp = (LayoutParams) extensionLayout.getLayoutParams();
            subToolsLp.removeRule(RelativeLayout.RIGHT_OF);
            subToolsLp.addRule(RelativeLayout.LEFT_OF, layoutView.getId());
        }
        requestLayout();
    }
}
