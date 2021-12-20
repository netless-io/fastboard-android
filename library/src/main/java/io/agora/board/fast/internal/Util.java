package io.agora.board.fast.internal;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.google.gson.Gson;

/**
 * @author fenglibin
 */
public class Util {
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
}
