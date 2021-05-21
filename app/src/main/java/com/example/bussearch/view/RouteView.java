package com.example.bussearch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import com.baidu.mapapi.search.route.TransitRouteLine;

import java.util.ArrayList;
import java.util.List;

public class RouteView extends View {

     public interface OnItemSelectListener{
         public void onItemSelect(TransitRouteLine.TransitStep transitStep);
     }

    private Paint mLinePaint;
    private Context mContext;
    private List<TransitRouteLine.TransitStep> mTransitSteps;
    private ArrayList<Rect> mRects = new ArrayList<>();
    public static final String TAG = "RouteView";
    private String mWalk = "步行导航";
    private OnItemSelectListener listener;
    private int index=0;

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
        mTransitSteps = new ArrayList<>();
    }

    public RouteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnItemSelectListener(OnItemSelectListener listener){
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSize, heightSize);
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
        float baseInterval = 200;
        float circlePointY = 0;//圆心Y坐标
        float radius = 20;
        float distance = 0;
        String text;

        if (mTransitSteps == null || mTransitSteps.size() == 0) {
            return;
        }

        mRects.clear();
        for (int i = 0; i < mTransitSteps.size(); i++) {
            TransitRouteLine.TransitStep step = mTransitSteps.get(i);
            if (step == null) {
                break;
            }

            //画线
            if (i>=0) {
                //distance = distance+80;
                if (step.getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING) {
                    //distance = distance+80;
                    text = "步行导航";
                    mLinePaint.setColor(Color.GREEN);
                } else {
                    text = "公交详情";
                    mLinePaint.setColor(mLineColor);
                }
                if (i == mTransitSteps.size()-1) {
                    text = "目的地";
                }
                    Path path = new Path();
                    path.moveTo(progressCenterX,circlePointY + radius);
                    path.lineTo(progressCenterX,
                            initLine + progressCenterY + circlePointDistance - radius + distance);
                    canvas.drawPath(path, mLinePaint);


                //画圆
                Paint mPaintCircle = new Paint();
                mPaintCircle.setStyle(Paint.Style.STROKE);
                mPaintCircle.setStrokeWidth(5);
                mPaintCircle.setColor(Color.BLACK);
                circlePointY = initLine + progressCenterY + circlePointDistance + distance;
                canvas.drawCircle(progressCenterX,circlePointY, radius, mPaintCircle);


                //画文字
                float textdx = 80;
                Paint mPainText = new Paint();
                mPainText.setTextSize(50);
                mPainText.setColor(Color.BLUE);
                Paint.FontMetrics fontMetrics = mPainText.getFontMetrics();
                float dy = (fontMetrics.descent - fontMetrics.ascent) /2
                        - fontMetrics.descent;
                canvas.drawText(text, progressCenterX+textdx,circlePointY+dy,
                        mPainText);

                Rect rect = new Rect();
                rect.left = (int)(progressCenterX+textdx);
                rect.right = (int)(progressCenterX+textdx+mPainText.measureText(mWalk));
                rect.top = (int)(circlePointY+dy-fontMetrics.top);
                rect.bottom = (int)(circlePointY+dy+fontMetrics.bottom-2*fontMetrics.top+150);
                mRects.add(rect);

                circlePointDistance +=  baseInterval+radius;

            }
        }
        mRects.remove(mTransitSteps.size()-1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d(TAG, "onTouchEvent: " + event.getAction() + " index = " + index);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getRawX();
                float y = event.getRawY();
                Log.d(TAG, "onTouchEvent: x = " + x + " y = " + y);
                Log.d(TAG, "onTouchEvent: isTouch = " + isTouch(x,y));
                if (isTouch(x,y)&&listener != null) {
                    listener.onItemSelect(mTransitSteps.get(index));
                }
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                index = -1;
                return true;
        }
        return super.onTouchEvent(event);
    }

    public boolean isTouch(float x , float y) {
        Log.d(TAG, "isTouch: rectSize = " + mRects.size());
        for (int i = 0; i < mRects.size(); i++) {
            Log.d(TAG, "isTouch: left = " + mRects.get(i).left
         + " right = " + mRects.get(i).right
            + " top = " + mRects.get(i).top
          + " bottom = " + mRects.get(i).bottom);
            if (mRects.get(i).contains((int)x,(int)y)) {
                index = i;
                return true;
            }
        }
        return false;
    }

    public void setBusDataList(List<TransitRouteLine.TransitStep> transitSteps) {
        if (transitSteps != null && transitSteps.size()>0) {
            mTransitSteps = transitSteps;
        }
        invalidate();
    }
}
