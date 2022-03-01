package io.agora.board.fast.sample.cases;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Objects;

import io.agora.board.fast.FastSdk;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
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
        // step 2: obtain fastSdk
        FastSdkOptions fastSdkOptions = new FastSdkOptions(Constants.SAMPLE_APP_ID);
        FastSdk fastSdk = fastboardView.getFastSdk(fastSdkOptions);

        // step 3: join room
        FastRoomOptions roomOptions = new FastRoomOptions(
                getIntent().getStringExtra(Constants.KEY_ROOM_UUID),
                getIntent().getStringExtra(Constants.KEY_ROOM_TOKEN),
                repository.getUserId());
        fastSdk.joinRoom(roomOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFullScreen();
    }
}