package io.agora.board.fast.sample.cases;

import static androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.Objects;

import io.agora.board.fast.FastSdk;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.sample.Constants;
import io.agora.board.fast.sample.ControlView;
import io.agora.board.fast.sample.R;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.ui.FastUiSettings;

/**
 * @author fenglibin
 */
public class RoomActivity extends AppCompatActivity {

    private FastSdk fastSdk;
    private Repository repository = Repository.get();

    private static int getThemePrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setupFullScreen();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setupFastBoard();
        setupController();
    }

    private void setupFastBoard() {
        FastboardView fastBoardView = findViewById(R.id.fast_board_view);
        // create fastSdk
        FastSdkOptions fastSdkOptions = new FastSdkOptions(Constants.SIMPLE_APP_ID);
        fastSdk = fastBoardView.obtainFastSdk(fastSdkOptions);

        // join room
        FastRoomOptions roomOptions = new FastRoomOptions(
                Constants.SIMPLE_ROOM_UUID,
                Constants.SIMPLE_ROOM_TOKEN,
                repository.getUserId());
        fastSdk.joinRoom(roomOptions);

        // global style change
        FastStyle fastStyle = fastSdk.getFastStyle();
        fastStyle.setDarkMode(isDarkMode());
        fastStyle.setMainColor(getThemePrimaryColor(this));
        fastSdk.setFastStyle(fastStyle);

        // change ui
        FastUiSettings uiSettings = fastBoardView.getUiSettings();
        uiSettings.setToolboxGravity(Gravity.RIGHT);
    }

    private void setupController() {
        ControlView controlView = findViewById(R.id.control_view);
        controlView.attachFastSdk(fastSdk);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration config) {
        super.onConfigurationChanged(config);

        updateDayNightStyle(config);
    }

    private boolean isDarkMode() {
        int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    private void updateDayNightStyle(@NonNull Configuration config) {
        int nightMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        FastStyle fastStyle = fastSdk.getFastStyle();
        fastStyle.setDarkMode(nightMode == Configuration.UI_MODE_NIGHT_YES);
        fastSdk.setFastStyle(fastStyle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fastSdk != null) {
            fastSdk.destroy();
        }
    }

    private void setupFullScreen() {
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.navigationBars());
        controller.hide(WindowInsetsCompat.Type.statusBars());
        controller.setSystemBarsBehavior(BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }
}