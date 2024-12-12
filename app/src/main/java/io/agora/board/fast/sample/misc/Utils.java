package io.agora.board.fast.sample.misc;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.sample.RoomControlView;

public class Utils {
    private static Gson gson = new Gson();

    public static int getThemePrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static boolean isDarkMode(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static void setupDevTools(Activity activity, FastRoom fastRoom) {
        try {
            RoomControlView controlView = new RoomControlView(activity);
            controlView.attachFastRoom(fastRoom);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

            FrameLayout layout = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            layout.addView(controlView, layoutParams);
        } catch (Exception ignored) {
        }
    }

    public static boolean isPhone(Context context) {
        return !isTablet(context);
    }

    public static boolean isTablet(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        return (configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

    public static String getStringFromAsserts(Context context, String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = context.getAssets().open(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        }

        return sb.toString();
    }
}
