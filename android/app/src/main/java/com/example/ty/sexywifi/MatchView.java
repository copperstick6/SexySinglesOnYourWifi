package com.example.ty.sexywifi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;
import static com.example.ty.sexywifi.ColorHelper.numberToColor;

/**
 * Created by ty on 2/24/18.
 */
public class MatchView extends View {
    private Paint mCirclePaint;
    private Paint mBackgroundPaint;
    private String matchName = "Searching";

    public MatchView(Context context) {
        super(context);
        mColor = Color.parseColor("#0080FF");
        mBackgroundColor = Color.parseColor("#5066B2FF");
    }

    boolean mAnimationOn = true;
    boolean mPaintGoBack = true;
    final float MIN_RADIUS_VALUE = 150.0f;
    final float MAX_RADIUS_VALUE = 200.0f;

    float mRadius = 200.0f;
    int mColor;
    int mBackgroundColor;
    String distanceStr = "Distance: n/a";

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK); // Color.parseColor("#D32F2F"));
        Paint p=new Paint();
        Rect imageRect = new Rect(0, 0, getWidth(), getHeight());


        Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.background);
        p.setColor(Color.RED);
        canvas.drawBitmap(b, null, imageRect, p);
        drawCircle(canvas);
        drawMatchText(canvas);
        drawDistancetext(canvas);

        super.onDraw(canvas);
    }

    private void drawMatchText(final Canvas canvas) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(24 * getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText(matchName, xPos, yPos, textPaint);
    }

    private void drawDistancetext(final Canvas canvas) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(20 * getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 1.25) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText(distanceStr, xPos, yPos, textPaint);
    }

    private void drawCircle(final Canvas canvas) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mColor);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);
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

    public void setMatchName(String name) {
        this.matchName = name;
    }

    public int getIntFromColor(int Red, int Green, int Blue, int mask){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return mask | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public void setDistance(double distance) {
        distance = Math.min(100., distance);
        ColorHelper.triplet colorTriplet = numberToColor(distance);
        mColor = getIntFromColor(colorTriplet.r, colorTriplet.g, colorTriplet. b, 0xFF000000);
        mBackgroundColor = getIntFromColor(colorTriplet.r, colorTriplet.g, colorTriplet. b, 0x50000000);
        distanceStr = "Distance: " + distance + " m";
    }

}
