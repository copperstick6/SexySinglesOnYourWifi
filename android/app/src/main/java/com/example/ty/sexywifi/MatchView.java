package com.example.ty.sexywifi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

import static android.graphics.Color.parseColor;

/**
 * Created by ty on 2/24/18.
 */
public class MatchView extends View {
    private Rect mRectangle;
    private Paint mPaint;

    private Paint mCirclePaint;
    private Paint mMatchName;
    private Paint mBackgroundPaint;

    public MatchView(Context context) {
        super(context);
        int x = 50;
        int y = 50;
        int sideLength = 200;

        // create a rectangle that we'll draw later
        mRectangle = new Rect(x, y, sideLength, sideLength);

        // create the Paint and set its color
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
    }

    boolean mAnimationOn = true;
    boolean mPaintGoBack = true;
    final float MIN_RADIUS_VALUE = 150.0f;
    final float MAX_RADIUS_VALUE = 200.0f;

    float mRadius = 200.0f;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#F50057"));

        drawCircle(canvas);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(24 * getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText("Ty", xPos, yPos, textPaint);
        super.onDraw(canvas);
    }

    private void drawCircle(final Canvas canvas) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#0080FF"));
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.parseColor("#5066B2FF"));
//        //Draw circle
        if (mAnimationOn) {
            if (mRadius >= MAX_RADIUS_VALUE)
                mPaintGoBack = true;
            else if (mRadius <= MIN_RADIUS_VALUE)
                mPaintGoBack = false;
            //Draw pulsating shadow
            canvas.drawCircle(w / 2, h / 2, mRadius, mBackgroundPaint);
            mRadius = mPaintGoBack ? (mRadius - 2.0f) : (mRadius + 2.0f);
            invalidate();
        }
        canvas.drawCircle(w / 2, h / 2, MIN_RADIUS_VALUE, mCirclePaint);

    }

    public void animateButton(boolean animate){
        if (!animate)
            mRadius = MIN_RADIUS_VALUE;
        mAnimationOn = animate;
        invalidate();
    }

}
