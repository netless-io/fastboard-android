package io.agora.board.fast.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.herewhite.sdk.domain.MemberState;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ExtensionLayout extends LinearLayoutCompat implements RoomController {
    static final int SHOW_MASK = 0xF000;
    static final int SHOW_TOOLS = 0x1000;
    static final int SHOW_SEEKER = 0x2000;
    static final int SHOW_COLORS = 0x4000;

    public static final int TYPE_PHONE = 1 | SHOW_SEEKER | SHOW_COLORS;
    public static final int TYPE_TEXT = 2 | SHOW_COLORS;
    public static final int TYPE_PENCIL = 3 | SHOW_SEEKER | SHOW_COLORS;
    public static final int TYPE_TABLET_SHAPE = 4 | SHOW_TOOLS | SHOW_SEEKER | SHOW_COLORS;
    private static final List<FastAppliance> DEFAULT_TOOLS = new ArrayList<FastAppliance>() {
        {
            add(FastAppliance.RECTANGLE);
            add(FastAppliance.ELLIPSE);
            add(FastAppliance.STRAIGHT);
            add(FastAppliance.ARROW);
            add(FastAppliance.PENTAGRAM);
            add(FastAppliance.RHOMBUS);
            add(FastAppliance.BUBBLE);
            add(FastAppliance.TRIANGLE);
        }
    };
    private static final List<Integer> DEFAULT_COLORS = new ArrayList<Integer>() {
        {
            add(Color.parseColor("#EC3455"));
            add(Color.parseColor("#F5AD46"));
            add(Color.parseColor("#68AB5D"));
            add(Color.parseColor("#32C5FF"));
            add(Color.parseColor("#005BF6"));
            add(Color.parseColor("#6236FF"));
            add(Color.parseColor("#9E51B6"));
            add(Color.parseColor("#6D7278"));
        }
    };
    private RecyclerView toolsRecyclerView;
    private ApplianceAdapter toolsAdapter;
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
        toolsRecyclerView = root.findViewById(R.id.tools_ext_recycler_view);
        colorsRecyclerView = root.findViewById(R.id.colors_recycler_view);
        strokeSeeker = root.findViewById(R.id.stroke_seeker);

        toolsAdapter = new ApplianceAdapter(DEFAULT_TOOLS);
        toolsRecyclerView.setAdapter(toolsAdapter);
        toolsRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        colorAdapter = new ColorAdapter(DEFAULT_COLORS);
        colorsRecyclerView.setAdapter(colorAdapter);
        colorsRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        strokeSeeker.setStrokeRange(3, 12);

        setOnClickListener(v -> {

        });
    }

    public void setOnApplianceClickListener(ApplianceAdapter.OnApplianceClickListener onApplianceClickListener) {
        toolsAdapter.setOnApplianceClickListener(onApplianceClickListener);
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

    public void setApplianceItem(FastAppliance applianceItem) {
        toolsAdapter.setApplianceItem(applianceItem);
    }

    @Override
    public void updateMemberState(MemberState memberState) {
        updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
    }

    private void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        this.setColor(color);
    }

    @Override
    public void updateFastStyle(FastStyle style) {
        setBackground(ResourceFetcher.get().getLayoutBackground(style.isDarkMode()));
        colorAdapter.setStyle(style);
        toolsAdapter.setStyle(style);
    }

    @Override
    public View getBindView() {
        return this;
    }

    public void setType(@Type int type) {
        int showFlag = type & SHOW_MASK;

        toolsRecyclerView.setVisibility((showFlag & SHOW_TOOLS) > 0 ? VISIBLE : GONE);
        strokeSeeker.setVisibility((showFlag & SHOW_SEEKER) > 0 ? VISIBLE : GONE);
        colorsRecyclerView.setVisibility((showFlag & SHOW_COLORS) > 0 ? VISIBLE : GONE);
    }

    @IntDef({TYPE_PHONE, TYPE_TEXT, TYPE_PENCIL, TYPE_TABLET_SHAPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
}
