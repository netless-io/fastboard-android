package io.agora.board.fast;

import android.content.Context;
import android.content.ContextWrapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class FastboardConfigTest {
    private FastboardConfig.Builder builder() {
        return new FastboardConfig.Builder(new ContextWrapper(null) {
            @Override public Context getApplicationContext() { return this; }
        });
    }
    @Test public void enablingPreloadAloneUsesPositiveDefault() {
        FastboardConfig config = builder().enablePreload(true).build();
        assertTrue(config.isEnablePreload());
        assertEquals(1, config.getPreloadCount());
    }
    @Test public void invalidCapacityFailsBeforeManagerInitialization() {
        for (int count : new int[]{0, -1}) {
            assertThrows(IllegalArgumentException.class, () -> builder().enablePreload(true).preloadCount(count).build());
        }
        assertEquals(2, builder().enablePreload(true).preloadCount(2).build().getPreloadCount());
    }
    @Test public void disabledPreloadingDoesNotRequireCapacity() {
        assertFalse(builder().enablePreload(false).preloadCount(0).build().isEnablePreload());
    }
}
