package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import io.agora.board.fast.FastRoom;
import io.agora.board.fast.R;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastStyle;

/**
 * @author fenglibin
 */
public class RedoUndoLayout extends LinearLayoutCompat implements RoomController {
    private ImageView redoImage;
    private ImageView undoImage;

    private FastRoom fastRoom;

    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == redoImage) {
                fastRoom.redo();
            } else if (v == undoImage) {
                fastRoom.undo();
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
        setupView(context);
    }

    private void setupView(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_fast_undo_redo, this, true);
        undoImage = root.findViewById(R.id.tool_undo);
        redoImage = root.findViewById(R.id.tool_redo);

        undoImage.setOnClickListener(onClickListener);
        redoImage.setOnClickListener(onClickListener);
        updateRedoUndo(new FastRedoUndo(0, 0));
    }

    @Override
    public void setFastRoom(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    @Override
    public void updateFastStyle(FastStyle fastStyle) {
        setBackground(ResourceFetcher.get().getLayoutBackground(fastStyle.isDarkMode()));
        undoImage.setImageTintList(ResourceFetcher.get().getIconColor(fastStyle.isDarkMode(), true));
        redoImage.setImageTintList(ResourceFetcher.get().getIconColor(fastStyle.isDarkMode(), true));
    }

    @Override
    public void updateRedoUndo(FastRedoUndo count) {
        undoImage.setEnabled(count.getUndo() > 0);
        redoImage.setEnabled(count.getRedo() > 0);
    }

    @Override
    public View getBindView() {
        return this;
    }
}
