package io.agora.board.fast.internal;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import com.google.gson.Gson;

import java.util.Random;

/**
 * @author fenglibin
 */
public class Util {
    private final static Gson gson = new Gson();
    private final static Random random = new Random();

    /**
     * @param object
     * @param classOfT
     * @param <T>
     * @return 返回深拷贝对象
     */
    static <T> T deepCopy(T object, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(object), classOfT);
    }

    public static Activity getActivity(Context context) {
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

    public static boolean isTablet(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        return (configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String toJson(Object args) {
        return gson.toJson(args);
    }


    public static int rgbToColor(int[] rgb) {
        if (rgb == null || rgb.length < 3) {
            return 0xFF123456; // Default color if input is invalid
        }
        return 0xFF000000
                | ((rgb[0] & 0xFF) << 16)
                | ((rgb[1] & 0xFF) << 8)
                | (rgb[2] & 0xFF);
    }

    public static String toHexColorString(int color) {
        return String.format("#%08X", color);
    }

    public static int randomInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be greater than 0");
        }
        return random.nextInt(bound);
    }

}
