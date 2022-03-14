package io.agora.board.fast.sample.cases;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.NonNull;

import java.util.Objects;

import io.agora.board.fast.Fastboard;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.model.FastRegion;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.cases.base.BaseActivity;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.sample.misc.Utils;
import io.agora.board.fast.ui.FastUiSettings;

/**
 * @author fenglibin
 */
public class RoomActivity extends BaseActivity {

    private final Repository repository = Repository.get();
    private Fastboard fastboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setupFastboard();
    }

    private void setupFastboard() {
        FastboardView fastboardView = findViewById(R.id.fastboard_view);
        // create fastSdk
        fastboard = fastboardView.getFastboard();

        // join room
        FastRoomOptions roomOptions = new FastRoomOptions(
                Constants.SAMPLE_APP_ID,
                getIntent().getStringExtra(Constants.KEY_ROOM_UUID),
                getIntent().getStringExtra(Constants.KEY_ROOM_TOKEN),
                repository.getUserId(),
                FastRegion.CN_HZ
        );
        fastboard.joinRoom(roomOptions);

        // global style change
        FastStyle fastStyle = fastboard.getFastStyle();
        fastStyle.setDarkMode(Utils.isDarkMode(this));
        fastStyle.setMainColor(Utils.getThemePrimaryColor(this));
        fastboard.setFastStyle(fastStyle);

        // change ui
        FastUiSettings uiSettings = fastboardView.getUiSettings();
        uiSettings.setToolboxGravity(Gravity.RIGHT);

        // dev tools display
        Utils.setupDevTools(this, fastboard);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration config) {
        super.onConfigurationChanged(config);

        updateDayNightStyle(config);
    }

    private void updateDayNightStyle(@NonNull Configuration config) {
        int nightMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        FastStyle fastStyle = fastboard.getFastStyle();
        fastStyle.setDarkMode(nightMode == Configuration.UI_MODE_NIGHT_YES);
        fastboard.setFastStyle(fastStyle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFullScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fastboard != null) {
            fastboard.destroy();
        }
    }
}