package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.herewhite.sdk.domain.MemberState;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.internal.Util;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ToolboxLayout extends RelativeLayout implements RoomController {
    private Toolbox IMPL;

    public ToolboxLayout(@NonNull Context context) {
        this(context, null);
    }

    public ToolboxLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolboxLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        IMPL = Util.isTablet(context) ? new ToolboxTablet() : new ToolboxPhone();
        IMPL.setupView(this);
    }

    @Override
    public void updateMemberState(MemberState memberState) {
        IMPL.updateAppliance(memberState.getCurrentApplianceName(), memberState.getShapeType());
        IMPL.updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
    }

    @Override
    public void updateOverlayChanged(int key) {
        IMPL.updateOverlayChanged(key);
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        IMPL.setFastRoom(fastRoom);
    }

    public void setFastStyle(FastStyle fastStyle) {
        IMPL.updateFastStyle(fastStyle);
    }

    public void setLayoutGravity(int gravity) {
        IMPL.setLayoutGravity(gravity);
    }
}
