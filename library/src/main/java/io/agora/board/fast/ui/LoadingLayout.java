package io.agora.board.fast.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.R;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class LoadingLayout extends LinearLayoutCompat implements RoomController {
    private ProgressBar progressBar;

    private FastRoom fastRoom;

    public LoadingLayout(@NonNull Context context) {
        this(context, null);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setupView(context);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.fast_layout_loading, this, true);

        progressBar = root.findViewById(R.id.loading_progress_bar);
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    @Override
    public void updateFastStyle(FastStyle style) {
        setBackgroundColor(ResourceFetcher.get().getBackgroundColor(style.isDarkMode()));
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(style.getMainColor()));
    }

    public void showRetry() {

    }

    @Override
    public View getBindView() {
        return this;
    }
}
