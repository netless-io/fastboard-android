package io.agora.board.fast.sample.cases.flat;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class FlatRoomToolsLayout extends LinearLayout {

    public FlatRoomToolsLayout(Context context) {
        this(context, null);
    }

    public FlatRoomToolsLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatRoomToolsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView(context);
        setOrientation(VERTICAL);
    }

    private void setupView(Context context) {

    }
}
