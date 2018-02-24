package com.example.ty.sexywifi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by ty on 2/24/18.
 */
public class MatchView extends View {
    private Rect mRectangle;
    private Paint mPaint;

    private Paint mCirclePaint;
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



        super.onDraw(canvas);
    }

    private void drawCircle(final Canvas canvas) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(0xFFFFFFFF);
        //mCirclePaint.setColor(0xFF4081);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(0xFFFFFFFF);
//        //Draw circle
        canvas.drawCircle(w / 2, h / 2, MIN_RADIUS_VALUE, mCirclePaint);
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
    }

    public void animateButton(boolean animate){
        if (!animate)
            mRadius = MIN_RADIUS_VALUE;
        mAnimationOn = animate;
        invalidate();
    }

}
