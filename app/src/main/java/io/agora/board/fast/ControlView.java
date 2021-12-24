package io.agora.board.fast;

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

        SwitchCompat switchButton = root.findViewById(R.id.dark_mode_switch);
        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (fastSdk != null) {
                AppCompatActivity activity = (AppCompatActivity) getActivity(context);
                activity.getDelegate().setLocalNightMode(
                        isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                );
            }
        });

        root.findViewById(R.id.clear_scenes).setOnClickListener(v -> {
            if (fastSdk != null) {
                SceneState sceneState = fastSdk.getFastRoom().getRoom().getRoomState().getSceneState();
                String scenePath = sceneState.getScenePath();
                String dir = scenePath.substring(0, scenePath.lastIndexOf("/"));
                fastSdk.getFastRoom().getRoom().removeScenes(dir);
            }
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
