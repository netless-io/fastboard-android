package com.herewhite.sdk;

public final class RecordingPlayer extends Player {
    public int stopCalls;
    public RecordingPlayer() { super("test", null, 160); }
    @Override public void stop() { stopCalls++; }
}
