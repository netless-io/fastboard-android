package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.agora.board.fast.R;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class DeleteButton extends FrameLayout {
    private ImageView delete;

    public DeleteButton(@NonNull Context context) {
        this(context, null);
    }

    public DeleteButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeleteButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_delete_button, this, true);
        delete = root.findViewById(R.id.delete);
    }

    public void setAppliance(FastAppliance fastAppliance) {
        setVisibility(fastAppliance == FastAppliance.SELECTOR ? VISIBLE : INVISIBLE);
    }

    public void updateFastStyle(FastStyle style) {
        setBackground(ResourceFetcher.get().getButtonBackground(style.isDarkMode()));
        delete.setImageTintList(ResourceFetcher.get().getIconColor(style.isDarkMode()));
    }
}
