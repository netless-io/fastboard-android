package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.herewhite.sdk.domain.MemberState;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.internal.Util;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ToolboxLayout extends FrameLayout implements RoomController {
    private final ToolboxExpand toolboxExpand;
    private final ToolboxCollapse toolboxCollapse;

    public ToolboxLayout(@NonNull Context context) {
        this(context, null);
    }

    public ToolboxLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolboxLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        toolboxExpand = new ToolboxExpand(context);
        toolboxCollapse = new ToolboxCollapse(context);
        addView(toolboxExpand);
        addView(toolboxCollapse);
        setLayoutMode(Util.isTablet(context));
    }

    @Override
    public void updateMemberState(MemberState memberState) {
        FastAppliance appliance = FastAppliance.of(memberState.getCurrentApplianceName(), memberState.getShapeType());

        toolboxExpand.updateAppliance(appliance);
        toolboxExpand.updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());

        toolboxCollapse.updateAppliance(appliance);
        toolboxCollapse.updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
    }

    @Override
    public void updateOverlayChanged(int key) {
        toolboxExpand.updateOverlayChanged(key);
        toolboxCollapse.updateOverlayChanged(key);
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        toolboxExpand.setFastRoom(fastRoom);
        toolboxCollapse.setFastRoom(fastRoom);
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        toolboxExpand.updateFastStyle(fastStyle);
        toolboxCollapse.updateFastStyle(fastStyle);
    }

    public void setLayoutGravity(int gravity) {
        FrameLayout.LayoutParams expandParams = (LayoutParams) toolboxExpand.getLayoutParams();
        expandParams.gravity = gravity;
        toolboxExpand.setLayoutGravity(gravity);
        toolboxExpand.setLayoutParams(expandParams);

        FrameLayout.LayoutParams collapseParams = (LayoutParams) toolboxCollapse.getLayoutParams();
        collapseParams.gravity = gravity;
        toolboxCollapse.setLayoutGravity(gravity);
        toolboxCollapse.setLayoutParams(collapseParams);
    }

    public void setEdgeMargin(int margin) {
        toolboxExpand.setEdgeMargin(margin);
        toolboxCollapse.setEdgeMargin(margin);
    }

    public void setLayoutMode(boolean expand) {
        if (expand) {
            toolboxExpand.setVisibility(VISIBLE);
            toolboxCollapse.setVisibility(GONE);
        } else {
            toolboxExpand.setVisibility(GONE);
            toolboxCollapse.setVisibility(VISIBLE);
        }
    }

    @Override
    public View getBindView() {
        return this;
    }
}
