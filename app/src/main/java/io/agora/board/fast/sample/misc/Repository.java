package io.agora.board.fast.sample.misc;

import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.sample.Constants;

/**
 * provider mock data
 */
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

    public List<TestCase> getTestCases() {
        ArrayList<TestCase> result = new ArrayList<>();
        result.add(new TestCase(
                "Get Started",
                "start fastboard with a simple",
                new TestCase.RoomInfo(Constants.SIMPLE_ROOM_UUID, Constants.SIMPLE_ROOM_TOKEN, true)
        ));
        return result;
    }
}
