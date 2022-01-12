package io.agora.board.fast.sample.cases.drawsth;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import io.agora.board.fast.FastSdk;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.cases.base.BaseActivity;
import io.agora.board.fast.sample.misc.Repository;

public class DrawSthActivity extends BaseActivity {

    private FastboardView fastboardView;
    private FastSdk fastSdk;
    private Repository repository = Repository.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_sth);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupFastboard();
    }

    private void setupFastboard() {
        fastboardView = findViewById(R.id.fastboard_view);

        FastSdkOptions fastSdkOptions = new FastSdkOptions(Constants.SAMPLE_APP_ID);
        fastSdk = fastboardView.obtainFastSdk(fastSdkOptions);

        FastRoomOptions roomOptions = new FastRoomOptions(
                getIntent().getStringExtra(Constants.KEY_ROOM_UUID),
                getIntent().getStringExtra(Constants.KEY_ROOM_TOKEN),
                repository.getUserId()
        );
        fastSdk.joinRoom(roomOptions);

        DrawSthController controller = new DrawSthController(findViewById(R.id.draw_sth_controller));
        controller.setupView();

        fastboardView.setRootRoomController(controller);
    }
}