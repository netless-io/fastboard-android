package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

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
public class ToolboxLayout extends RelativeLayout implements RoomController {
    private Toolbox IMPL;
    private FastRoom fastRoom;
    private FastStyle fastStyle;
    private int gravity;

    public ToolboxLayout(@NonNull Context context) {
        this(context, null);
    }

    public ToolboxLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolboxLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        IMPL = Util.isTablet(context) ? new ToolboxExpand() : new ToolboxCollapse();
        IMPL.setupView(this);
    }

    @Override
    public void updateMemberState(MemberState memberState) {
        FastAppliance appliance = FastAppliance.of(memberState.getCurrentApplianceName(), memberState.getShapeType());
        IMPL.updateAppliance(appliance);
        IMPL.updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
    }

    @Override
    public void updateOverlayChanged(int key) {
        IMPL.updateOverlayChanged(key);
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
        IMPL.setFastRoom(fastRoom);
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
        IMPL.updateFastStyle(fastStyle);
    }

    public void setLayoutGravity(int gravity) {
        this.gravity = gravity;
        IMPL.setLayoutGravity(gravity);
    }

    public void setLayoutMode(boolean expand) {
        removeAllViews();

        IMPL = expand ? new ToolboxExpand() : new ToolboxCollapse();
        IMPL.setupView(this);
        IMPL.setFastRoom(fastRoom);
        IMPL.updateFastStyle(fastStyle);
        IMPL.setLayoutGravity(gravity);

        updateMemberState(fastRoom.getRoom().getRoomState().getMemberState());
    }

    @Override
    public View getBindView() {
        return this;
    }
}
