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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.RoomPhase;

import io.agora.board.fast.library.R;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.ResourceFetcher;
import io.agora.board.fast.ui.RoomController;

/**
 * @author fenglibin
 */
public class FastboardView extends FrameLayout implements BoardStateObserver {
    FastSdk fastSdk;
    WhiteboardView whiteboardView;
    @NonNull
    RoomController roomController;

    FastContext fastContext = new FastContext();

    private View loadingView;
    private ProgressBar progressBar;

    public FastboardView(@NonNull Context context) {
        this(context, null);
    }

    public FastboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ResourceFetcher.get().init(context);
        setupView(context);
        setupStyle(context, attrs, defStyleAttr);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fast_board_view, this, true);
        whiteboardView = root.findViewById(R.id.real_white_board_view);
        roomController = root.findViewById(R.id.default_room_controller);

        progressBar = root.findViewById(R.id.progress_bar_cyclic);
        loadingView = root.findViewById(R.id.loading_layout);
        loadingView.setVisibility(VISIBLE);
    }

    private void setupStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FastboardView, defStyleAttr, R.style.DefaultFastBoardView);
        int mainColor = a.getColor(R.styleable.FastboardView_fbv_main_color, Color.parseColor("#3381FF"));
        boolean darkMode = a.getBoolean(R.styleable.FastboardView_fbv_dark_mode, false);
        a.recycle();

        FastStyle fastStyle = new FastStyle();
        fastStyle.setMainColor(mainColor);
        fastStyle.setDarkMode(darkMode);
        fastContext.initFastStyle(fastStyle);

        roomController.updateFastStyle(fastStyle);
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
    public void onGlobalStyleChanged(FastStyle fastStyle) {
        updateStyle(fastStyle);
    }

    @Override
    public void onFastRoomCreated(FastRoom fastRoom) {
        roomController.attachRoom(fastRoom);
    }

    private void updateStyle(FastStyle fastStyle) {
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(fastStyle.getMainColor()));
        whiteboardView.setBackgroundColor(ContextCompat.getColor(
                getContext(),
                R.color.fast_day_night_bg
        ));
        roomController.updateFastStyle(fastStyle);
    }

    public FastStyle getFastStyle() {
        return fastContext.getFastStyle();
    }

    public FastSdk obtainFastSdk(FastSdkOptions options) {
        if (fastSdk == null) {
            fastSdk = new FastSdk(this);
            fastSdk.initSdk(new FastSdkOptions(options.getAppId()));
            fastSdk.registerObserver(this);
            roomController.attachSdk(fastSdk);
        }
        return fastSdk;
    }
}
