package io.agora.board.fast.internal;

import io.agora.board.fast.FastException;

/**
 * @author fenglibin
 */
public interface FastErrorHandler {
    void handleError(FastException e);
}
