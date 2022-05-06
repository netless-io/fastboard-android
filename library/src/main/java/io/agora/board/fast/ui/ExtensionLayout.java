package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ExtensionLayout extends LinearLayoutCompat implements RoomController {
    private RecyclerView appliancesRecyclerView;
    private ApplianceAdapter applianceAdapter;
    private RecyclerView colorsRecyclerView;
    private ColorAdapter colorAdapter;
    private StrokeSeeker strokeSeeker;

    public ExtensionLayout(@NonNull Context context) {
        this(context, null);
    }

    public ExtensionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtensionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        initView(context);
    }

    private void initView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fast_extension_layout, this, true);
        appliancesRecyclerView = root.findViewById(R.id.tools_ext_recycler_view);
        colorsRecyclerView = root.findViewById(R.id.colors_recycler_view);
        strokeSeeker = root.findViewById(R.id.stroke_seeker);

        applianceAdapter = new ApplianceAdapter();
        appliancesRecyclerView.setAdapter(applianceAdapter);
        appliancesRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        colorAdapter = new ColorAdapter(FastUiSettings.getToolsColors());
        colorsRecyclerView.setAdapter(colorAdapter);
        colorsRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        strokeSeeker.setStrokeRange(3, 12);

        setOnClickListener(v -> {

        });
    }

    public void setOnApplianceClickListener(ApplianceAdapter.OnApplianceClickListener onApplianceClickListener) {
        applianceAdapter.setOnApplianceClickListener(onApplianceClickListener);
    }

    public void setOnColorClickListener(ColorAdapter.OnColorClickListener onColorClickListener) {
        colorAdapter.setOnColorClickListener(onColorClickListener);
    }

    public void setOnStrokeChangedListener(StrokeSeeker.OnStrokeChangedListener onStrokeChangedListener) {
        strokeSeeker.setOnStrokeChangedListener(onStrokeChangedListener);
    }

    public void setColor(Integer color) {
        colorAdapter.setColor(color);
        strokeSeeker.setSeekerBarColor(color);
    }

    public void setStrokeWidth(int width) {
        strokeSeeker.setStrokeWidth(width);
    }

    public void updateAppliance(FastAppliance appliance) {
        applianceAdapter.updateAppliance(appliance);
        updateLayout();
    }

    public void updateToolboxItem(ToolboxItem item) {
        applianceAdapter.updateToolboxItem(item);
        updateLayout();
    }

    private void updateLayout() {
        appliancesRecyclerView.setVisibility(applianceAdapter.appliances.size() > 1 ? VISIBLE : GONE);
        colorsRecyclerView.setVisibility(applianceAdapter.curAppliance.hasProperties() ? VISIBLE : GONE);
        strokeSeeker.setVisibility(applianceAdapter.curAppliance.hasProperties() ? VISIBLE : GONE);
    }

    @Override
    public void updateFastStyle(FastStyle style) {
        setBackground(ResourceFetcher.get().getLayoutBackground(style.isDarkMode()));
        colorAdapter.setStyle(style);
        applianceAdapter.setStyle(style);
    }

    @Override
    public View getBindView() {
        return this;
    }
}
