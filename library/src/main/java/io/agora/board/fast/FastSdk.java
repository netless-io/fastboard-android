package io.agora.board.fast;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.herewhite.sdk.CommonCallback;
import com.herewhite.sdk.Room;
import com.herewhite.sdk.RoomListener;
import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.SDKError;

import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;
import io.agora.board.fast.model.FastStyle;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

/**
 * @author fenglibin
 */
public class FastSdk {
    private WhiteboardView whiteboardView;
    private Context context;

    private WhiteSdk whiteSdk;
    private FastErrorHandler errorHandler;
    Room room;

    BoardStateObservable observable = new BoardStateObservable();

    private CommonCallback commonCallback = new CommonCallback() {
        @Override
        public void throwError(Object args) {

        }

        @Override
        public void onMessage(JSONObject object) {

        }

        @Override
        public void sdkSetupFail(SDKError error) {
            if (errorHandler != null) {
                errorHandler.onJoinRoomError();
            }
        }

        @Override
        public void onLogger(JSONObject object) {

        }
    };

    private RoomListener roomListener = new RoomListener() {
        private long canUndoSteps;
        private long canRedoSteps;
        
        @Override
        public void onPhaseChanged(RoomPhase phase) {
            observable.notifyRoomPhaseChanged(phase);
        }

        @Override
        public void onDisconnectWithError(Exception e) {

        }

        @Override
        public void onKickedWithReason(String reason) {

        }

        @Override
        public void onRoomStateChanged(RoomState modifyState) {
            notifyRoomState(modifyState);
        }

        @Override
        public void onCanUndoStepsUpdate(long canUndoSteps) {

        }

        @Override
        public void onCanRedoStepsUpdate(long canRedoSteps) {

        }

        @Override
        public void onCatchErrorWhenAppendFrame(long userId, Exception error) {

        }
    };

    private Promise<Room> joinRoomPromise = new Promise<Room>() {
        @Override
        public void then(Room room) {
            FastLogger.info("join room success" + room.toString());
            FastSdk.this.room = room;
            notifyRoomState(room.getRoomState());
        }

        @Override
        public void catchEx(SDKError t) {
            FastLogger.error("join room error", t);
            if (errorHandler != null) {
                errorHandler.onJoinRoomError();
            }
        }
    };

    private void notifyRoomState(RoomState roomState) {
        if (roomState.getBroadcastState() != null) {
            observable.notifyBroadcastStateChanged(roomState.getBroadcastState());
        }
        if (roomState.getMemberState() != null) {
            observable.notifyMemberStateChanged(roomState.getMemberState());
        }
    }

    public FastSdk(FastBoardView fastBoardView) {
        whiteboardView = fastBoardView.whiteboardView;
        context = whiteboardView.getContext();
    }

    void initSdk(FastSdkOptions options) {
        WhiteSdkConfiguration conf = new WhiteSdkConfiguration(options.getAppId(), true);
        whiteSdk = new WhiteSdk(whiteboardView, context, conf, commonCallback);
    }

    public void joinRoom(FastRoomOptions options) {
        if (whiteSdk == null) {
            FastLogger.error("sdk not init, call initSdk first");
            return;
        }
        RoomParams params = new RoomParams(options.getUuid(), options.getToken(), options.getUid());
        whiteSdk.joinRoom(params, roomListener, joinRoomPromise);
    }

    public void setErrorHandler(FastErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void destroy() {
        whiteboardView.removeAllViews();
        whiteboardView.destroy();
    }

    public Room getRoom() {
        return room;
    }

    public void setColor(Integer color) {
        MemberState memberState = new MemberState();
        memberState.setStrokeColor(new int[]{
                color >> 16 & 0xff,
                color >> 8 & 0xff,
                color & 0xff,
        });
        room.setMemberState(memberState);
    }

    public void setAppliance(String appliance) {
        MemberState memberState = new MemberState();
        memberState.setCurrentApplianceName(appliance);
        room.setMemberState(memberState);
    }

    public void setStokeWidth(int width) {
        MemberState memberState = new MemberState();
        memberState.setStrokeWidth(width);
        room.setMemberState(memberState);
    }

    public void cleanScene() {
        room.cleanScene(true);
    }

    public void registerObserver(BoardStateObserver observer) {
        observable.registerObserver(observer);
    }

    public void unregisterObserver(BoardStateObserver observer) {
        observable.unregisterObserver(observer);
    }

    public void setFastStyle(FastStyle style) {
        observable.notifyGlobalStyleChanged(style);
    }

    public static class DefaultErrorHandler implements FastErrorHandler {
        private Activity activity;

        public DefaultErrorHandler(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onJoinRoomError() {
            showExitDialog();
        }

        private void showExitDialog() {
            AlertDialog dialog = new MaterialAlertDialogBuilder(activity)
                    .setTitle("Exit?")
                    .setMessage("join room error, click yes and reenter")
                    .setPositiveButton("Yes", (d, which) -> activity.finish())
                    .create();
            dialog.getWindow().setFlags(FLAG_NOT_FOCUSABLE, FLAG_NOT_FOCUSABLE);
            dialog.show();
            dialog.getWindow().clearFlags(FLAG_NOT_FOCUSABLE);
            hideBars(dialog.getWindow());
        }

        private void hideBars(Window window) {
            if (window != null) {
                WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(window, window.getDecorView());
                controller.hide(WindowInsetsCompat.Type.navigationBars());
                controller.hide(WindowInsetsCompat.Type.statusBars());
                controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }
}
