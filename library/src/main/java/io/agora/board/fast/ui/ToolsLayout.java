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

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.R;
import io.agora.board.fast.model.ApplianceItem;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ToolsLayout extends FrameLayout {
    private static final List<ApplianceItem> PHONE_APPLIANCES = new ArrayList<ApplianceItem>() {
        {
            add(ApplianceItem.CLICKER);
            add(ApplianceItem.SELECTOR);
            add(ApplianceItem.PENCIL);
            add(ApplianceItem.ERASER);
            add(ApplianceItem.ARROW);
            add(ApplianceItem.RECTANGLE);
            add(ApplianceItem.ELLIPSE);
            add(ApplianceItem.OTHER_CLEAR);
        }
    };

    private RecyclerView toolsRecyclerView;
    private ApplianceAdapter applianceAdapter;

    public ToolsLayout(@NonNull Context context) {
        this(context, null);
    }

    public ToolsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_tools, this, true);
        toolsRecyclerView = root.findViewById(R.id.tools_recycler_view);

        applianceAdapter = new ApplianceAdapter(PHONE_APPLIANCES);
        toolsRecyclerView.setAdapter(applianceAdapter);
        toolsRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
    }

    public void setOnApplianceClickListener(ApplianceAdapter.OnApplianceClickListener onApplianceClickListener) {
        applianceAdapter.setOnApplianceClickListener(onApplianceClickListener);
    }

    public void setApplianceItem(ApplianceItem item) {
        applianceAdapter.setApplianceItem(item);
    }

    public void setAppliances(List<ApplianceItem> items) {
        applianceAdapter.setAppliances(items);
    }

    public void setShown(boolean shown) {
        setVisibility(shown ? VISIBLE : GONE);
    }

    public boolean shown() {
        return getVisibility() == VISIBLE;
    }

    public void setFastStyle(FastStyle fastStyle) {
        setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        applianceAdapter.setStyle(fastStyle);
    }
}
