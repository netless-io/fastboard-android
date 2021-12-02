package io.agora.board.fast;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

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
        setupFullScreen();
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
        FastStyle fastStyle = new FastStyle().setMainColor(Color.parseColor("#FF6200EE"));
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
        getSupportActionBar().hide();

        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.navigationBars());
        controller.hide(WindowInsetsCompat.Type.statusBars());
        // Some oneplus, huawei devices rely on this line of code for full screen
        controller.setSystemBarsBehavior(BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}