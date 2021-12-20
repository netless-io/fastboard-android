package io.agora.board.fast.internal;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

import android.app.Activity;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.agora.board.fast.FastException;
import io.agora.board.fast.library.R;

public class DefaultErrorHandler implements FastErrorHandler {
    private final Activity activity;

    public DefaultErrorHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onJoinRoomError(FastException e) {
        if (activity == null) {
            return;
        }

        showExitDialog(
                activity.getString(R.string.fast_default_exit_title),
                activity.getString(R.string.fast_default_exit_room_message),
                activity.getString(R.string.fast_default_exit_ok)
        );
    }

    @Override
    public void onJoinPlayerError(FastException e) {
        if (activity == null) {
            return;
        }

        showExitDialog(
                activity.getString(R.string.fast_default_exit_title),
                activity.getString(R.string.fast_default_exit_player_message),
                activity.getString(R.string.fast_default_exit_ok)
        );
    }

    /**
     * default handle exit activity
     */
    protected void showExitDialog(String title, String message, String button) {
        AlertDialog dialog = new MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(button, (d, which) -> activity.finish())
                .create();
        // enter full screen
        dialog.getWindow().setFlags(FLAG_NOT_FOCUSABLE, FLAG_NOT_FOCUSABLE);
        dialog.show();
        dialog.getWindow().clearFlags(FLAG_NOT_FOCUSABLE);
        hideBars(dialog.getWindow());
    }

    private void hideBars(Window window) {
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(window, window.getDecorView());
        controller.hide(WindowInsetsCompat.Type.navigationBars());
        controller.hide(WindowInsetsCompat.Type.statusBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }
}