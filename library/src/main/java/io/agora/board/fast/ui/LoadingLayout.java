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

import io.agora.board.fast.BoardStateObserver;
import io.agora.board.fast.library.R;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class LoadingLayout extends LinearLayoutCompat implements BoardStateObserver {
    private ProgressBar progressBar;

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
        View root = LayoutInflater.from(context).inflate(R.layout.layout_loading, this, true);

        progressBar = root.findViewById(R.id.loading_progress_bar);
    }

    public void setShown(boolean shown) {
        setVisibility(shown ? VISIBLE : GONE);
    }

    public boolean shown() {
        return getVisibility() == GONE;
    }

    public void updateFastStyle(FastStyle fastStyle) {
        this.setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(fastStyle.getMainColor()));
    }
}
