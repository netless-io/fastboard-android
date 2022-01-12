package io.agora.board.fast.extension;

import io.agora.board.fast.FastException;

/**
 * @author fenglibin
 */
public interface ErrorHandler {
    void handleError(FastException e);
}
