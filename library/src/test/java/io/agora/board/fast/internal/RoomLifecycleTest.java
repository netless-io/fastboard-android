package io.agora.board.fast.internal;

import org.junit.Test;
import static org.junit.Assert.*;

public class RoomLifecycleTest {
    @Test public void cancelledJoinMustCleanLateRoomBeforeRejoin() {
        RoomLifecycle gate = new RoomLifecycle();
        assertTrue(gate.beginJoin());
        long operation = gate.generation();
        gate.beginLeave();
        assertNotEquals(operation, gate.generation());
        assertFalse(gate.beginJoin());
        assertFalse(gate.joined()); // late SDK success must be disconnected, never published
        assertFalse(gate.beginJoin());
        gate.left(true);
        assertTrue(gate.beginJoin());
        assertTrue(gate.joined());
    }
    @Test public void joinFailureAfterCancellationAllowsRetry() {
        RoomLifecycle gate = new RoomLifecycle();
        gate.beginJoin(); gate.beginLeave(); gate.joinFailed();
        assertTrue(gate.beginJoin());
    }
    @Test public void failedCleanupCannotDisconnectAReplacementSession() {
        RoomLifecycle gate = new RoomLifecycle();
        gate.beginJoin(); gate.joined(); gate.beginLeave(); gate.left(false);
        assertFalse(gate.beginJoin());
    }
    @Test public void destroyIsTerminalEvenWithLateCallbacks() {
        RoomLifecycle gate = new RoomLifecycle();
        gate.beginJoin(); gate.destroy();
        assertFalse(gate.joined());
        gate.joinFailed(); gate.left(true); gate.fail();
        assertEquals(RoomLifecycle.State.DESTROYED, gate.state());
        assertFalse(gate.beginJoin());
    }
    @Test public void setupFailureCannotReopenBridgeOnLateError() {
        RoomLifecycle gate = new RoomLifecycle();
        gate.beginJoin(); gate.beginLeave(); gate.fail(); gate.joinFailed();
        assertFalse(gate.beginJoin());
        assertFalse(gate.joined());
        gate.left(true);
        assertEquals(RoomLifecycle.State.FAILED, gate.state());
    }
}
