package com.example.bussearch.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.bussearch.R;
import com.example.bussearch.data.BusLineBean;

import java.util.ArrayList;
import java.util.List;

public class BusLineView extends View {

    private Paint mLinePaint, mBusPaint;
    private List<BusLineBean> mBusStops;
    private static Bitmap mBusBitMap, upBitMap;
    private Context mContext;
    private int mBusBitWidth,mBusBitHeight;
    private Rect mSrcRect;
    private RectF mDestRect;
    public static final String TAG = "BusLineView";

    public BusLineView(Context context) {
        super(context);
    }

    public BusLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        context = mContext;
        mBusStops = new ArrayList<>();
        initBitmap();
        mBusPaint = new Paint();
    }

    private void initBitmap() {
        mBusBitMap = ((BitmapDrawable)getResources().getDrawable(R.drawable.bus)).getBitmap();
        Log.d(TAG, "initBitmap: " + mBusBitMap);
        upBitMap = upImageSize(mContext, mBusBitMap, 30,30);
        Log.d(TAG, "initBitmap: " +upBitMap);
        mBusBitWidth = upBitMap.getWidth();
        mBusBitHeight = upBitMap.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSize, 2*heightSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(10);
        mLinePaint.setStyle(Paint.Style.STROKE);

        float circlePointDistance = 0;//圆心之间的距离
        float initLine = 20;//进度条的长度
        //进度条X，y坐标
        float progressCenterX = 200;
        float progressCenterY = 20;
        float baseInterval = 100;
        float circlePointY = 0;//圆心Y坐标
        float radius = 20;
        float distance = 0;

        if (mBusStops == null || mBusStops.size() == 0) {
            return;
        }
        for (int i = 0; i < mBusStops.size(); i++) {
            if (i>0) {
                //画线
                Path path = new Path();
                path.moveTo(progressCenterX,circlePointY + radius);
                path.lineTo(progressCenterX,
                        initLine + progressCenterY + circlePointDistance - radius + distance);
                canvas.drawPath(path, mLinePaint);
            }
            //画圆
            Paint mPaintCircle = new Paint();
            mPaintCircle.setStyle(Paint.Style.STROKE);
            mPaintCircle.setStrokeWidth(5);
            mPaintCircle.setColor(Color.BLACK);
            circlePointY = initLine + progressCenterY + circlePointDistance + distance;
            canvas.drawCircle(progressCenterX,circlePointY, radius, mPaintCircle);


            //画文字
            float textdx = 80;
            String text = mBusStops.get(i).getStop();
            Paint mPainText = new Paint();
            mPainText.setTextSize(50);
            mPainText.setColor(Color.BLUE);
            Paint.FontMetrics fontMetrics = mPainText.getFontMetrics();
            float dy = (fontMetrics.descent - fontMetrics.ascent) /2
                    - fontMetrics.descent;
            canvas.drawText(text, progressCenterX+textdx,circlePointY+dy,
                    mPainText);

            circlePointDistance +=  baseInterval+radius;

            if (mBusStops.get(i).getIsArrived()) {
                mSrcRect = new Rect(0, 0, mBusBitWidth, mBusBitHeight);//图片的大小
                float left = progressCenterX - mBusBitWidth/2;
                float top = circlePointY - mBusBitWidth /2;
                float right = progressCenterX+mBusBitWidth/2;
                float bottom = circlePointY + mBusBitHeight/2;
                mDestRect = new RectF(left, top, right, bottom);//图片的位置
                //if (upBitMap != null)
                canvas.drawBitmap(upBitMap,mSrcRect,mDestRect,mBusPaint);
            }
        }
    }

    public void setBusStops(List<BusLineBean> busStops) {
        if (busStops != null && busStops.size()>0) {
            mBusStops = busStops;
        }
        invalidate();
    }

    protected Bitmap upImageSize(Context context, Bitmap bitmap, int width, int height) {
        Log.d(TAG, "upImageSize: " + bitmap);
        if (bitmap == null) {
            return null;
        }

        float scaleX = (float)width / bitmap.getWidth();
        float scaleY = (float)height / bitmap.getHeight();

        int newW = 0;
        int newH = 0;

        if (scaleX>scaleY) {
            newW = (int)(bitmap.getWidth() * scaleX);
            newH = (int)(bitmap.getHeight() * scaleX);
        } else if (scaleX<=scaleY) {
            newH = (int)(bitmap.getHeight() * scaleY);
            newW = (int)(bitmap.getWidth() * scaleY);
        }
        Log.d(TAG, "upImageSize: " + Bitmap.createScaledBitmap(bitmap, newW,newH, true));
        return Bitmap.createScaledBitmap(bitmap, newW,newH, true);
    }
}
