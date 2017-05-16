package csci571.zhiqinliao.hw9.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Self design progress bar used in legislator's detail
 */

public class MyProgressBar extends ProgressBar {
    private String text;
    private int value;
    private Paint mPaint;

    public MyProgressBar(Context context) {
        super(context);
        initTextPaint();
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTextPaint();
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTextPaint();
    }

    private void initTextPaint() {
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.parseColor("#726F73"));
        this.mPaint.setTextSize(40);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        setText(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        if(value < 45)
            this.mPaint.setColor(Color.WHITE);
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    private void setText(int progress){
        this.value = (progress * 100)/this.getMax();
        this.text = String.valueOf(value) + "%";
    }
}
