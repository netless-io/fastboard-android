package io.agora.board.fast;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.RoomState;

import io.agora.board.fast.internal.FastErrorHandler;
import io.agora.board.fast.internal.FastOverlayManager;
import io.agora.board.fast.internal.FastRoomPhaseHandler;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.ErrorHandleLayout;
import io.agora.board.fast.ui.FastRoomController;
import io.agora.board.fast.ui.FastUiSettings;
import io.agora.board.fast.ui.LoadingLayout;
import io.agora.board.fast.ui.OverlayLayout;
import io.agora.board.fast.ui.RoomControllerGroup;

/**
 * @author fenglibin
 */
public class FastboardView extends FrameLayout {
    WhiteboardView whiteboardView;
    RoomControllerGroup roomControllerGroup;

    FastContext fastContext;
    FastUiSettings fastUiSettings;
    FastListener fastListener = new FastListener() {
        @Override
        public void onRoomReadyChanged(FastRoom fastRoom) {
            roomControllerGroup.setFastRoom(fastRoom);
        }

        @Override
        public void onRoomStateChanged(RoomState roomState) {
            if (roomState.getBroadcastState() != null) {
                roomControllerGroup.updateBroadcastState(roomState.getBroadcastState());
            }

            if (roomState.getMemberState() != null) {
                roomControllerGroup.updateMemberState(roomState.getMemberState());
            }

            if (roomState.getSceneState() != null) {
                roomControllerGroup.updateSceneState(roomState.getSceneState());
            }

            if (roomState.getPageState() != null) {
                roomControllerGroup.updatePageState(roomState.getPageState());
            }

            if (roomState.getWindowBoxState() != null) {
                roomControllerGroup.updateWindowBoxState(roomState.getWindowBoxState());
            }
        }

        @Override
        public void onRedoUndoChanged(FastRedoUndo count) {
            roomControllerGroup.updateRedoUndo(count);
        }

        @Override
        public void onOverlayChanged(int key) {
            roomControllerGroup.updateOverlayChanged(key);
        }

        @Override
        public void onFastStyleChanged(FastStyle style) {
            whiteboardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.fast_day_night_bg));
            roomControllerGroup.updateFastStyle(style);
        }
    };
    private float whiteboardRatio = 9.0f / 16;

    public FastboardView(@NonNull Context context) {
        this(context, null);
    }

    public FastboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupFastContext();
        setupView(context);
        setupStyle(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        post(() -> updateWhiteboardLayout(w, h, whiteboardRatio));
    }

    private void updateWhiteboardLayout(int w, int h, float ratio) {
        FrameLayout.LayoutParams lps = (LayoutParams) whiteboardView.getLayoutParams();
        if (h / ratio >= w) {
            lps.height = (int) (w * ratio);
            lps.width = w;
        } else {
            lps.height = h;
            lps.width = (int) (h / ratio);
        }
        whiteboardView.setLayoutParams(lps);
    }

    private void setupFastContext() {
        fastContext = new FastContext(this);
        fastContext.addListener(fastListener);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fastboard_view, this, true);
        whiteboardView = root.findViewById(R.id.fast_whiteboard_view);

        ViewGroup container = root.findViewById(R.id.fast_room_controller);
        LoadingLayout loadingLayout = root.findViewById(R.id.fast_loading_layout);
        ErrorHandleLayout errorHandleLayout = root.findViewById(R.id.fast_error_handle_layout);
        OverlayLayout overlayLayout = root.findViewById(R.id.fast_overlay_handle_view);

        roomControllerGroup = new FastRoomController(container);
        roomControllerGroup.addController(loadingLayout);
        roomControllerGroup.addController(errorHandleLayout);
        roomControllerGroup.addController(overlayLayout);

        fastContext.setRoomPhaseHandler(new FastRoomPhaseHandler(loadingLayout));
        fastContext.setErrorHandler(new FastErrorHandler(errorHandleLayout));
        fastContext.setOverlayManager(new FastOverlayManager(fastContext, overlayLayout));
    }

    private void setupStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FastboardView, defStyleAttr, R.style.DefaultFastboardView);
        int mainColor = a.getColor(R.styleable.FastboardView_fbv_main_color, ContextCompat.getColor(context, R.color.fast_default_main_color));
        boolean darkMode = a.getBoolean(R.styleable.FastboardView_fbv_dark_mode, false);
        a.recycle();

        FastStyle fastStyle = new FastStyle();
        fastStyle.setMainColor(mainColor);
        fastStyle.setDarkMode(darkMode);
        fastContext.setFastStyle(fastStyle);
    }

    public FastStyle getFastStyle() {
        return fastContext.getFastStyle();
    }

    public Fastboard getFastboard() {
        return fastContext.getFastboard();
    }

    public FastUiSettings getUiSettings() {
        if (fastUiSettings == null) {
            fastUiSettings = new FastUiSettings(this);
        }
        return fastUiSettings;
    }

    public RoomControllerGroup getRootRoomController() {
        return roomControllerGroup;
    }

    public void setRootRoomController(RoomControllerGroup roomControllerGroup) {
        RoomControllerGroup oldGroup = this.roomControllerGroup;
        oldGroup.hide();
        oldGroup.removeAll();

        this.roomControllerGroup = roomControllerGroup;
        if (fastContext.fastRoom != null) {
            roomControllerGroup.setFastRoom(fastContext.fastRoom);
            roomControllerGroup.updateFastStyle(fastContext.fastStyle);
        }
    }

    public void setWhiteboardRatio(float ratio) {
        this.whiteboardRatio = ratio;
        updateWhiteboardLayout(getWidth(), getHeight(), ratio);
    }
}
