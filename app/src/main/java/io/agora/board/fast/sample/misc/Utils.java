package io.agora.board.fast.sample.misc;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.widget.FrameLayout;

import io.agora.board.fast.FastSdk;
import io.agora.board.fast.sample.ControlView;

public class Utils {
    public static int getThemePrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static boolean isDarkMode(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static void setupDevTools(Activity activity, FastSdk fastSdk) {
        try {
            ControlView controlView = new ControlView(activity);
            controlView.attachFastSdk(fastSdk);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

            FrameLayout layout = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            layout.addView(controlView, layoutParams);
        } catch (Exception ignored) {
        }
    }
}
