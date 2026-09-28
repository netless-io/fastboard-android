package io.agora.board.fast;

import static org.junit.Assert.*;
import android.content.Context;
import android.view.ContextThemeWrapper;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.herewhite.sdk.RecordingAppRoom;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.WindowOriginSize;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.*;
import org.junit.runner.RunWith;
import io.agora.board.fast.extension.FastResult;
import io.agora.board.fast.internal.RoomLifecycle;
import io.agora.board.fast.model.*;

@RunWith(AndroidJUnit4.class)
public class PresentationCompatibilityTest {
    private FastRoom fast;
    private RecordingAppRoom nativeRoom;
    private final DocPage[] pages = {
        new DocPage("https://example.com/1.png", 1280d, 720d, "https://example.com/thumb.png"),
        new DocPage("https://example.com/2.png", 720d, 1280d)
    };
    private void main(Runnable action) { InstrumentationRegistry.getInstrumentation().runOnMainSync(action); }
    @Before public void setup() {
        main(() -> {
            Context context = new ContextThemeWrapper(InstrumentationRegistry.getInstrumentation().getTargetContext(), androidx.appcompat.R.style.Theme_AppCompat);
            FastboardView view = new FastboardView(context);
            fast = view.getFastboard().createFastRoom(new FastRoomOptions("test", "test", "test", "test", FastRegion.CN_HZ));
            nativeRoom = new RecordingAppRoom();
            try {
                Field room = FastRoom.class.getDeclaredField("room");
                room.setAccessible(true);
                room.set(fast, nativeRoom);
                Field state = FastRoom.class.getDeclaredField("lifecycle");
                state.setAccessible(true);
                RoomLifecycle lifecycle = (RoomLifecycle) state.get(fast);
                assertTrue(lifecycle.beginJoin());
                assertTrue(lifecycle.joined());
            } catch (Exception error) { throw new AssertionError(error); }
        });
    }
    @After public void cleanup() { main(() -> fast.destroy()); }

    @Test public void focusPreservesCommitBooleanAndCancelsLateResult() { main(() -> {
        List<Boolean> values = new ArrayList<>();
        List<Exception> errors = new ArrayList<>();
        FastResult<Boolean> result = new FastResult<Boolean>() {
            @Override public void onSuccess(Boolean value) { values.add(value); }
            @Override public void onError(Exception error) { errors.add(error); }
        };
        fast.focusApp("app", result);
        nativeRoom.recording.pendingFocus.onValue(false);
        fast.focusApp("app", result);
        nativeRoom.recording.pendingFocus.onValue(true);
        assertEquals(java.util.Arrays.asList(false, true), values);
        fast.focusApp("app", result);
        fast.destroy();
        nativeRoom.recording.pendingFocus.onValue(true);
        assertEquals(2, values.size());
        assertEquals(1, errors.size());
        assertEquals(FastException.ROOM_OPERATION_CANCELLED, ((FastException) errors.get(0)).getCode());
    }); }

    @Test public void listenerDestroyStopsOriginalRoomEventFanout() { main(() -> {
        java.util.concurrent.atomic.AtomicInteger laterEvents = new java.util.concurrent.atomic.AtomicInteger();
        fast.addListener(new FastRoomListener() {
            @Override public void onFastStyleChanged(FastStyle value) { fast.destroy(); }
        });
        fast.addListener(new FastRoomListener() {
            @Override public void onFastStyleChanged(FastStyle value) { laterEvents.incrementAndGet(); }
        });
        fast.setFastStyle(fast.getFastStyle());
        assertEquals(0, laterEvents.get());
    }); }

    @Test public void presentationUsesNativeBridgePayload() { main(() -> {
        fast.insertPresentation(pages, "课件", new WindowOriginSize(1280, 720), null);
        JsonArray wire = nativeRoom.recording.calls.get(0);
        assertEquals("Presentation", wire.get(0).getAsString());
        JsonObject options = wire.get(1).getAsJsonObject();
        assertEquals("课件", options.get("title").getAsString());
        assertTrue(options.get("scenePath").getAsString().startsWith("/"));
        assertFalse(options.has("originSize"));
        JsonArray scenes = options.getAsJsonArray("scenes");
        assertEquals(2, scenes.size());
        assertEquals("1", scenes.get(0).getAsJsonObject().get("name").getAsString());
        assertEquals("2", scenes.get(1).getAsJsonObject().get("name").getAsString());
        JsonObject ppt = scenes.get(0).getAsJsonObject().getAsJsonObject("ppt");
        assertEquals(pages[0].getSrc(), ppt.get("src").getAsString());
        assertEquals(pages[0].getPreview(), ppt.get("previewURL").getAsString());
        assertEquals(1280d, ppt.get("width").getAsDouble(), 0);
        assertEquals(720d, ppt.get("height").getAsDouble(), 0);
        JsonObject origin = wire.get(2).getAsJsonObject().getAsJsonObject("originSize");
        assertEquals(1280d, origin.get("width").getAsDouble(), 0);
        assertEquals(720d, origin.get("height").getAsDouble(), 0);
    }); }

