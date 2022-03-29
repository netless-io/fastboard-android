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
            hide();
            fastRoom.join();
        });
    }

    public void showRetry(@StringRes int resId) {
        messageView.setText(resId);
        show();
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    @Override
    public void updateFastStyle(FastStyle style) {

    }

    @Override
    public View getBindView() {
        return this;
    }
}
