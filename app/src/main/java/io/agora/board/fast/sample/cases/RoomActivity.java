package io.agora.board.fast.sample.cases;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.WindowRegisterAppParams;
import io.agora.board.fast.FastLogger;
import io.agora.board.fast.FastLogger.Logger;
import io.agora.board.fast.OnRoomReadyCallback;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.Fastboard;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.extension.FastResource;
import io.agora.board.fast.model.FastRegion;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.cases.base.BaseActivity;
import io.agora.board.fast.sample.cases.helper.UiConfig;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.sample.misc.Utils;
import io.agora.board.fast.ui.FastUiSettings;

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
        fastRoom.join(new OnRoomReadyCallback() {
            @Override
            public void onRoomReady(@NonNull FastRoom fastRoom) {
                // register apps
                registerApps(fastRoom);
            }
        });

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

    /**
     * for cn region, use https://netless-app.oss-cn-hangzhou.aliyuncs.com/
     * for other region, use https://cdn.jsdelivr.net/ or https://unpkg.com/
     * <p>
     * @param fastRoom
     */
    private void registerApps(FastRoom fastRoom) {
        List<WindowRegisterAppParams> paramsList = Arrays.asList(
            // Youtube, OnlineVideo etc.
            new WindowRegisterAppParams(
                // https://cdn.jsdelivr.net/npm/@netless/app-plyr@0.1.3/dist/main.iife.js
                // https://unpkg.com/@netless/app-plyr@0.1.3/dist/main.iife.js
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-plyr/0.1.3/dist/main.iife.js",
                "Plyr",
                Collections.emptyMap()
            ),

            // EmbeddedPage app
            new WindowRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-embedded-page/0.1.1/dist/main.iife.js",
                "EmbeddedPage",
                Collections.emptyMap()
            ),

            new WindowRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-monaco/0.1.14-beta.1/dist/main.iife.js",
                "Monaco",
                Collections.emptyMap()
            )
        );

        for (WindowRegisterAppParams params : paramsList) {
            fastRoom.getWhiteSdk().registerApp(params, new Promise<Boolean>() {
                @Override
                public void then(Boolean aBoolean) {
                    // register success
                }

                @Override
                public void catchEx(SDKError t) {
                    // register fail
                }
            });
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fastRoom != null) {
            fastRoom.destroy();
        }
    }
}