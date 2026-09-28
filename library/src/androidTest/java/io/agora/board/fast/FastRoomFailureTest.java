package io.agora.board.fast;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.herewhite.sdk.CommonCallback;
import com.herewhite.sdk.DeferredRoom;
import com.herewhite.sdk.Room;
import com.herewhite.sdk.RoomListener;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.WindowRegisterAppParams;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.agora.board.fast.extension.FastResult;
import io.agora.board.fast.model.FastRegion;
import io.agora.board.fast.model.FastRegisterAppParams;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.ui.ErrorHandleLayout;
import io.agora.board.fast.internal.RoomLifecycle;

@RunWith(AndroidJUnit4.class)
public class FastRoomFailureTest {
    private FastboardView view;
    private FastRoom room;
    private CommonCallback callback;
    private final AtomicInteger errors = new AtomicInteger();

    private void main(Runnable action) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(action);
    }

    private static Object field(Object object, String name) {
        try {
            Field field = FastRoom.class.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) { throw new AssertionError(e); }
    }

    @Before public void setup() {
        main(() -> {
            Context context = new ContextThemeWrapper(InstrumentationRegistry.getInstrumentation().getTargetContext(), androidx.appcompat.R.style.Theme_AppCompat);
            view = new FastboardView(context);
            room = view.getFastboard().createFastRoom(new FastRoomOptions("test", "test", "test", "test", FastRegion.CN_HZ));
            callback = (CommonCallback) field(room, "commonCallback");
            room.setCommonCallback(new CommonCallback() {
                @Override public void sdkSetupFail(SDKError error) {
                    assertSame(Looper.getMainLooper(), Looper.myLooper());
                    errors.incrementAndGet();
                }
            });
        });
    }

    @After public void cleanup() { main(() -> room.destroy()); }

    @Test public void backgroundSetupFailureIsOnMainAndDoesNotOfferImpossibleRetry() {
        assertNotSame(Looper.getMainLooper(), Looper.myLooper());
        callback.sdkSetupFail(new SDKError("injected failure"));
        main(() -> {
            assertTrue(room.requiresRecreation());
            assertFalse(room.canRetryJoin());
            assertEquals(1, errors.get());
            assertEquals(View.GONE, view.findViewById(R.id.fast_error_handle_retry).getVisibility());
            TextView message = view.findViewById(R.id.fast_error_handle_message);
            assertEquals(view.getContext().getString(R.string.fast_default_reopen_required), message.getText().toString());
        });
        callback.sdkSetupFail(new SDKError("duplicate failure"));
        main(() -> assertEquals(1, errors.get()));
    }

    @Test public void queuedFailureAfterDestroyDoesNotNotifyOrChangeUi() {
        main(() -> {
            Thread worker = new Thread(() -> callback.sdkSetupFail(new SDKError("late failure")));
            worker.start();
            try { worker.join(); } catch (InterruptedException e) { throw new AssertionError(e); }
            room.destroy(); // posted failure has not run yet
        });
        main(() -> assertEquals(0, errors.get()));
    }

    @Test public void retryIsStillAvailableForRecoverableIdleRoom() {
        main(() -> {
            ErrorHandleLayout layout = view.findViewById(R.id.fast_error_handle_layout);
            layout.showRetry(R.string.fast_default_room_error_message);
            assertTrue(room.canRetryJoin());
            assertEquals(View.VISIBLE, view.findViewById(R.id.fast_error_handle_retry).getVisibility());
        });
    }

    @Test public void registerIsCancelledOnceAndIgnoresLateSuccess() {
        main(() -> {
            // Keep the actual FastRoom wrapper and WebView, control only SDK response timing.
            class DeferredSdk extends WhiteSdk {
                Promise<Boolean> pending;
                DeferredSdk() { super(view.getWhiteboardView(), view.getContext(),
                    new com.herewhite.sdk.WhiteSdkConfiguration("test"), callback); }
                @Override public void registerApp(WindowRegisterAppParams params, Promise<Boolean> result) { pending = result; }
            }
            DeferredSdk sdk = new DeferredSdk();
            try {
                Field f = FastRoom.class.getDeclaredField("whiteSdk");
                f.setAccessible(true);
                f.set(room, sdk);
            } catch (Exception e) { throw new AssertionError(e); }
            AtomicInteger results = new AtomicInteger();
            room.registerApp(new FastRegisterAppParams("", "test", "test", null), new FastResult<Boolean>() {
                @Override public void onSuccess(Boolean value) { fail("success after destroy"); }
                @Override public void onError(Exception error) {
                    assertSame(Looper.getMainLooper(), Looper.myLooper());
                    assertEquals(FastException.ROOM_OPERATION_CANCELLED, ((FastException) error).getCode());
                    results.incrementAndGet();
                    room.destroy(); // reentrant cancellation
                }
            });
            room.destroy();
            assertEquals(1, results.get());
            sdk.pending.then(true);
            sdk.pending.catchEx(new SDKError("late failure"));
            assertEquals(1, results.get());
        });
    }

    @SuppressWarnings("unchecked")
    private DeferredRoom acceptJoin() {
        RoomLifecycle lifecycle = (RoomLifecycle) field(room, "lifecycle");
        assertTrue(lifecycle.beginJoin());
        ErrorHandleLayout layout = view.findViewById(R.id.fast_error_handle_layout);
        room.join(); // duplicate JOINING request displays a busy error
        assertEquals(View.VISIBLE, layout.getVisibility());
        DeferredRoom nativeRoom = new DeferredRoom();
        ((Promise<Room>) field(room, "joinRoomPromise")).then(nativeRoom);
        assertTrue(room.isReady());
        assertEquals(View.GONE, layout.getVisibility());
        return nativeRoom;
    }

    @Test public void joinSuccessHidesEarlierBusyError() { main(this::acceptJoin); }

    @Test public void duplicateJoinCannotCoverAnAlreadyConnectedRoom() {
        main(() -> {
            acceptJoin();
            room.join();
            assertTrue(room.isReady());
            assertEquals(View.GONE, view.findViewById(R.id.fast_error_handle_layout).getVisibility());
        });
    }

    @Test public void connectingCallbackCanCancelAndRecoverRetryUi() {
        main(() -> {
            room.addListener(new FastRoomListener() {
                @Override public void onRoomPhaseChanged(com.herewhite.sdk.domain.RoomPhase phase) {
                    if (phase == com.herewhite.sdk.domain.RoomPhase.connecting) {
                        room.join(); // displays busy while the first join is pending
                        room.disconnect();
                    }
                }
            });
            room.join();
            assertTrue(room.canRetryJoin());
            assertEquals(View.VISIBLE, view.findViewById(R.id.fast_error_handle_retry).getVisibility());
        });
    }

    @Test public void completedDisconnectRestoresRetryAfterBusyError() {
        main(() -> {
            DeferredRoom nativeRoom = acceptJoin();
            ((RoomListener) field(room, "roomListener")).onDisconnectWithError(new Exception("injected disconnect"));
            assertEquals(View.GONE, view.findViewById(R.id.fast_error_handle_retry).getVisibility());
            nativeRoom.pendingLeave.then(null);
            assertTrue(room.canRetryJoin());
            assertEquals(View.VISIBLE, view.findViewById(R.id.fast_error_handle_retry).getVisibility());
        });
    }

    @Test public void failedDisconnectReplacesBusyWithReopenMessage() {
        main(() -> {
            DeferredRoom nativeRoom = acceptJoin();
            ((RoomListener) field(room, "roomListener")).onDisconnectWithError(new Exception("injected disconnect"));
            nativeRoom.pendingLeave.catchEx(new SDKError("injected cleanup failure"));
            assertTrue(room.requiresRecreation());
            assertEquals(View.GONE, view.findViewById(R.id.fast_error_handle_retry).getVisibility());
            TextView message = view.findViewById(R.id.fast_error_handle_message);
            assertEquals(view.getContext().getString(R.string.fast_default_reopen_required), message.getText().toString());
        });
    }
}
