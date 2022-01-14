package io.agora.board.fast.extension;

import org.jetbrains.annotations.NotNull;

import io.agora.board.fast.FastException;

/**
 * A ErrorHandler is a class enable caller to redefine handle of board cause error
 */
public interface ErrorHandler {
    void handleError(@NotNull FastException e);
}
