package io.agora.board.fast;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;

import static androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;

/**
 * @author fenglibin
 */
public class RoomActivity extends AppCompatActivity {

    private FastSdk fastSdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        setupFastBoard();
    }

    private void setupFastBoard() {
        FastBoardView fastBoardView = findViewById(R.id.fast_board_view);
        // create fastSdk
        FastSdkOptions fastSdkOptions = new FastSdkOptions(Constants.SIMPLE_APP_ID);
        fastSdk = fastBoardView.obtainFastSdk(fastSdkOptions);

        // join room
        FastRoomOptions roomOptions = new FastRoomOptions(Constants.SIMPLE_ROOM_UUID, Constants.SIMPLE_ROOM_TOKEN, Constants.SIMPLE_UID);
        fastSdk.joinRoom(roomOptions);

        // set error handler
        fastSdk.setErrorHandler(new FastSdk.DefaultErrorHandler(this));

        // global style change
        FastStyle fastStyle = new FastStyle();
        fastStyle.setMainColor(getThemePrimaryColor(this));
        fastSdk.setFastStyle(fastStyle);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration config) {
        super.onConfigurationChanged(config);
        int currentNightMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
            case Configuration.UI_MODE_NIGHT_YES:
                FastStyle fastStyle = new FastStyle();
                fastStyle.setMainColor(getThemePrimaryColor(this));
                fastSdk.setFastStyle(fastStyle);
                break;
            default:
                break;
        }
    }

    private static int getThemePrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setupFullScreen();
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
        getSupportActionBar().hide();

        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.navigationBars());
        controller.hide(WindowInsetsCompat.Type.statusBars());
        // Some oneplus, huawei devices rely on this line of code for full screen
        controller.setSystemBarsBehavior(BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}