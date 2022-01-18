package io.agora.board.fast.sample;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.herewhite.sdk.domain.SceneState;

import io.agora.board.fast.FastSdk;
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
                activity.getDelegate().setLocalNightMode(
                        isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                );
            }
        });

        root.findViewById(R.id.eraser_view).setOnClickListener(v -> {
            if (fastSdk != null) {
                SceneState sceneState = fastSdk.getFastRoom().getRoom().getRoomState().getSceneState();
                String scenePath = sceneState.getScenePath();
                String dir = scenePath.substring(0, scenePath.lastIndexOf("/"));
                fastSdk.getFastRoom().getRoom().removeScenes(dir);
            }
        });

        SwitchCompat toolboxSwitch = root.findViewById(R.id.toolbox_mode_switch);
        toolboxSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FastUiSettings uiSettings = fastSdk.getFastboardView().getUiSettings();
            uiSettings.setToolboxExpand(isChecked);
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
