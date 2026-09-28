package io.agora.board.fast.internal;

import io.agora.board.fast.extension.FastResult;
import org.junit.Test;
import static org.junit.Assert.*;

public class OnceResultTest {
    @Test public void cancelledConversionIgnoresLateSuccessAndError() {
        int[] calls = {0};
        Exception cancelled = new Exception("cancelled");
        OnceResult<String> result = new OnceResult<>(new FastResult<String>() {
            public void onSuccess(String value) { fail("late app ID must not be published"); }
            public void onError(Exception error) { assertSame(cancelled, error); calls[0]++; }
        });
        result.finish(null, cancelled, () -> {});
        result.finish("late-app-id", null, () -> fail("already completed"));
        result.finish(null, new Exception(), () -> fail("already completed"));
        assertEquals(1, calls[0]);
    }
    @Test public void writableFalseIsSuccessAndCallbackMayReenter() {
        int[] calls = {0};
        boolean[] removed = {false};
        @SuppressWarnings("unchecked") OnceResult<Boolean>[] holder = new OnceResult[1];
        holder[0] = new OnceResult<>(new FastResult<Boolean>() {
            public void onSuccess(Boolean writable) {
                assertFalse(writable);
                assertTrue(removed[0]);
                assertTrue(holder[0].isCompleted());
                calls[0]++;
                holder[0].finish(null, new Exception(), () -> fail("reentry"));
            }
            public void onError(Exception error) { fail(error.toString()); }
        });
        holder[0].finish(false, null, () -> removed[0] = true);
        assertEquals(1, calls[0]);
    }
}
