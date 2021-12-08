package io.agora.board.fast;

import com.google.gson.Gson;

/**
 * @author fenglibin
 */
class Util {
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
