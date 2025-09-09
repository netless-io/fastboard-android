package io.agora.board.fast.sample.cases.flat;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Objects;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.Fastboard;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.OnRoomReadyCallback;
import io.agora.board.fast.model.FastRegion;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.cases.base.BaseActivity;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.sample.misc.Utils;

public class FlatRoomActivity extends BaseActivity {
    private final Repository repository = Repository.get();

    private FastboardView fastboardView;
    private FastRoom fastRoom;

    private CloudFilesController cloudFilesController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_room);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setupWindowInsets();
        setupCloud();
        setupFastboard();
    }

    private void setupCloud() {
        cloudFilesController = findViewById(R.id.cloud_files_controller);
        cloudFilesController.hide();

        View cloudBtn = findViewById(R.id.cloud);
        cloudBtn.setOnClickListener(v -> {
            if (cloudFilesController.isShowing()) {
                cloudFilesController.hide();
            } else {
                cloudFilesController.show();
            }
        });
    }

    private void setupFastboard() {
        fastboardView = findViewById(R.id.fastboard_view);
        Fastboard fastboard = fastboardView.getFastboard();

        FastRoomOptions roomOptions = new FastRoomOptions(
                Constants.SAMPLE_APP_ID,
                getIntent().getStringExtra(Constants.KEY_ROOM_UUID),
                getIntent().getStringExtra(Constants.KEY_ROOM_TOKEN),
                repository.getUserId(),
                FastRegion.CN_HZ
        );

        fastRoom = fastboard.createFastRoom(roomOptions);
        fastRoom.join();

        FlatControllerGroup controller = new FlatControllerGroup(findViewById(R.id.flat_controller_layout));
        // it's a restriction that add controller before setRootRoomController
        controller.addController(cloudFilesController);
        fastRoom.setRootRoomController(controller);

        updateFastStyle();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration config) {
        super.onConfigurationChanged(config);

        updateFastStyle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFullScreen();
    }

    protected void updateFastStyle() {
        // global style change
        FastStyle fastStyle = fastRoom.getFastStyle();
        fastStyle.setDarkMode(Utils.isDarkMode(this));
        fastStyle.setMainColor(Utils.getThemePrimaryColor(this));
        fastRoom.setFastStyle(fastStyle);
    }
}
