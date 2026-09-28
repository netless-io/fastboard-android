package com.herewhite.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.herewhite.sdk.domain.Promise;
import java.util.ArrayList;
import java.util.List;
import wendu.dsbridge.special.OnReturnValue;

/** Runs the published Room.addApp implementation against a recording JS transport. */
public final class RecordingAppRoom extends Room {
    public final RecordingBridge recording;
    public RecordingAppRoom() { this(new RecordingBridge()); }
    private RecordingAppRoom(RecordingBridge bridge) {
        super("test", bridge, 160, false);
        recording = bridge;
    }
    @Override public void disconnect(Promise<Object> promise) { }

    public static final class RecordingBridge implements JsBridgeInterface {
        public final List<JsonArray> calls = new ArrayList<>();
        public OnReturnValue<String> pending;
        public OnReturnValue<Boolean> pendingFocus;
        @Override public void addJavascriptObject(Object object, String namespace) { }
        @SuppressWarnings("unchecked")
        @Override public <T> void callHandler(String method, Object[] args, OnReturnValue<T> handler) {
            if ("room.focusApp".equals(method)) {
                pendingFocus = (OnReturnValue<Boolean>) handler;
                return;
            }
            if (!"room.addApp".equals(method)) throw new AssertionError(method);
            calls.add(new Gson().toJsonTree(args).getAsJsonArray());
            pending = (OnReturnValue<String>) handler;
        }
        @Override public <T> void callHandler(String method, OnReturnValue<T> handler) { throw new AssertionError(method); }
        @Override public void callHandler(String method, Object[] args) { }
        @Override public void evaluateJavascript(String script) { }
    }
}
