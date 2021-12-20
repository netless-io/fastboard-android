package io.agora.board.fast.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.library.R;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class SubToolsLayout extends LinearLayout {
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

    private RecyclerView colorsRecyclerView;
    private ColorAdapter colorAdapter;

    private StrokeSeeker strokeSeeker;

    public SubToolsLayout(@NonNull Context context) {
        this(context, null);
    }

    public SubToolsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubToolsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        initView(context);
    }

    private void initView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_sub_tools, this, true);
        colorsRecyclerView = root.findViewById(R.id.colors_recycler_view);
        strokeSeeker = root.findViewById(R.id.stroke_seeker);

        colorAdapter = new ColorAdapter(DEFAULT_COLORS);
        colorsRecyclerView.setAdapter(colorAdapter);
        colorsRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        strokeSeeker.setStrokeRange(3, 12);

        setOnClickListener(v -> {

        });
    }

    public void setOnColorClickListener(ColorAdapter.OnColorClickListener onColorClickListener) {
        colorAdapter.setOnColorClickListener(onColorClickListener);
    }

    public void setOnStrokeChangedListener(StrokeSeeker.OnStrokeChangedListener onStrokeChangedListener) {
        strokeSeeker.setOnStrokeChangedListener(onStrokeChangedListener);
    }

    public void setShown(boolean shown) {
        setVisibility(shown ? VISIBLE : INVISIBLE);
    }

    public boolean shown() {
        return getVisibility() == VISIBLE;
    }

    public void setColor(Integer color) {
        colorAdapter.setCurrentColor(color);
        strokeSeeker.setSeekerBarColor(color);
    }

    public void setStrokeWidth(int width) {
        strokeSeeker.setStrokeWidth(width);
    }

    public void setFastStyle(FastStyle style) {
        this.setBackground(ResourceFetcher.get().getLayoutBackground(style.isDarkMode()));
        colorAdapter.setStyle(style);
    }
}
