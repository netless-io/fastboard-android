package io.agora.board.fast.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import io.agora.board.fast.library.R;

/**
 * @author fenglibin
 */
public final class StrokeSeeker extends View {
    private StrokeSeeker.OnStrokeChangedListener onStrokeChangedListener;
    private Paint indicatorPaint;
    private float indicatorWidth;
    private float indicatorHeight;
    private float offsetLeftX;
    private float offsetRightX;
    private float leftBarHeight;
    private final RectF leftRect;
    private Paint leftPaint;
    private float rightBarHeight;
    private RectF rightRect;
    private Paint rightPaint;
    private Path seekerPath;
    private final float leftLimit;
    private float currentX;
    private int baseY;
    private int minStroke;
    private int maxStroke;
    private int currentStrokeWidth;

    public StrokeSeeker(@NonNull Context context) {
        this(context, null);
    }

    public StrokeSeeker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokeSeeker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.indicatorPaint = new Paint();
        this.indicatorWidth = this.dp2px(2);
        this.indicatorHeight = this.dp2px(16);
        this.offsetLeftX = this.dp2px(2);
        this.offsetRightX = this.dp2px(8);
        this.leftBarHeight = this.dp2px(2);
        this.leftRect = new RectF();
        this.leftPaint = new Paint();
        this.rightBarHeight = this.dp2px(12);
        this.rightRect = new RectF();
        this.rightPaint = new Paint();
        this.seekerPath = new Path();
        this.leftLimit = this.offsetLeftX - this.leftBarHeight / (float) 2;
        this.currentX = this.leftLimit;
        this.minStroke = 12;
        this.maxStroke = 24;
        this.currentStrokeWidth = -1;
        this.indicatorPaint.setAntiAlias(true);
        this.indicatorPaint.setStrokeWidth(this.indicatorWidth);
        this.indicatorPaint.setColor(ContextCompat.getColor(context, R.color.fast_default_main_color));
        this.leftPaint.setAntiAlias(true);
        this.leftPaint.setColor(ContextCompat.getColor(context, R.color.fast_default_main_color));
        this.rightPaint.setAntiAlias(true);
        this.rightPaint.setColor(Color.parseColor("#F3F6F9"));
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                getContext().getResources().getDisplayMetrics()
        );
    }

    private float getRightLimit() {
        return (float) this.getWidth() - this.offsetRightX + this.rightBarHeight / (float) 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.baseY = this.getHeight() / 2;
        this.leftRect.set(this.offsetLeftX - this.leftBarHeight, (float) this.baseY - this.leftBarHeight / (float) 2, this.offsetLeftX + this.leftBarHeight / (float) 2, (float) this.baseY + this.leftBarHeight / (float) 2);
        this.rightRect.set((float) this.getWidth() - this.offsetRightX - this.rightBarHeight / (float) 2, (float) this.baseY - this.rightBarHeight / (float) 2, (float) this.getWidth() - this.offsetRightX + this.rightBarHeight / (float) 2, (float) this.baseY + this.rightBarHeight / (float) 2);
        this.seekerPath.reset();
        this.seekerPath.moveTo(this.offsetLeftX, (float) this.baseY - this.leftBarHeight / (float) 2);
        this.seekerPath.lineTo((float) this.getWidth() - this.offsetRightX, (float) this.baseY - this.rightBarHeight / (float) 2);
        this.seekerPath.arcTo(this.rightRect, 270.0F, 180.0F);
        this.seekerPath.lineTo(this.offsetLeftX, (float) this.baseY + this.leftBarHeight / (float) 2);
        this.seekerPath.arcTo(this.leftRect, 90.0F, 180.0F);
        this.setStrokeWidth(this.currentStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipRect(this.currentX, 0.0F, (float) this.getWidth(), (float) this.getHeight());
        canvas.drawPath(this.seekerPath, this.rightPaint);
        canvas.restore();
        canvas.save();
        canvas.clipRect(0.0F, 0.0F, this.currentX, (float) this.getHeight());
        canvas.drawPath(this.seekerPath, this.leftPaint);
        canvas.restore();
        canvas.drawLine(this.currentX, (float) this.baseY - this.indicatorHeight / (float) 2, this.currentX, (float) this.baseY + this.indicatorHeight / (float) 2, this.indicatorPaint);
    }

    private float currentBarHeight() {
        float wa = (float) this.getWidth() - this.offsetLeftX - this.offsetRightX;
        float wp = this.currentX - this.offsetLeftX;
        return (this.rightBarHeight - this.leftBarHeight) * wp / wa + this.leftBarHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                this.updateCurrentX(event.getX());
                break;
            default:
                break;
        }

        return true;
    }

    private void updateCurrentX(float x) {
        this.currentX = Math.min(Math.max(x, this.leftLimit), this.getRightLimit());
        float per = (this.currentX - this.leftLimit) / (this.getRightLimit() - this.leftLimit);
        int result = (int) Math.min((float) this.rangeSize() * per + (float) this.minStroke, (float) this.maxStroke);
        if (result != this.currentStrokeWidth) {
            this.currentStrokeWidth = result;
            StrokeSeeker.OnStrokeChangedListener listener = this.onStrokeChangedListener;
            if (listener != null) {
                listener.onStroke(result);
            }
        }
        this.invalidate();
    }

    private int rangeSize() {
        return this.maxStroke - this.minStroke + 1;
    }

    public final void setStrokeRange(int minStroke, int maxStroke) {
        this.minStroke = minStroke;
        this.maxStroke = maxStroke;
    }

    public final void setStrokeWidth(int strokeWidth) {
        this.currentStrokeWidth = Math.max(strokeWidth, minStroke);
        this.currentX = (float) (currentStrokeWidth - this.minStroke) / (float) this.rangeSize() * (this.getRightLimit() - this.leftLimit) + this.leftLimit;
        this.invalidate();
    }

    public final void setSeekerBarColor(@ColorInt int color) {
        indicatorPaint.setColor(color);
        leftPaint.setColor(color);
        this.invalidate();
    }

    public final void setOnStrokeChangedListener(StrokeSeeker.OnStrokeChangedListener onStrokeChangedListener) {
        this.onStrokeChangedListener = onStrokeChangedListener;
    }

    public interface OnStrokeChangedListener {
        void onStroke(int width);
    }
}