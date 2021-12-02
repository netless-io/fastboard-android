package io.agora.board.fast.ui;

import android.content.Context;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import io.agora.board.fast.library.R;

public class FastColorDrawableManager {
    private static SparseArray<FastColorDrawable> drawables = new SparseArray();

    public static FastColorDrawable getDrawable(Context context, int color) {
        FastColorDrawable drawable = drawables.get(color);
        if (drawable == null) {
            drawable = createFastColorDrawable(context, color);
            drawables.append(color, drawable);
        }
        return drawable;
    }

    @NonNull
    private static FastColorDrawable createFastColorDrawable(Context context, int color) {
        FastColorDrawable drawable;
        int borderWidth = context.getResources().getDimensionPixelSize(R.dimen.fast_color_border_width);
        int borderRadius = context.getResources().getDimensionPixelSize(R.dimen.fast_color_border_radius);
        drawable = new FastColorDrawable(color, borderWidth, borderRadius);
        return drawable;
    }
}
