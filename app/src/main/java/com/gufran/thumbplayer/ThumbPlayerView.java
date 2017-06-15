package com.gufran.thumbplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.icu.text.RuleBasedCollator;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description ...
 *
 * @author Gufran Khurshid
 * @version 1.0
 * @since 6/12/17
 */

public class ThumbPlayerView extends View {

    private int playerPrimaryColor, playerDisabledColor;
    private Paint finishedPaint, unfinishedPaint;
    private RectF finishedOuterRect = new RectF();
    private RectF unfinishedOuterRect = new RectF();
    private int strokeWidth;
    private int height, width;
    private int progress = 0;
    private int max = 100;
    private Drawable iconPlayDrawable, iconStopDrawable,iconErrorDrawable;

    public static final int STATE_PREPARING = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_STOPPED = 2;
    public static final int STATE_ERROR = 3;


    public int state = STATE_STOPPED;


    ///loading progress
    private int animatingProgress = 0;
    private Handler animatingProgressHandler;
    private Runnable animatingProgressRunnable;
    private int ANIMATION_PROGRESS_DELAY = 20;


    public ThumbPlayerView(Context context) {
        this(context, null);
    }

    public ThumbPlayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThumbPlayerView, 0, defStyleAttr);
        setProgress(typedArray.getInt(R.styleable.ThumbPlayerView_progress, 0));
        playerPrimaryColor = typedArray
                .getColor(R.styleable.ThumbPlayerView_playerPrimaryColor, 0);
        playerDisabledColor = typedArray
                .getColor(R.styleable.ThumbPlayerView_playerDisabledColor, 0);
        iconPlayDrawable = typedArray
                .getDrawable(R.styleable.ThumbPlayerView_icon_play);
        iconStopDrawable = typedArray
                .getDrawable(R.styleable.ThumbPlayerView_icon_stop);
        iconErrorDrawable= typedArray
                .getDrawable(R.styleable.ThumbPlayerView_icon_error);
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.ThumbPlayerView_strokeWidth, 10);
        setMax(100);
        initPainters();
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state == STATE_PREPARING) {
            setProgress(0);
            drawAnimatingOutline(canvas);
        } else if (state == STATE_PLAYING) {
            drawOutline(canvas);
            drawDrawableAtCenter(canvas, iconStopDrawable);
        } else if (state == STATE_STOPPED) {
            setProgress(0);
            drawOutline(canvas);
            drawDrawableAtCenter(canvas, iconPlayDrawable);
        }else{
            setProgress(0);
            drawOutline(canvas);
            drawDrawableAtCenter(canvas, iconErrorDrawable);
        }
    }

    private void drawOutline(Canvas canvas) {
        float delta = (float) (0.05 * height);
        finishedOuterRect.set(delta,
                delta,
                width - delta,
                height - delta);
        unfinishedOuterRect.set(delta,
                delta,
                width - delta,
                height - delta);
        canvas.drawArc(finishedOuterRect, -90, getProgressAngle(), false, finishedPaint);
        canvas.drawArc(unfinishedOuterRect, -90 + getProgressAngle(), 360 - getProgressAngle(), false, unfinishedPaint);
    }

    private void drawAnimatingOutline(Canvas canvas) {
        int arcAngle = 10; //30 degrees
        float delta = (float) (0.05 * height);
        finishedOuterRect.set(delta,
                delta,
                width - delta,
                height - delta);
        unfinishedOuterRect.set(delta,
                delta,
                width - delta,
                height - delta);

        canvas.drawArc(unfinishedOuterRect, 0, 360, false, unfinishedPaint);
        int startAngle = animatingProgress + arcAngle;
        int endAngle = animatingProgress + arcAngle;
        canvas.drawArc(finishedOuterRect, startAngle, endAngle, false, finishedPaint);
    }

    private void drawDrawableAtCenter(Canvas canvas, Drawable drawable) {
        int start = (int) width / 2 - (int) drawable.getIntrinsicWidth() / 2 + getPaddingLeft();
        int top = (int) height / 2 - (int) drawable.getIntrinsicHeight() / 2 + getPaddingTop();
        int end = (int) width / 2 + (int) drawable.getIntrinsicWidth() / 2 - getPaddingRight();
        int bottom = (int) height / 2 + (int) drawable.getIntrinsicHeight() / 2 - getPaddingBottom();
        drawable.setBounds(start, top, end, bottom);
        drawable.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = width;
        setMeasuredDimension(width, width);
    }

    private float getProgressAngle() {
        return getProgress() / (float) max * 360f;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    protected void initPainters() {
        finishedPaint = new Paint();
        finishedPaint.setColor(playerPrimaryColor);
        finishedPaint.setStrokeWidth(strokeWidth);
        finishedPaint.setStyle(Paint.Style.STROKE);
        finishedPaint.setAntiAlias(true);

        unfinishedPaint = new Paint();
        unfinishedPaint.setColor(playerDisabledColor);
        unfinishedPaint.setStrokeWidth(strokeWidth);
        unfinishedPaint.setStyle(Paint.Style.STROKE);
        unfinishedPaint.setAntiAlias(true);
    }

    private void startProgressAnimation() {
        animatingProgressHandler = new Handler();
        animatingProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (animatingProgress >= 360)
                    animatingProgress = 0;
                animatingProgress += 3;
                animatingProgressHandler.postDelayed(this, ANIMATION_PROGRESS_DELAY);
                invalidate();
            }
        };
        animatingProgressHandler.post(animatingProgressRunnable);
    }

    private void stopProgressAnimation() {
        if (animatingProgressHandler != null && animatingProgressRunnable != null) {
            animatingProgress = 0;
            animatingProgressHandler.removeCallbacks(animatingProgressRunnable);
            invalidate();
        }
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case STATE_PREPARING:
                startProgressAnimation();
                break;
            case STATE_PLAYING:
                stopProgressAnimation();
                break;
            case STATE_STOPPED:
                stopProgressAnimation();
                break;
            case STATE_ERROR:
                stopProgressAnimation();
                break;
        }
        invalidate();
    }
}
