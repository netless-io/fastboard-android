package io.agora.board.fast;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;

/**
 * @author fenglibin
 */
class Util {
    private static final String META_DATA_KEY = "io.agora.board.APP_ID";

    static String getAppIdFromMeta(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA
            );
            if (info.metaData != null && info.metaData.getString(META_DATA_KEY) != null) {
                return info.metaData.getString(META_DATA_KEY);
            } else {
                return null;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private final static Gson gson = new Gson();

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
}
