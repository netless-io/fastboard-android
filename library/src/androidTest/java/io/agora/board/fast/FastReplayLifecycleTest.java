package io.agora.board.fast;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.herewhite.sdk.CommonCallback;
import com.herewhite.sdk.Player;
import com.herewhite.sdk.PlayerListener;
import com.herewhite.sdk.RecordingPlayer;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.domain.PlayerConfiguration;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.SDKError;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import io.agora.board.fast.model.FastReplayOptions;

@RunWith(AndroidJUnit4.class)
public class FastReplayLifecycleTest {
    private FastboardView view;
    private FastReplay replay;
    private DeferredSdk sdk;
    private boolean destroyed;

    private void main(Runnable action) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(action);
    }

    private static Object field(Object target, String name) {
        try {
            Field field = FastReplay.class.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception error) {
            throw new AssertionError(error);
        }
    }

    private static void setField(Object target, String name, Object value) {
        try {
            Field field = FastReplay.class.getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception error) {
            throw new AssertionError(error);
        }
    }

    @Before public void setup() {
        main(() -> {
            Context context = new ContextThemeWrapper(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                androidx.appcompat.R.style.Theme_AppCompat);
            view = new FastboardView(context);
            replay = view.getFastboard().createFastReplay(new FastReplayOptions("test", "test", "test"));
            sdk = new DeferredSdk(view, context);
            setField(replay, "whiteSdk", sdk);
        });
    }

    @After public void cleanup() {
        main(() -> {
            if (!destroyed) replay.destroy();
        });
    }

    @Test public void lateCreateResultCannotReviveDestroyedReplay() {
        main(() -> {
            AtomicInteger callbacks = new AtomicInteger();
            replay.join(value -> callbacks.incrementAndGet());
            Promise<Player> pending = sdk.pending.get(0);
            replay.destroy();
            destroyed = true;
            pending.then(new RecordingPlayer());
            assertFalse(replay.isReady());
            assertNull(replay.getPlayer());
            assertEquals(0, callbacks.get());
            assertEquals(1, sdk.releaseCalls);
        });
    }

    @Test public void listenerDestroySuppressesLaterReadyCallback() {
        main(() -> {
            AtomicInteger callbacks = new AtomicInteger();
            AtomicInteger laterListeners = new AtomicInteger();
            replay.addListener(new FastReplayListener() {
                @Override public void onReplayReadyChanged(FastReplay value) {
                    if (value.isReady()) {
                        value.destroy();
                        destroyed = true;
                    }
                }
            });
            replay.addListener(new FastReplayListener() {
                @Override public void onReplayReadyChanged(FastReplay value) {
                    laterListeners.incrementAndGet();
                }
            });
            replay.join(value -> callbacks.incrementAndGet());
            sdk.pending.get(0).then(new RecordingPlayer());
            assertFalse(replay.isReady());
            assertEquals(0, callbacks.get());
            assertEquals(0, laterListeners.get());
            assertEquals(1, sdk.releaseCalls);
        });
    }

    @Test public void setupFailureMakesInstanceTerminalUntilPendingCreateSettles() {
        main(() -> {
            AtomicInteger errors = new AtomicInteger();
            replay.addListener(new FastReplayListener() {
                @Override public void onFastError(FastException error) {
                    errors.incrementAndGet();
                }
            });
            replay.join();
            Promise<Player> staleResult = sdk.pending.get(0);
            ((CommonCallback) field(replay, "commonCallback")).sdkSetupFail(new SDKError("injected"));
            assertEquals(1, errors.get());

            replay.join();
            assertEquals(1, sdk.pending.size());
            RecordingPlayer stale = new RecordingPlayer();
            staleResult.then(stale);
            assertEquals(1, stale.stopCalls);
            assertFalse(replay.isReady());
            assertEquals(1, sdk.pending.size());
        });
    }

    @Test public void stopReleasesPlayerAndSupportsFreshJoin() {
        main(() -> {
            AtomicInteger callbacks = new AtomicInteger();
            RecordingPlayer first = new RecordingPlayer();
            replay.join(value -> callbacks.incrementAndGet());
            sdk.pending.get(0).then(first);
            assertTrue(replay.isReady());
            replay.stop();
            assertFalse(replay.isReady());
            assertEquals(1, first.stopCalls);
            assertEquals(1, sdk.releaseCalls);

            replay.join(value -> callbacks.incrementAndGet());
            assertEquals(2, sdk.pending.size());
            sdk.pending.get(1).then(new RecordingPlayer());
            assertTrue(replay.isReady());
            assertEquals(2, callbacks.get());
        });
    }

    @Test public void stopDuringCreateDefersRestartUntilOldResultIsDisposed() {
        main(() -> {
            AtomicInteger callbacks = new AtomicInteger();
            replay.join();
            Promise<Player> firstResult = sdk.pending.get(0);
            replay.stop();
            replay.join(value -> callbacks.incrementAndGet());
            assertEquals(1, sdk.pending.size());

            RecordingPlayer stale = new RecordingPlayer();
            firstResult.then(stale);
            assertEquals(1, stale.stopCalls);
            assertEquals(2, sdk.pending.size());
            sdk.pending.get(1).then(new RecordingPlayer());
            assertTrue(replay.isReady());
            assertEquals(1, callbacks.get());
        });
    }

    @Test public void setupFailureIsDeliveredOnMainAndIgnoredAfterDestroy() throws Exception {
        AtomicInteger errors = new AtomicInteger();
        main(() -> replay.addListener(new FastReplayListener() {
            @Override public void onFastError(FastException error) {
                assertSame(Looper.getMainLooper(), Looper.myLooper());
                errors.incrementAndGet();
            }
        }));
        CommonCallback callback = (CommonCallback) field(replay, "commonCallback");
        Thread worker = new Thread(() -> callback.sdkSetupFail(new SDKError("injected")));
        worker.start();
        worker.join();
        main(() -> assertEquals(1, errors.get()));
        main(() -> {
            replay.destroy();
            destroyed = true;
        });
        callback.sdkSetupFail(new SDKError("late"));
        main(() -> assertEquals(1, errors.get()));
    }

    private static final class DeferredSdk extends WhiteSdk {
        final List<Promise<Player>> pending = new ArrayList<>();
        int releaseCalls;

        DeferredSdk(FastboardView view, Context context) {
            super(view.getWhiteboardView(), context, new WhiteSdkConfiguration("test"),
                new CommonCallback() {
                    @Override public void throwError(Object args) { }
                    @Override public void onMessage(JSONObject object) { }
                    @Override public void sdkSetupFail(SDKError error) { }
                    @Override public void onLogger(JSONObject object) { }
                });
        }

        @Override public void createPlayer(PlayerConfiguration config, PlayerListener listener,
                                           Promise<Player> promise) {
            pending.add(promise);
        }

        @Override public void releasePlayer() {
            releaseCalls++;
        }
    }
}
