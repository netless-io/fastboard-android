package io.agora.board.fast.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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

    private View overlayView;
    private RecyclerView toolsRecyclerView;
    private ToolboxAdapter toolboxAdapter;
    private DeleteButton toolboxDelete;
    private SubToolsLayout subToolsLayout;

    @Override
    public void setupView(ToolboxLayout toolboxLayout) {
        Context context = toolboxLayout.getContext();

        View root = LayoutInflater.from(context).inflate(R.layout.layout_toolbox_tablet, toolboxLayout, true);
        overlayView = root.findViewById(R.id.overlay_handle_view);
        toolsRecyclerView = root.findViewById(R.id.tools_recycler_view);
        subToolsLayout = root.findViewById(R.id.sub_tools_layout);
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
                if (subToolsLayout.shown()) {
                    subToolsLayout.setShown(false);
                    return;
                }
                switch (item.key) {
                    case ToolboxItem.KEY_CLICK:
                    case ToolboxItem.KEY_SELECTOR:
                    case ToolboxItem.KEY_ERASER:
                        fastRoom.setAppliance(item.applianceItem);
                        subToolsLayout.setShown(false);
                        break;
                    case ToolboxItem.KEY_PENCIL:
                        subToolsLayout.setType(SubToolsLayout.TYPE_PENCIL);
                        subToolsLayout.setShown(true);
                        break;
                    case ToolboxItem.KEY_TEXT:
                        subToolsLayout.setType(SubToolsLayout.TYPE_TEXT);
                        subToolsLayout.setShown(true);
                        break;
                    case ToolboxItem.KEY_SHAPE:
                        subToolsLayout.setType(SubToolsLayout.TYPE_TABLET_SHAPE);
                        subToolsLayout.setShown(true);
                        break;
                    default:
                        break;
                }
                updateOverlay();
            }

            @Override
            public void onSwitchToolbox(ToolboxItem item, ToolboxItem oldItem) {
                subToolsLayout.setShown(false);

                if (item.applianceItem == ApplianceItem.OTHER_CLEAR) {
                    fastRoom.cleanScene();
                } else {
                    fastRoom.setAppliance(item.applianceItem);
                }
            }
        });

        subToolsLayout.setOnStrokeChangedListener(width -> {
            fastRoom.setStokeWidth(width);

            subToolsLayout.setStrokeWidth(width);
        });

        subToolsLayout.setOnColorClickListener(color -> {
            fastRoom.setColor(color);
            subToolsLayout.setColor(color);
        });

        subToolsLayout.setOnApplianceClickListener(item -> {
            fastRoom.setAppliance(item);
            toolboxAdapter.updateSubTool(ToolboxItem.KEY_SHAPE, item);
            subToolsLayout.setShown(false);
            updateOverlay();
        });

        overlayView.setOnClickListener(v ->
                hideAllOverlay()
        );
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    @Override
    public void setFastStyle(FastStyle fastStyle) {
        toolsRecyclerView.setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        toolboxAdapter.setStyle(fastStyle);
        toolboxDelete.setFastStyle(fastStyle);
        subToolsLayout.setFastStyle(fastStyle);
    }

    @Override
    public void updateAppliance(String appliance, ShapeType shapeType) {
        ApplianceItem item = ApplianceItem.of(appliance, shapeType);
        toolboxAdapter.setApplianceItem(item);
        toolboxDelete.setApplianceItem(item);
        subToolsLayout.setApplianceItem(item);
    }

    @Override
    public void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        subToolsLayout.setColor(color);
        subToolsLayout.setStrokeWidth((int) strokeWidth);
    }

    @Override
    public void setLayoutGravity(int gravity) {
        boolean isLeft = (gravity & Gravity.LEFT) == Gravity.LEFT;
        boolean isRight = (gravity & Gravity.RIGHT) == Gravity.RIGHT;

        if (isLeft) {
            LayoutParams toolsLp = (LayoutParams) toolsRecyclerView.getLayoutParams();
            toolsLp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            toolsLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            LayoutParams subToolsLp = (LayoutParams) subToolsLayout.getLayoutParams();
            subToolsLp.removeRule(RelativeLayout.LEFT_OF);
            subToolsLp.addRule(RelativeLayout.RIGHT_OF, toolsRecyclerView.getId());
        }

        if (isRight) {
            LayoutParams toolsLp = (LayoutParams) toolsRecyclerView.getLayoutParams();
            toolsLp.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            toolsLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            LayoutParams subToolsLp = (LayoutParams) subToolsLayout.getLayoutParams();
            subToolsLp.removeRule(RelativeLayout.RIGHT_OF);
            subToolsLp.addRule(RelativeLayout.LEFT_OF, toolsRecyclerView.getId());
        }
    }

    private void hideAllOverlay() {
        subToolsLayout.setShown(false);
        overlayView.setVisibility(GONE);
    }

    private void updateOverlay() {
        overlayView.setVisibility(subToolsLayout.shown() ? VISIBLE : GONE);
    }
}
