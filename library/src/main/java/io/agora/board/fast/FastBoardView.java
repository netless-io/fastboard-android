package io.agora.board.fast;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.ShapeType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.agora.board.fast.library.R;
import io.agora.board.fast.model.ApplianceItem;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.SubToolButton;
import io.agora.board.fast.ui.SubToolsLayout;
import io.agora.board.fast.ui.ToolButton;
import io.agora.board.fast.ui.ToolsLayout;

/**
 * @author fenglibin
 */
public class FastBoardView extends FrameLayout implements BoardStateObserver {
    WhiteboardView whiteboardView;
    FastSdk fastSdk;

    private ToolButton toolButton;
    private ToolsLayout toolsLayout;

    private SubToolButton subToolButton;
    private SubToolsLayout subToolsLayout;

    private View overlayView;
    private View loadingView;
    private ProgressBar progressBar;
    private FastStyle fastStyle;

    public FastBoardView(@NonNull Context context) {
        this(context, null);
    }

    public FastBoardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastBoardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
        setupStyle(context, attrs, defStyleAttr);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fast_board_view, this, true);
        whiteboardView = root.findViewById(R.id.real_white_board_view);
        overlayView = root.findViewById(R.id.overlay_handle_view);
        toolButton = root.findViewById(R.id.tool_button);
        subToolButton = root.findViewById(R.id.sub_tool_button);
        progressBar = root.findViewById(R.id.progress_bar_cyclic);

        toolsLayout = root.findViewById(R.id.tools_layout);
        toolsLayout.setOnApplianceClickListener(item -> {
            if (item != ApplianceItem.OTHER_CLEAR) {
                toolButton.setApplianceItem(item);
                subToolButton.setApplianceItem(item);
            }

            hideAllOverlay();

            if (item == ApplianceItem.OTHER_CLEAR) {
                fastSdk.cleanScene();
            } else {
                fastSdk.setAppliance(item.appliance);
            }
        });

        subToolsLayout = root.findViewById(R.id.sub_tools_layout);
        subToolsLayout.setOnColorClickListener(color -> {
            subToolButton.setColor(color);
            subToolsLayout.setColor(color);

            hideAllOverlay();

            fastSdk.setColor(color);
        });
        subToolsLayout.setOnStrokeChangedListener(width -> {
            fastSdk.setStokeWidth(width);
        });

        toolButton.setOnClickListener(v -> {
            boolean target = !toolButton.isSelected();
            toolButton.setSelected(target);
            toolsLayout.setShown(target);

            subToolButton.setSelected(false);
            subToolsLayout.setShown(false);

            updateOverlay();
        });

        subToolButton.setOnSubToolClickListener(new SubToolButton.OnSubToolClickListener() {
            @Override
            public void onDeleteClick() {
                fastSdk.getRoom().deleteOperation();
            }

            @Override
            public void onColorClick() {
                boolean target = !subToolButton.isSelected();
                subToolButton.setSelected(target);
                subToolsLayout.setShown(target);

                toolButton.setSelected(false);
                toolsLayout.setShown(false);

                updateOverlay();
            }
        });

        overlayView.setOnClickListener(v -> {
            hideAllOverlay();
        });

        loadingView = root.findViewById(R.id.loading_layout);
        loadingView.setVisibility(VISIBLE);
    }

    private void setupStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FastBoardView, defStyleAttr, R.style.DefaultFastBoardView);
        int mainColor = a.getColor(R.styleable.FastBoardView_fbv_main_color, Color.parseColor("#3381FF"));
        boolean darkMode = a.getBoolean(R.styleable.FastBoardView_fbv_dark_mode, false);
        a.recycle();

        fastStyle = new FastStyle();
        fastStyle.setMainColor(mainColor);
        fastStyle.setDarkMode(darkMode);
        updateStyle();
    }

    private void hideAllOverlay() {
        if (toolButton.isSelected()) {
            toolButton.setSelected(false);
            toolsLayout.setShown(false);
        }

        if (subToolButton.isSelected()) {
            subToolButton.setSelected(false);
            subToolsLayout.setShown(false);
        }

        overlayView.setVisibility(GONE);
    }

    private void updateOverlay() {
        boolean showOverlay = toolButton.isSelected() || subToolButton.isSelected();
        overlayView.setVisibility(showOverlay ? VISIBLE : GONE);
    }

    @Override
    public void onMemberStateChanged(MemberState memberState) {
        updateAppliance(memberState.getCurrentApplianceName(), memberState.getShapeType());
        updateStroke(memberState.getStrokeColor(), memberState.getStrokeWidth());
    }

    private void updateAppliance(String appliance, ShapeType shapeType) {
        ApplianceItem item = ApplianceItem.of(appliance);
        toolButton.setApplianceItem(item);
        toolsLayout.setApplianceItem(item);
        subToolButton.setApplianceItem(item);
    }

    private void updateStroke(int[] strokeColor, double strokeWidth) {
        int color = Color.rgb(strokeColor[0], strokeColor[1], strokeColor[2]);
        subToolButton.setColor(color);
        subToolsLayout.setColor(color);
        subToolsLayout.setStrokeWidth((int) strokeWidth);
    }

    @Override
    public void onRoomPhaseChanged(RoomPhase roomPhase) {
        switch (roomPhase) {
            case connecting:
            case reconnecting:
                loadingView.setVisibility(VISIBLE);
                break;
            case connected:
                loadingView.setVisibility(GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGlobalStyleChanged(FastStyle style) {
        fastStyle = style;
        updateStyle();
    }

    private void updateStyle() {
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(fastStyle.getMainColor()));
        subToolsLayout.setFastStyle(fastStyle);
    }

    public FastSdk obtainFastSdk() {
        if (fastSdk == null) {
            fastSdk = new FastSdk(this);
            fastSdk.initSdk(new FastSdkOptions(getAppId()));
            fastSdk.registerObserver(this);
        }
        return fastSdk;
    }

    public FastSdk obtainFastSdk(FastSdkOptions options) {
        if (fastSdk == null) {
            fastSdk = new FastSdk(this);
            fastSdk.initSdk(new FastSdkOptions(options.getAppId()));
            fastSdk.registerObserver(this);
        }
        return fastSdk;
    }

    String getAppId() {
        return Util.getAppIdFromMeta(getContext());
    }
}
