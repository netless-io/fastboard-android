package io.agora.board.fast.misc;

import android.content.Context;
import android.provider.Settings;

// provider mock data
public class Repository {
    private static Repository instance;

    private Context context;

    private Repository() {

    }

    public synchronized static Repository get() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public String getUserId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
