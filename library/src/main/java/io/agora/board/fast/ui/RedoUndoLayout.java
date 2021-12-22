package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import io.agora.board.fast.library.R;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;

/**
 * @author fenglibin
 */
public class RedoUndoLayout extends LinearLayoutCompat {
    private ImageView redoImage;
    private ImageView undoImage;
    private OnRedoUndoClickListener onRedoUndoClickListener;

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onRedoUndoClickListener != null) {
                if (v == redoImage) {
                    onRedoUndoClickListener.onRedoClick();
                } else if (v == undoImage) {
                    onRedoUndoClickListener.onUndoClick();
                }
            }
        }
    };

    public RedoUndoLayout(@NonNull Context context) {
        this(context, null);
    }

    public RedoUndoLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedoUndoLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setupView(context);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_undo_redo, this, true);
        undoImage = root.findViewById(R.id.tool_undo);
        redoImage = root.findViewById(R.id.tool_redo);

        undoImage.setOnClickListener(onClickListener);
        redoImage.setOnClickListener(onClickListener);
    }

    public void setShown(boolean shown) {
        setVisibility(shown ? VISIBLE : INVISIBLE);
    }

    public boolean shown() {
        return getVisibility() == VISIBLE;
    }

    public void setFastStyle(FastStyle fastStyle) {
        this.setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        undoImage.setImageTintList(ResourceFetcher.get().getIconColor(fastStyle.isDarkMode(), true));
        redoImage.setImageTintList(ResourceFetcher.get().getIconColor(fastStyle.isDarkMode(), true));
    }

    public void updateRedoUndoCount(RedoUndoCount count) {
        undoImage.setEnabled(count.getUndo() > 0);
        redoImage.setEnabled(count.getRedo() > 0);
    }

    public void setOnRedoUndoClickListener(OnRedoUndoClickListener listener) {
        this.onRedoUndoClickListener = listener;
    }

    public interface OnRedoUndoClickListener {
        void onUndoClick();

        void onRedoClick();
    }
}
