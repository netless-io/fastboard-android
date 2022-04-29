package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.herewhite.sdk.domain.MemberState;

import java.util.List;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ToolLayout extends FrameLayout implements RoomController {
    private ApplianceAdapter applianceAdapter;

    public ToolLayout(@NonNull Context context) {
        this(context, null);
    }

    public ToolLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_tools, this, true);
        RecyclerView toolsRecyclerView = root.findViewById(R.id.tools_recycler_view);

        applianceAdapter = new ApplianceAdapter(FastUiSettings.getToolsCollapseAppliances());
        toolsRecyclerView.setAdapter(applianceAdapter);
        toolsRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
    }

    public void setOnApplianceClickListener(ApplianceAdapter.OnApplianceClickListener onApplianceClickListener) {
        applianceAdapter.setOnApplianceClickListener(onApplianceClickListener);
    }

    public void setAppliances(List<FastAppliance> appliance) {
        applianceAdapter.setAppliances(appliance);
    }

    public void updateAppliance(FastAppliance appliance) {
        applianceAdapter.setApplianceItem(appliance);
    }

    @Override
    public void updateMemberState(MemberState memberState) {
        FastAppliance item = FastAppliance.of(memberState.getCurrentApplianceName(), memberState.getShapeType());
        applianceAdapter.setApplianceItem(item);
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        applianceAdapter.setStyle(fastStyle);
    }

    @Override
    public View getBindView() {
        return this;
    }
}
