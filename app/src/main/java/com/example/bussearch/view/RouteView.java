package com.example.bussearch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RouteView extends View {

    private Paint mLinePaint;
    private Context mContext;
    private ArrayList<String> mBusLines;
    //蓝色线段颜色
    private int mLineColor = Color.rgb(30,144,255);
    private int mWalkCircle = Color.rgb(230,230,250);
    private int mBusCircle = Color.rgb(0,0,205);


    public RouteView(Context context) {
        super(context);
    }

    public RouteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public RouteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(8);
        mLinePaint.setStyle(Paint.Style.STROKE);

        float circlePointDistance = 0;//圆心之间的距离
        float initLine = 15;//进度条的长度
        //进度条X，y坐标
        float progressCenterX = 50;
        float progressCenterY = 20;
        float baseInterval = 100;
        float circlePointY = 0;//圆心Y坐标
        float radius = 20;

        if (mBusLines == null || mBusLines.size() == 0) {
            return;
        }

        for (int i = 0; i < mBusLines.size(); i++) {
            String address = mBusLines.get(i);
            if (address == null) {
                break;
            }

            //画线
            if (i>0) {
                Path path = new Path();
                path.moveTo(progressCenterX,circlePointY+radius);
                path.lineTo(progressCenterX,
                        initLine + circlePointDistance - radius);
                canvas.drawPath(path, mLinePaint);

                //画圆
                Paint mPaintCircle = new Paint();
                mPaintCircle.setStyle(Paint.Style.STROKE);
                mPaintCircle.setStrokeWidth(5);
                mPaintCircle.setColor(Color.WHITE);
                circlePointY = initLine + progressCenterY + circlePointDistance;
                canvas.drawCircle(progressCenterX,circlePointY, radius, mPaintCircle);


                //画文字
                String text = mBusLines.get(i);
                float textdx = 80;
                Paint mPainText = new Paint();
                mPainText.setTextSize(30);
                mPainText.setColor(Color.BLACK);
                Paint.FontMetrics fontMetrics = mPainText.getFontMetrics();
                float dy = (fontMetrics.descent - fontMetrics.ascent) /2
                        - fontMetrics.descent;
                canvas.drawText(text, progressCenterX+textdx,circlePointY+dy,
                        mPainText);

            }
        }
    }
}
