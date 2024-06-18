package io.agora.board.fast.sample.cases;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.Gravity;

import androidx.annotation.NonNull;

import io.agora.board.fast.FastLogger;
import io.agora.board.fast.FastLogger.Logger;
import io.agora.board.fast.FastRoom;
import io.agora.board.fast.FastRoomListener;
import io.agora.board.fast.Fastboard;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.extension.FastResource;
import io.agora.board.fast.model.FastRegion;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.cases.base.BaseActivity;
import io.agora.board.fast.sample.cases.helper.RoomOperationsKT;
import io.agora.board.fast.sample.cases.helper.UiConfig;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.sample.misc.Utils;
import io.agora.board.fast.ui.FastUiSettings;

import java.util.Objects;

/**
 * @author fenglibin
 */
public class RoomActivity extends BaseActivity {

    private final Repository repository = Repository.get();

    private FastRoom fastRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        UiConfig.setupSampleCollapseAppliances();
        setupFastboard();
        preloadWebViewOnIdle();
    }

    private void setupFastboard() {
        FastboardView fastboardView = findViewById(R.id.fastboard_view);
        // create fastSdk
        Fastboard fastboard = fastboardView.getFastboard();

        // join room
        FastRoomOptions roomOptions = new FastRoomOptions(
                Constants.SAMPLE_APP_ID,
                getIntent().getStringExtra(Constants.KEY_ROOM_UUID),
                getIntent().getStringExtra(Constants.KEY_ROOM_TOKEN),
                repository.getUserId(),
                FastRegion.CN_HZ
        );
        fastRoom = fastboard.createFastRoom(roomOptions);
        fastRoom.setResource(new FastResource() {
            @Override
            public int getBackgroundColor(boolean darkMode) {
                return Color.TRANSPARENT;
            }
        });
        fastRoom.addListener(new FastRoomListener() {
            @Override
            public void onRoomReadyChanged(FastRoom fastRoom) {
                if (fastRoom.isReady()) {
                    // register apps
                    new RoomOperationsKT(fastRoom).registerApps();
                }
            }
        });
        fastRoom.join();

        // global style change
        FastStyle fastStyle = fastRoom.getFastStyle();
        fastStyle.setDarkMode(Utils.isDarkMode(this));
        fastStyle.setMainColor(Utils.getThemePrimaryColor(this));
        fastRoom.setFastStyle(fastStyle);

        // change ui
        FastUiSettings uiSettings = fastboardView.getUiSettings();
        uiSettings.setToolboxGravity(Gravity.RIGHT);

        // dev tools display
        Utils.setupDevTools(this, fastRoom);

        // set custom logger
        FastLogger.setLogger(new Logger() {
            @Override
            public void debug(String msg) {

            }

            @Override
            public void info(String msg) {

            }

            @Override
            public void warn(String msg) {

            }

            @Override
            public void error(String msg) {

            }

            @Override
            public void error(String msg, Throwable throwable) {

            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration config) {
        super.onConfigurationChanged(config);

        updateDayNightStyle(config);
    }

    private void updateDayNightStyle(@NonNull Configuration config) {
        int nightMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        FastStyle fastStyle = fastRoom.getFastStyle();
        fastStyle.setDarkMode(nightMode == Configuration.UI_MODE_NIGHT_YES);
        fastRoom.setFastStyle(fastStyle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFullScreen();
    }

    private void preloadWebViewOnIdle() {
        Looper.myQueue().addIdleHandler(() -> {
            Fastboard.preloadWhiteboardView();
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fastRoom != null) {
            fastRoom.destroy();
        }
    }
}