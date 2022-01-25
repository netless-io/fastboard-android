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
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;

import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.FastSdk;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.ui.FastUiSettings;

public class ControlView extends FrameLayout {
    private FastSdk fastSdk;

    public ControlView(@NonNull Context context) {
        this(context, null);
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_control_view, this, true);

        SwitchCompat darkModeSwitch = root.findViewById(R.id.dark_mode_switch);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (fastSdk != null) {
                AppCompatActivity activity = (AppCompatActivity) getActivity(context);
                activity.getDelegate().setLocalNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        root.findViewById(R.id.clear).setOnClickListener(v -> {
            SceneState sceneState = fastSdk.getFastRoom().getRoom().getRoomState().getSceneState();
            String scenePath = sceneState.getScenePath();
            String dir = scenePath.substring(0, scenePath.lastIndexOf("/"));
            fastSdk.getFastRoom().getRoom().removeScenes(dir);
        });

        SwitchCompat toolboxSwitch = root.findViewById(R.id.toolbox_mode_switch);
        toolboxSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FastUiSettings uiSettings = fastSdk.getFastboardView().getUiSettings();
            uiSettings.setToolboxExpand(!isChecked);
        });

        SwitchCompat toolboxGravitySwitch = root.findViewById(R.id.toolbox_gravity_switch);
        toolboxGravitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FastUiSettings uiSettings = fastSdk.getFastboardView().getUiSettings();
            uiSettings.setToolboxGravity(isChecked ? Gravity.LEFT : Gravity.RIGHT);
        });

        SwitchCompat redoUndoSwitch = root.findViewById(R.id.redo_undo_orientation_switch);
        redoUndoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FastUiSettings uiSettings = fastSdk.getFastboardView().getUiSettings();
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
                    FastboardView fastboardView = fastSdk.getFastboardView();
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
                    FastboardView fastboardView = fastSdk.getFastboardView();
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
    }

    public void attachFastSdk(FastSdk fastSdk) {
        this.fastSdk = fastSdk;
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
