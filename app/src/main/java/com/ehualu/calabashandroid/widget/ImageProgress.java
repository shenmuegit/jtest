package com.ehualu.calabashandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by GaoTing on 2020/1/1.
 * <p>
 * Explain:自定义图片进度条
 */
public class ImageProgress extends View {
    private Paint paint;
    public RectF rectF;
    private Paint paintStroke;

    int startAngle = -90;
    int sweepAngle = 360;

    public ImageProgress(Context context) {
        this(context, null);
    }

    public ImageProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0x80808080);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paintStroke = new Paint();
        paintStroke.setColor(0x80808080);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setAntiAlias(true);
        paintStroke.setStrokeWidth(2);


    }

    public void setProgress(int progress) {
        if (progress == 100) {
            setVisibility(GONE);
            return;
        }
        int pro = (int) (progress * 3.6);
        startAngle = pro - 90;
        sweepAngle = 360 - pro;
        postInvalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
            rectF = new RectF(0, 0, width, width);

        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
        canvas.drawCircle(width / 2, width / 2, width / 2, paintStroke);

    }
}
