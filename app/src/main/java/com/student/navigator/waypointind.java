package com.student.navigator;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class waypointind extends View {

    private float direction;

    public waypointind(Context context) {
        super(context);

    }

    public waypointind(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public waypointind(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int r;
        if(w > h){
            r = h/2;
        }else{
            r = w/2;
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.GRAY);
        //draws the circle
        canvas.drawCircle(w/2, h/2, r, paint);

        paint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawLine(
                w/2,
                h/2,
                (float)(w/2 + r * Math.sin(-direction)),
                (float)(h/2 - r * Math.cos(-direction)),
                paint);

    }

    public void update(float dir){
        direction = dir;

        // Call invalidate to force drawing on page.

        invalidate();
    }

}