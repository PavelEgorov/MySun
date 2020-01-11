package com.egorovsoft.mysun.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.egorovsoft.mysun.R;

import androidx.annotation.Nullable;

public class RectangleView extends View {
    public static final String TAG = "SUN_VIEW";
    public static final String DEF_TEXT = "0";
    public static final int DEF_COLOR = Color.BLUE;
    public static final int DEF_LENGTH = 100;
    public static final int DEF_RADIUS = 10;
    public static final int Y = 100;
    public static final int X = 100;

    private Paint paint;
    private int x;
    private int y;
    private int width;
    private int height;

    public RectangleView(Context context) {
        super(context);
        init(Y, X, DEF_LENGTH, DEF_LENGTH);
    }

    public RectangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initAttr(context, attrs);
    }

    public RectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, Paint paint) {
        super(context, attrs, defStyleAttr);

        initAttr(context, attrs);
        this.paint = paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void init(int x, int y, int w, int h){
        Log.d(TAG, "Constructor");

        paint = new Paint();
        paint.setColor(DEF_COLOR);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    private  void initAttr(Context context, @Nullable AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.RectangleView,
                0,
                0
        );

        int x = typedArray.getInt(R.styleable.RectangleView_view_x, DEF_RADIUS);
        int y = typedArray.getInt(R.styleable.RectangleView_view_y, DEF_LENGTH);
        int width = typedArray.getInt(R.styleable.RectangleView_view_weight, DEF_COLOR);
        int height = typedArray.getInt(R.styleable.RectangleView_view_heigth, DEF_COLOR);

        init(x, y, width, height);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        height = getMeasuredHeight();	// высота
        width = getMeasuredWidth();	// ширина

        setMeasuredDimension(width, Math.max(width, height));
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        Log.d(TAG, "layout");
        super.layout(l, t, r, b);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "draw");
        super.draw(canvas);
    }
}
