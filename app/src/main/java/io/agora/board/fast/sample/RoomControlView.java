package io.agora.board.fast.sample;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.model.ControllerId;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.FastUiSettings;

public class RoomControlView extends FrameLayout {
    private FastRoom fastRoom;

    private final View controllerLayout;
    private final ImageView handleView;

    public RoomControlView(@NonNull Context context) {
        this(context, null);
    }

    public RoomControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoomControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_control_view, this, true);
        controllerLayout = root.findViewById(R.id.controller_layout);
        handleView = findViewById(R.id.handle);

        SwitchCompat darkModeSwitch = root.findViewById(R.id.dark_mode_switch);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (fastRoom != null) {
                FastStyle fastStyle = fastRoom.getFastStyle();
                fastStyle.setDarkMode(isChecked);
                fastRoom.setFastStyle(fastStyle);
            }
        });

        root.findViewById(R.id.clear).setOnClickListener(v -> {
            fastRoom.getRoom().removeScenes("/");
        });

        SwitchCompat visiableSwitch = root.findViewById(R.id.controller_visiable_switch);
        visiableSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FastUiSettings uiSettings = fastRoom.getFastboardView().getUiSettings();
            if (isChecked) {
                uiSettings.hideRoomController(ControllerId.RedoUndo, ControllerId.PageIndicator, ControllerId.ToolBox);
            } else {
                uiSettings.showRoomController(ControllerId.RedoUndo, ControllerId.PageIndicator, ControllerId.ToolBox);
            }
        });

        SwitchCompat writableSwitch = root.findViewById(R.id.writable_switch);
        writableSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> fastRoom.setWritable(isChecked));

        SwitchCompat toolboxSwitch = root.findViewById(R.id.toolbox_mode_switch);
        toolboxSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FastUiSettings uiSettings = fastRoom.getFastboardView().getUiSettings();
            uiSettings.setToolboxExpand(isChecked);
        });

        SwitchCompat toolboxGravitySwitch = root.findViewById(R.id.toolbox_gravity_switch);
        toolboxGravitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FastUiSettings uiSettings = fastRoom.getFastboardView().getUiSettings();
            uiSettings.setToolboxGravity(isChecked ? Gravity.LEFT : Gravity.RIGHT);
        });

        SwitchCompat redoUndoSwitch = root.findViewById(R.id.redo_undo_orientation_switch);
        redoUndoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FastUiSettings uiSettings = fastRoom.getFastboardView().getUiSettings();
            uiSettings.setRedoUndoOrientation(isChecked ? LinearLayoutCompat.VERTICAL : LinearLayoutCompat.HORIZONTAL);
        });

        setupWHTest(root);
    }

    private void setupWHTest(View root) {
        AppCompatSeekBar widthSeekBar = root.findViewById(R.id.change_width);
        widthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    FastboardView fastboardView = fastRoom.getFastboardView();
                    ViewGroup.LayoutParams layoutParams = fastboardView.getLayoutParams();
                    layoutParams.width = progress;
                    fastboardView.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AppCompatSeekBar heightSeekbar = root.findViewById(R.id.change_height);
        heightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    FastboardView fastboardView = fastRoom.getFastboardView();
                    ViewGroup.LayoutParams layoutParams = fastboardView.getLayoutParams();
                    layoutParams.height = progress;
                    fastboardView.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        View mainLayout = getActivity(getContext()).findViewById(R.id.main_layout);
        ViewTreeObserver viewTreeObserver = mainLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {
            int height = mainLayout.getHeight();
            int width = mainLayout.getWidth();
            // widthSeekBar.setProgress(width);
            widthSeekBar.setMax(width);
            // heightSeekbar.setProgress(height);
            heightSeekbar.setMax(height);
        });

        ImageView handle = findViewById(R.id.handle);
        handle.setOnClickListener(v -> {
            boolean isVisiable = controllerLayout.getVisibility() == VISIBLE;
            controllerLayout.setVisibility(isVisiable ? GONE : VISIBLE);
            updateHandleView();
        });
        updateHandleView();
    }

    private void updateHandleView() {
        boolean isVisiable = controllerLayout.getVisibility() == VISIBLE;
        handleView.setImageResource(isVisiable ? R.drawable.ic_toolbox_ext_expanded : R.drawable.ic_toolbox_ext_collapsed);
    }

    public void attachFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    private Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }
        return null;
    }
}
