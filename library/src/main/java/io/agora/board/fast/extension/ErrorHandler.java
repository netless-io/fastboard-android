package io.agora.board.fast.extension;

import org.jetbrains.annotations.NotNull;

import io.agora.board.fast.FastException;

/**
 * @author fenglibin
 */
public interface ErrorHandler {
    void handleError(@NotNull FastException e);
}
