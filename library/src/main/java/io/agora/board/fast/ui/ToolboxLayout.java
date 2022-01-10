package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.herewhite.sdk.domain.MemberState;

import io.agora.board.fast.FastListener;
import io.agora.board.fast.FastRoom;
import io.agora.board.fast.FastSdk;
import io.agora.board.fast.internal.Util;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ToolboxLayout extends RelativeLayout implements FastListener, RoomController {
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
    public void onMemberStateChanged(MemberState memberState) {
        IMPL.updateAppliance(memberState.getCurrentApplianceName(), memberState.getShapeType());
        IMPL.updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
    }

    public void setShown(boolean shown) {
        setVisibility(shown ? VISIBLE : GONE);
    }

    public boolean shown() {
        return getVisibility() == VISIBLE;
    }

    public void setLayoutGravity(int gravity) {
        IMPL.setLayoutGravity(gravity);
    }

    @Override
    public void attachSdk(FastSdk fastSdk) {
        fastSdk.addListener(this);
    }

    public void setFastStyle(FastStyle fastStyle) {
        IMPL.setFastStyle(fastStyle);
    }

    @Override
    public void onFastRoomCreated(FastRoom fastRoom) {
        IMPL.setFastRoom(fastRoom);
    }
}
