package com.herewhite.sdk;

import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.WindowPrefersColorScheme;

/** Controls native callback timing without connecting to a room or changing server state. */
public final class DeferredRoom extends Room {
    public Promise<Object> pendingLeave;
    public DeferredRoom() { super("test", null, 160, false); }
    @Override public RoomState getRoomState() { return new RoomState(); }
    @Override public Boolean getWritable() { return false; }
    @Override public void setPrefersColorScheme(WindowPrefersColorScheme scheme) { }
    @Override public void disconnect(Promise<Object> result) { pendingLeave = result; }
}
