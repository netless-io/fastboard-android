package io.agora.board.fast;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.internal.FastOverlayHandler;
import io.agora.board.fast.internal.FastRoomPhaseHandler;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;
import io.agora.board.fast.ui.FastRoomController;
import io.agora.board.fast.ui.FastUiSettings;
import io.agora.board.fast.ui.LoadingLayout;
import io.agora.board.fast.ui.OverlayLayout;
import io.agora.board.fast.ui.RoomControllerGroup;

/**
 * @author fenglibin
 */
public class FastboardView extends FrameLayout implements FastListener {
    WhiteboardView whiteboardView;
    RoomControllerGroup roomControllerGroup;

    FastContext fastContext;
    FastUiSettings fastUiSettings;

    FastListener fastListener = new FastListener() {
        @Override
        public void onSceneStateChanged(SceneState sceneState) {
            roomControllerGroup.updateSceneState(sceneState);
        }

        @Override
        public void onMemberStateChanged(MemberState memberState) {
            roomControllerGroup.updateMemberState(memberState);
        }

        @Override
        public void onRedoUndoChanged(RedoUndoCount count) {
            roomControllerGroup.updateRedoUndo(count);
        }

        @Override
        public void onOverlayChanged(int key) {
            roomControllerGroup.updateOverlayChanged(key);
        }

        @Override
        public void onFastStyleChanged(FastStyle style) {
            roomControllerGroup.updateFastStyle(style);
        }

        @Override
        public void onFastRoomCreated(FastRoom fastRoom) {
            roomControllerGroup.setFastRoom(fastRoom);
        }
    };

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

    private void setupFastContext() {
        fastContext = new FastContext(this);
        fastContext.addListener(fastListener);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fastboard_view, this, true);
        whiteboardView = root.findViewById(R.id.fast_whiteboard_view);

        ViewGroup container = root.findViewById(R.id.fast_room_controller);
        roomControllerGroup = new FastRoomController(container);
        roomControllerGroup.setupView();

        LoadingLayout loadingLayout = root.findViewById(R.id.fast_loading_layout);
        roomControllerGroup.addController(loadingLayout);

        fastContext.setRoomPhaseHandler(new FastRoomPhaseHandler(loadingLayout));

        OverlayLayout overlayLayout = root.findViewById(R.id.fast_overlay_handle_view);
        roomControllerGroup.addController(overlayLayout);
        fastContext.setOverlayHandler(new FastOverlayHandler(fastContext, overlayLayout));
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

        roomControllerGroup.updateFastStyle(fastStyle);
    }

    public FastStyle getFastStyle() {
        return fastContext.getFastStyle();
    }

    public FastSdk obtainFastSdk(FastSdkOptions options) {
        return fastContext.obtainFastSdk(options);
    }

    public FastUiSettings getUiSettings() {
        if (fastUiSettings == null) {
            fastUiSettings = new FastUiSettings(this);
        }
        return fastUiSettings;
    }

    public void setRootRoomController(RoomControllerGroup roomControllerGroup) {
        RoomControllerGroup oldGroup = this.roomControllerGroup;
        oldGroup.hide();
        oldGroup.removeAll();

        this.roomControllerGroup = roomControllerGroup;
        if (fastContext.fastRoom != null) {
            roomControllerGroup.setFastRoom(fastContext.fastRoom);
        }
    }

    public RoomControllerGroup getRootRoomController() {
        return roomControllerGroup;
    }

    @Override
    public void onFastStyleChanged(FastStyle fastStyle) {
        whiteboardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.fast_day_night_bg));
        roomControllerGroup.updateFastStyle(fastStyle);
    }
}
