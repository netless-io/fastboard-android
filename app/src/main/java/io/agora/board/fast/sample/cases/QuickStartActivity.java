package io.agora.board.fast.sample.cases;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Objects;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.Fastboard;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.OnRoomReadyCallback;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.cases.base.BaseActivity;
import io.agora.board.fast.sample.misc.Repository;

public class QuickStartActivity extends BaseActivity {
    private final Repository repository = Repository.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_start);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setupFastboard();
    }

    private void setupFastboard() {
        // step 1
        FastboardView fastboardView = findViewById(R.id.fastboard_view);
        // step 2: obtain fastboard
        Fastboard fastboard = fastboardView.getFastboard();

        FastRoomOptions roomOptions = new FastRoomOptions(
                Constants.SAMPLE_APP_ID,
                getIntent().getStringExtra(Constants.KEY_ROOM_UUID),
                getIntent().getStringExtra(Constants.KEY_ROOM_TOKEN),
                repository.getUserId()
        );
        // step 3: join room
        fastboard.joinRoom(roomOptions, new OnRoomReadyCallback() {
            @Override
            public void onRoomReady(FastRoom fastRoom) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFullScreen();
    }
}