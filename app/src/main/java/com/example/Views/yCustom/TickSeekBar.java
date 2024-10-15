package com.example.Views.yCustom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatSeekBar;

public class TickSeekBar extends AppCompatSeekBar {

    private Paint tickPaint;
    private Paint textPaint;
    private float tickHeight;
    private int[] textSizes = {10, 15, 20, 25, 30};

    public TickSeekBar(Context context) {
        super(context);
        init();
    }

    public TickSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TickSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        tickPaint = new Paint();
        tickPaint.setColor(getResources().getColor(android.R.color.black));
        tickHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(android.R.color.black));
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        textPaint.setTextAlign(Paint.Align.CENTER);

        setMax(textSizes.length - 1);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < textSizes.length; i++) {
            float tickX = i * (width - getPaddingLeft() - getPaddingRight()) / (textSizes.length - 1) + getPaddingLeft();
            canvas.drawLine(tickX, height / 2 - tickHeight / 2,
                    tickX, height / 2 + tickHeight / 2, tickPaint);
            canvas.drawText(String.valueOf(textSizes[i]), tickX, height - getPaddingBottom(), textPaint);
        }
    }

    public int getTextSizeForProgress(int progress) {
        if (progress >= 0 && progress < textSizes.length) {
            return textSizes[progress];
        }
        return textSizes[2]; // Default to 15 if out of range
    }
}