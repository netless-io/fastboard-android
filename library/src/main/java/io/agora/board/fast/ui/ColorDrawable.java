package io.agora.board.fast.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author fenglibin
 */
class ColorDrawable extends Drawable {
    private static final int DEFAULT_BOARDER_COLOR = Color.parseColor("#40000000");

    private Paint mPaintColor;
    private Paint mPaintBoarder;
    private RectF mRect;

    private int mBorderWidth;
    private int mBorderRadius;

    public ColorDrawable(int color, int borderWidth, int borderRadius) {
        this(color, borderWidth, borderRadius, DEFAULT_BOARDER_COLOR);
    }

    public ColorDrawable(int color, int borderWidth, int borderRadius, int borderColor) {
        mPaintColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintColor.setStyle(Paint.Style.FILL);
        mPaintColor.setColor(color);

        mPaintBoarder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBoarder.setStyle(Paint.Style.STROKE);
        mPaintBoarder.setColor(borderColor);

        mRect = new RectF();

        mBorderWidth = borderWidth;
        mBorderRadius = borderRadius;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        int padding = bounds.width() / 6;
        mRect.set(bounds.left + padding, bounds.top + padding, bounds.right - padding, bounds.bottom - padding);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(mRect, mBorderRadius, mBorderRadius, mPaintColor);
        canvas.drawRoundRect(mRect, mBorderRadius, mBorderRadius, mPaintBoarder);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaintColor.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaintColor.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
