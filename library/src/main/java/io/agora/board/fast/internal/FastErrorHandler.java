package io.agora.board.fast.internal;

import io.agora.board.fast.FastException;
import io.agora.board.fast.R;
import io.agora.board.fast.extension.ErrorHandler;
import io.agora.board.fast.ui.ErrorHandleLayout;

/**
 * fastboard internal implementation of ErrorHandler
 *
 * @author fenglibin
 */
public class FastErrorHandler implements ErrorHandler {
    private final ErrorHandleLayout errorHandleLayout;

    public FastErrorHandler(ErrorHandleLayout errorHandleLayout) {
        this.errorHandleLayout = errorHandleLayout;
        this.errorHandleLayout.hide();
    }

    @Override
    public void handleError(FastException e) {
        if (e.getType() == FastException.TYPE_SDK) {
            errorHandleLayout.showRetry(R.string.fast_default_init_sdk_error);
        }

        if (e.getType() == FastException.TYPE_ROOM) {
            errorHandleLayout.showRetry(R.string.fast_default_room_error_message);
        }

        if (e.getType() == FastException.TYPE_PLAYER) {
            errorHandleLayout.showRetry(R.string.fast_default_play_error_message);
        }
    }
}