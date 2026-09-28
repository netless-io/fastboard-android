package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.LinearLayoutCompat;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.R;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class ErrorHandleLayout extends LinearLayoutCompat implements RoomController {
    private FastRoom fastRoom;

    private TextView messageView;
    private View retry;
    private int errorResource;

    public ErrorHandleLayout(@NonNull Context context) {
        this(context, null);
    }

    public ErrorHandleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorHandleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setupView(context);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.fast_layout_error_handle, this, true);
        messageView = root.findViewById(R.id.fast_error_handle_message);

        retry = root.findViewById(R.id.fast_error_handle_retry);
        retry.setOnClickListener(v -> {
            // State may have changed since this error was displayed.
            if (fastRoom != null && !fastRoom.canRetryJoin()) {
                showRetry(R.string.fast_default_room_error_message);
                return;
            }
            hide();
            if (fastRoom != null) {
                fastRoom.join();
            }
        });
    }

    public void showRetry(@StringRes int resId) {
        if (fastRoom != null && fastRoom.isReady()) {
            hide();
            return;
        }
        errorResource = resId;
        renderError();
        show();
    }

    /** Re-evaluate an already displayed error after asynchronous join/leave completion. */
    public void refreshRetryState() {
        if (fastRoom != null && fastRoom.isReady()) {
            hide();
        } else if (errorResource != 0 && getVisibility() == VISIBLE) {
            renderError();
        }
    }

    private void renderError() {
        int resId = errorResource;
        boolean canRetry = fastRoom == null || fastRoom.canRetryJoin();
        if (fastRoom != null && fastRoom.requiresRecreation()) {
            resId = R.string.fast_default_reopen_required;
        } else if (!canRetry) {
            resId = R.string.fast_default_room_busy;
        }
        messageView.setText(resId);
        retry.setVisibility(canRetry ? VISIBLE : GONE);
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    @Override
    public void updateFastStyle(FastStyle style) {
        setBackgroundColor(ResourceFetcher.get().getBackgroundColor(style.isDarkMode()));
    }

    @Override
    public View getBindView() {
        return this;
    }
}
