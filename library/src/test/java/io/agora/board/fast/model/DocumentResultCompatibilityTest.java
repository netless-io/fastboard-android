package io.agora.board.fast.model;

import com.google.gson.Gson;
import com.herewhite.sdk.domain.DispatchDocsEventResult;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentResultCompatibilityTest {
    @Test public void rejectionRetainsStructuredReasonAndMessage() {
        DispatchDocsEventResult result = new Gson().fromJson(
            "{\"accepted\":false,\"reason\":\"targetNotFound\",\"message\":\"Unknown app\"}",
            DispatchDocsEventResult.class);
        assertFalse(result.isAccepted());
        assertEquals("targetNotFound", result.getReason());
        assertEquals("Unknown app", result.getMessage());
    }
    @Test public void acceptedResultMayOmitFailureFields() {
        DispatchDocsEventResult result = new Gson().fromJson("{\"accepted\":true}", DispatchDocsEventResult.class);
        assertTrue(result.isAccepted());
        assertNull(result.getReason());
        assertNull(result.getMessage());
    }
}
