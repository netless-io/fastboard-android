package io.agora.board.fast.sample.cases;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.Fastboard;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastRegion;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.cases.base.BaseActivity;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.ui.FastUiSettings;

public class QuickStartActivity extends BaseActivity {
    private final Repository repository = Repository.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_start);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setupToolBox();
        setupFastboard();
    }

    private void setupToolBox() {
        // config toolbox appliances
        ArrayList<List<FastAppliance>> config = new ArrayList<>();
        config.add(Arrays.asList(FastAppliance.CLICKER));
        config.add(Arrays.asList(FastAppliance.SELECTOR));
        config.add(Arrays.asList(FastAppliance.PENCIL));
        config.add(Arrays.asList(FastAppliance.TEXT));
        config.add(Arrays.asList(FastAppliance.ERASER));
        config.add(Arrays.asList(
                FastAppliance.RECTANGLE,
                FastAppliance.ELLIPSE,
                FastAppliance.STRAIGHT,
                FastAppliance.ARROW,
                FastAppliance.PENTAGRAM,
                FastAppliance.RHOMBUS,
                FastAppliance.BUBBLE,
                FastAppliance.TRIANGLE
        ));
        config.add(Arrays.asList(FastAppliance.OTHER_CLEAR));
        FastUiSettings.setToolsExpandAppliances(config);
    }

    private void setupFastboard() {
        // step 1
        FastboardView fastboardView = findViewById(R.id.fastboard_view);
        // step 2: obtain fastboard
        Fastboard fastboard = fastboardView.getFastboard();
        // step 3: join room
        FastRoomOptions roomOptions = new FastRoomOptions(
                Constants.SAMPLE_APP_ID,
                getIntent().getStringExtra(Constants.KEY_ROOM_UUID),
                getIntent().getStringExtra(Constants.KEY_ROOM_TOKEN),
                repository.getUserId(),
                FastRegion.CN_HZ
        );
        FastRoom fastRoom = fastboard.createFastRoom(roomOptions);
        fastRoom.join();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFullScreen();
    }
}