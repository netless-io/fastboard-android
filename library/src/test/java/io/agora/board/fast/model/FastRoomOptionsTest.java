package io.agora.board.fast.model;

import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.domain.WindowParams;
import com.herewhite.sdk.domain.SlideNavigationButtonMode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class FastRoomOptionsTest {
    @Test public void latestWindowAndSlideOptionsKeepNativeWireNames() {
        FastRoomOptions options = new FastRoomOptions("app", "uuid", "token", "uid", FastRegion.CN_HZ);
        WindowParams window = options.getRoomParams().getWindowParams();
        window.setForceMaximized(true).setLazySetupInMaximizedMode(true).setMaxCachedAppsInMaximizedMode(2);
        WhiteSdkConfiguration.SlideAppOptions slide = new WhiteSdkConfiguration.SlideAppOptions();
        slide.setNavigationButtonMode(SlideNavigationButtonMode.Step);
        options.getSdkConfiguration().setSlideAppOptions(slide);
        JsonObject room = JsonParser.parseString(options.getRoomParams().toString()).getAsJsonObject();
        JsonObject wire = room.getAsJsonObject("windowParams");
        assertTrue(wire.get("forceMaximized").getAsBoolean());
        assertTrue(wire.get("lazySetupInMaximizedMode").getAsBoolean());
        assertEquals(2, wire.get("maxCachedAppsInMaximizedMode").getAsInt());
        JsonObject sdk = JsonParser.parseString(options.getSdkConfiguration().toString()).getAsJsonObject();
        assertEquals("step", sdk.getAsJsonObject("slideAppOptions").get("navigationButtonMode").getAsString());
    }
    @Test public void getterMutationsAreUsedByJoin() {
        FastRoomOptions options = new FastRoomOptions("app", "uuid", "token", "uid", FastRegion.CN_HZ);
        WhiteSdkConfiguration sdk = options.getSdkConfiguration();
        sdk.setEnableAppliancePlugin(true);
        RoomParams room = options.getRoomParams();
        room.setWritable(false);
        assertSame(sdk, options.getSdkConfiguration());
        assertSame(room, options.getRoomParams());
        assertTrue(options.getSdkConfiguration().isEnableAppliancePlugin());
        assertFalse(options.getRoomParams().isWritable());
    }
    @Test public void optionSettersUpdateCachedObjectsWithoutDroppingCustomizations() {
        FastRoomOptions options = new FastRoomOptions("app", "uuid", "token", "uid", FastRegion.CN_HZ);
        RoomParams params = options.getRoomParams();
        WindowParams window = params.getWindowParams();
        params.setWritable(false);
        options.getSdkConfiguration();
        FastUserPayload payload = new FastUserPayload("test");
        options.setUserPayload(payload);
        options.setContainerSizeRatio(0.75f);
        assertSame(params, options.getRoomParams());
        assertSame(window, params.getWindowParams());
        assertSame(payload, params.getUserPayload());
        assertEquals(0.75f, window.getContainerSizeRatio(), 0.00001f);
        assertFalse(params.isWritable());
    }
}
