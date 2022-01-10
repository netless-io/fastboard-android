package io.agora.board.fast.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import io.agora.board.fast.FastListener;
import io.agora.board.fast.FastRoom;
import io.agora.board.fast.FastSdk;
import io.agora.board.fast.R;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.RedoUndoCount;

/**
 * @author fenglibin
 */
public class RedoUndoLayout extends LinearLayoutCompat implements FastListener, RoomController {
    private ImageView redoImage;
    private ImageView undoImage;

    private FastRoom fastRoom;

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fastRoom == null) {
                return;
            }

            if (v == redoImage) {
                fastRoom.getRoom().redo();
            } else if (v == undoImage) {
                fastRoom.getRoom().undo();
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
    
    @Override
    public void attachSdk(FastSdk fastSdk) {
        fastSdk.addListener(this);
    }

    @Override
    public void onFastRoomCreated(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    @Override
    public void onRedoUndoChanged(RedoUndoCount count) {
        undoImage.setEnabled(count.getUndo() > 0);
        redoImage.setEnabled(count.getRedo() > 0);
    }
}
