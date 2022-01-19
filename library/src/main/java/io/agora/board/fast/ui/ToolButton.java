package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.herewhite.sdk.domain.MemberState;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ToolButton extends FrameLayout implements RoomController {
    private ImageView toolImage;
    private ImageView toolExpand;

    public ToolButton(@NonNull Context context) {
        this(context, null);
    }

    public ToolButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fast_tool_button, this, true);
        toolImage = root.findViewById(R.id.tool_button_image);
        toolExpand = root.findViewById(R.id.tool_button_expand);
    }

    @Override
    public void updateMemberState(MemberState memberState) {
        FastAppliance item = FastAppliance.of(memberState.getCurrentApplianceName(), memberState.getShapeType());
        updateAppliance(item);
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        setBackground(ResourceFetcher.get().getButtonBackground(fastStyle.isDarkMode()));
        toolImage.setImageTintList(ResourceFetcher.get().getIconColor(fastStyle.isDarkMode()));
        toolExpand.setImageTintList(ResourceFetcher.get().getIconColor(fastStyle.isDarkMode()));
    }

    public void updateAppliance(FastAppliance fastAppliance) {
        toolImage.setImageResource(ResourceFetcher.get().getApplianceIcon(fastAppliance));
    }
}
