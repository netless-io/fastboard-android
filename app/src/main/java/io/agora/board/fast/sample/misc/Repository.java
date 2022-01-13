package io.agora.board.fast.sample.misc;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.cases.GetStartActivity;
import io.agora.board.fast.sample.cases.RoomActivity;
import io.agora.board.fast.sample.cases.drawsth.DrawSthActivity;

/**
 * a singleton class to provider mock data
 *
 * @author fenglibin
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

    public void getRemoteData(int delay, Callback callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> callback.onSuccess(new Object()), delay);
    }

    public List<TestCase> getTestCases() {
        ArrayList<TestCase> result = new ArrayList<>();
        result.add(
                new TestCase(
                        "Get Started",
                        "Start fastboard with a sample",
                        GetStartActivity.class,
                        new TestCase.RoomInfo(Constants.SAMPLE_ROOM_UUID, Constants.SAMPLE_ROOM_TOKEN, true)
                ));

        result.add(
                new TestCase(
                        "Draw Something",
                        "Use fastboard in case with several people",
                        DrawSthActivity.class,
                        new TestCase.RoomInfo(Constants.SAMPLE_ROOM_UUID, Constants.SAMPLE_ROOM_TOKEN, true)
                ));


        result.add(
                new TestCase(
                        "Main Room",
                        "Test api by multiple api call",
                        RoomActivity.class,
                        new TestCase.RoomInfo(Constants.SAMPLE_ROOM_UUID, Constants.SAMPLE_ROOM_TOKEN, true)
                ));
        return result;
    }

    interface Callback {
        void onSuccess(Object object);

        void onFailure(Exception e);
    }
}
