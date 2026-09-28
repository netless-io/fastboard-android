package io.agora.board.fast.internal;

/** Main-thread connection gate. Cleanup must finish before the same bridge can rejoin. */
public final class RoomLifecycle {
    public enum State { IDLE, JOINING, JOINED, CANCELLING, LEAVING, FAILED, DESTROYED }
    private State state = State.IDLE;
    private long generation;
    public State state() { return state; }
    public long generation() { return generation; }
    public boolean beginJoin() {
        if (state != State.IDLE) return false;
        generation++;
        state = State.JOINING;
        return true;
    }
    public boolean joined() {
        if (state == State.JOINING) { state = State.JOINED; return true; }
        if (state == State.CANCELLING) state = State.LEAVING;
        return false;
    }
    public void joinFailed() {
        if (state == State.JOINING || state == State.CANCELLING) state = State.IDLE;
    }
    public void beginLeave() {
        generation++;
        if (state == State.JOINING) state = State.CANCELLING;
        else if (state == State.JOINED) state = State.LEAVING;
    }
    public void left(boolean success) {
        if (state == State.LEAVING) state = success ? State.IDLE : State.FAILED;
    }
    public void destroy() { generation++; state = State.DESTROYED; }
    public void fail() {
        if (state != State.DESTROYED) { generation++; state = State.FAILED; }
    }
}