    @Test public void legacyKindsAndScenePathsArePreserved() { main(() -> {
        fast.insertStaticDoc(pages, "legacy", null);
        fast.insertPptx(pages, "dynamic", null);
        fast.insertPptx("task", "https://example.com/task", "dynamic task", null);
        fast.insertPresentation(pages, "new", null);
        fast.insertPresentation(pages, "new again", null);
        String[] kinds = {"DocsViewer", "Slide", "Slide", "Presentation", "Presentation"};
        HashSet<String> paths = new HashSet<>();
        for (int i = 0; i < kinds.length; i++) {
            JsonArray args = nativeRoom.recording.calls.get(i);
            assertEquals(kinds[i], args.get(0).getAsString());
            paths.add(args.get(1).getAsJsonObject().get("scenePath").getAsString());
        }
        assertEquals(5, paths.size());
        // Official 2.16.130 emits null attributes when no optional attributes exist.
        assertTrue(nativeRoom.recording.calls.get(4).get(2).isJsonNull());
    }); }

    @Test public void nativeAppIdNullAndErrorFormatsArePreserved() { main(() -> {
        List<String> values = new ArrayList<>();
        List<Exception> errors = new ArrayList<>();
        FastResult<String> result = result(values, errors);
        fast.insertPresentation(pages, "ok", result);
        nativeRoom.recording.pending.onValue("native-app-id");
        fast.insertPresentation(pages, "null", result);
        nativeRoom.recording.pending.onValue(null);
        fast.insertPresentation(pages, "error", result);
        nativeRoom.recording.pending.onValue("{\"__error\":{\"message\":\"setup failed\",\"jsStack\":\"test stack\"}}");
        assertEquals(2, values.size());
        assertEquals("native-app-id", values.get(0));
        assertNull(values.get(1));
        assertEquals(1, errors.size());
        assertTrue(errors.get(0) instanceof SDKError);
        assertEquals("test stack", ((SDKError) errors.get(0)).getJsStack());
    }); }

    @Test public void destroyCancelsOnceAndIgnoresLateBridgeResult() { main(() -> {
        List<String> values = new ArrayList<>();
        List<Exception> errors = new ArrayList<>();
        fast.insertPresentation(pages, "pending", result(values, errors));
        fast.destroy();
        nativeRoom.recording.pending.onValue("late-app-id");
        assertTrue(values.isEmpty());
        assertEquals(1, errors.size());
        assertEquals(FastException.ROOM_OPERATION_CANCELLED, ((FastException) errors.get(0)).getCode());
    }); }

    @Test public void invalidPagesNeverReachNativeBridge() { main(() -> {
        List<String> values = new ArrayList<>();
        List<Exception> errors = new ArrayList<>();
        DocPage[][] invalid = {null, {}, {null}, {new DocPage("ppt://dynamic", 1d, 1d)},
            {new DocPage(" ", 1d, 1d)}, {new DocPage("https://example.com/1.png", Double.NaN, 1d)},
            {new DocPage("https://example.com/1.png", null, 1d)}};
        for (DocPage[] value : invalid) fast.insertPresentation(value, "bad", result(values, errors));
        fast.insertPresentation(pages, "bad size", new WindowOriginSize(0, 1), result(values, errors));
        assertEquals(8, errors.size());
        for (Exception error : errors) assertTrue(error instanceof IllegalArgumentException);
        assertTrue(values.isEmpty());
        assertTrue(nativeRoom.recording.calls.isEmpty());
    }); }

    private FastResult<String> result(List<String> values, List<Exception> errors) {
        return new FastResult<String>() {
            @Override public void onSuccess(String value) { values.add(value); }
            @Override public void onError(Exception error) { errors.add(error); }
        };
    }
}
