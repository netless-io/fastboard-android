package io.agora.board.fast;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.RoomPhase;

import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.FastUiSettings;
import io.agora.board.fast.ui.LoadingLayout;
import io.agora.board.fast.ui.RoomController;

/**
 * @author fenglibin
 */
public class FastboardView extends FrameLayout implements FastListener {
    WhiteboardView whiteboardView;
    RoomController roomController;
    LoadingLayout loadingLayout;

    FastContext fastContext;
    FastSdk fastSdk;
    FastUiSettings fastUiSettings;

    public FastboardView(@NonNull Context context) {
        this(context, null);
    }

    public FastboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupFastContext(context);
        setupView(context);
        setupStyle(context, attrs, defStyleAttr);
    }

    private void setupFastContext(@NonNull Context context) {
        fastContext = new FastContext(context);
        fastContext.addListener(this);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fastboard_view, this, true);
        whiteboardView = root.findViewById(R.id.fast_whiteboard_view);
        roomController = root.findViewById(R.id.fast_room_controller);
        loadingLayout = root.findViewById(R.id.fast_loading_layout);
    }

    private void setupStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FastboardView, defStyleAttr, R.style.DefaultFastboardView);
        int mainColor = a.getColor(R.styleable.FastboardView_fbv_main_color, Color.parseColor("#3381FF"));
        boolean darkMode = a.getBoolean(R.styleable.FastboardView_fbv_dark_mode, false);
        a.recycle();

        FastStyle fastStyle = new FastStyle();
        fastStyle.setMainColor(mainColor);
        fastStyle.setDarkMode(darkMode);
        fastContext.initFastStyle(fastStyle);

        roomController.updateFastStyle(fastStyle);
        loadingLayout.updateFastStyle(fastStyle);
    }

    @Override
    public void onRoomPhaseChanged(RoomPhase roomPhase) {
        switch (roomPhase) {
            case connecting:
            case reconnecting:
                loadingLayout.setShown(true);
                break;
            case connected:
                loadingLayout.setShown(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFastStyleChanged(FastStyle fastStyle) {
        updateStyle(fastStyle);
    }

    private void updateStyle(FastStyle fastStyle) {
        whiteboardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.fast_day_night_bg));
        roomController.updateFastStyle(fastStyle);
        loadingLayout.updateFastStyle(fastStyle);
    }

    public FastStyle getFastStyle() {
        return fastContext.getFastStyle();
    }

    public FastSdk obtainFastSdk(FastSdkOptions options) {
        if (fastSdk == null) {
            fastSdk = new FastSdk(this);
            fastSdk.initSdk(new FastSdkOptions(options.getAppId()));
            fastContext.notifyFastSdkCreated(fastSdk);
        }
        return fastSdk;
    }

    @Override
    public void onFastSdkCreated(FastSdk fastSdk) {
        roomController.attachSdk(fastSdk);
    }

    public void setRoomController(RoomController roomController) {
        this.roomController = roomController;
        if (fastSdk != null) {
            roomController.attachSdk(fastSdk);
        }
    }

    public FastUiSettings getUiSettings() {
        if (fastUiSettings == null) {
            fastUiSettings = new FastUiSettings(this);
        }
        return fastUiSettings;
    }
}
