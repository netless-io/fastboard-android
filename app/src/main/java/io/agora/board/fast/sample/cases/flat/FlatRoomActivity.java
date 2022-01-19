package io.agora.board.fast.sample.cases.flat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Objects;

import io.agora.board.fast.FastSdk;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.cases.base.BaseActivity;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.sample.misc.Utils;

public class FlatRoomActivity extends BaseActivity {
    private final Repository repository = Repository.get();

    private FastSdk fastSdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_room);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setupFastboard();
    }

    private void setupFastboard() {
        FastboardView fastboardView = findViewById(R.id.fastboard_view);

        FastSdkOptions fastSdkOptions = new FastSdkOptions(Constants.SAMPLE_APP_ID);
        fastSdk = fastboardView.getFastSdk(fastSdkOptions);

        FastRoomOptions roomOptions = new FastRoomOptions(
                getIntent().getStringExtra(Constants.KEY_ROOM_UUID),
                getIntent().getStringExtra(Constants.KEY_ROOM_TOKEN),
                repository.getUserId()
        );
        fastSdk.joinRoom(roomOptions);

        FlatControllerGroup controller = new FlatControllerGroup(findViewById(R.id.flat_controller_layout));
        controller.setupView();
        fastboardView.setRootRoomController(controller);

        updateFastStyle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFullScreen();
    }

    protected void updateFastStyle() {
        // global style change
        FastStyle fastStyle = fastSdk.getFastStyle();
        fastStyle.setDarkMode(Utils.isDarkMode(this));
        fastStyle.setMainColor(Utils.getThemePrimaryColor(this));
        fastSdk.setFastStyle(fastStyle);
    }
}
