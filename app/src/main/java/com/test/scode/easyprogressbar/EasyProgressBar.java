package com.test.scode.easyprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

public class EasyProgressBar extends View {
    private float C_R = 80;// 内圆半径大小
    private float C_R_M = 80;// 可变内圆半径大小
    private int P_W = 5;//进度条宽度
    private int M_W = 5;//圆弧与View Margin边距
    private int FROM_ANGLE = 270;//开始角度
    private float progress = 0.5F;//当前进度  0-1
    private int Color1 = R.color.colorGary;//圆1弧颜色
    private int Color2 = R.color.colorRed;//圆2弧颜色
    private int Color3 = R.color.colorWhite;//透明颜色
    private int Color4 = R.color.colorGreen;//绿色成功后的颜色
    private int Color5 = R.color.colorRed;//红色成功后的颜色
    private int Color6 = R.color.colorWhite;//背景颜色
    private int TextColor = R.color.colorGreen;//字体颜色
    private int TextSize = 24;//dp值
    Paint paint1;//圆1画笔
    Paint paint1_1;//圆1画笔
    Paint paint2;//圆2画笔
    Paint paint3;//字体画笔
    Paint paint4;//透明画笔
    Paint paint4_1;//成功
    Paint paint5;
    Context context;
    Point P_Center;//圆心
    RectF Rect_C;//
    RectF Rect_C_1;//向内偏移
    ValueAnimator animator;//第一阶段动画


    //设置进度
    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }

    public EasyProgressBar(Context context) {
        super(context);
        initView(context);
    }

    public EasyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EasyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initRect() {
        Rect_C = new RectF(P_Center.x - dip2px(C_R + P_W), P_Center.y - dip2px(C_R + P_W),
                2 * (P_Center.x - P_W), 2 * (P_Center.y - P_W));
        Rect_C_1 = new RectF(P_Center.x - dip2px(C_R + P_W / 2), P_Center.y - dip2px(C_R + P_W / 2),
                2 * (P_Center.x - 2 * P_W), 2 * (P_Center.y - 2 * P_W));
    }


    private Path initPath() {
        Path path = new Path();
        path.moveTo(P_Center.x - dip2px((C_R / 3)), P_Center.y);
        path.lineTo(P_Center.x, P_Center.y + dip2px((C_R / 3)));
        path.lineTo(P_Center.x + dip2px((float) (((1+Math.sqrt(2))/ 6 * C_R))), (float) (P_Center.y - (Math.sqrt(2)*C_R)/2));
        return path;
    }

    private void initView(Context context) {
        this.context = context;
        P_Center = new Point(dip2px(M_W + C_R + P_W), dip2px(M_W + C_R + P_W));

        initRect();
        paint1 = new Paint();
        paint1.setColor(context.getResources().getColor(Color4));
        paint1.setAntiAlias(true);
        paint1.setDither(true);


        paint1_1 = new Paint();
        paint1_1.setColor(context.getResources().getColor(Color1));
        paint1_1.setAntiAlias(true);
        paint1_1.setDither(true);
        paint1_1.setStrokeWidth(dip2px(P_W));
        paint1_1.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setColor(context.getResources().getColor(Color2));
        paint2.setAntiAlias(true);
        paint2.setDither(true);
        paint2.setStrokeWidth(dip2px(P_W));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);

        paint3 = new Paint();
        paint3.setTextAlign(Paint.Align.CENTER);
        paint3.setColor(context.getResources().getColor(TextColor));
        paint3.setTextSize(dip2px(TextSize));
        paint3.setAntiAlias(true);
        paint3.setDither(true);
        paint3.setStrokeCap(Paint.Cap.ROUND);


        paint4 = new Paint();
        paint4.setColor(context.getResources().getColor(Color3));
        paint4.setAntiAlias(true);
        paint4.setDither(true);

        paint4_1 = new Paint();
        paint4_1.setColor(context.getResources().getColor(Color3));
        paint4_1.setAntiAlias(true);
        paint4_1.setDither(true);
        paint4_1.setStrokeWidth(dip2px(P_W));
        paint4_1.setStyle(Paint.Style.STROKE);
        paint4_1.setStrokeCap(Paint.Cap.ROUND);
        paint4_1.setStrokeJoin(Paint.Join.ROUND);

        paint5 = new Paint();//去差值效果w
        paint5.setColor(context.getResources().getColor(Color6));
//        Xfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
//        paint5.setXfermode(xFermode);
        paint5.setAntiAlias(true);
        paint5.setDither(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(context.getResources().getColor(Color6));
        initRect();
        drawC1(canvas);
        drawC2(canvas);
        drawText(canvas);
    }

    private void drawC1(Canvas canvas) {
//        canvas.drawArc(Rect_C_1, FROM_ANGLE, 360, false, paint1);
        canvas.drawCircle(P_Center.x, P_Center.y, dip2px(C_R), paint1);

    }

    private void drawC2(Canvas canvas) {
        canvas.drawArc(Rect_C_1, FROM_ANGLE, 360, false, paint1_1);
        canvas.drawArc(Rect_C_1, FROM_ANGLE, progress * 360, false, paint2);
        canvas.drawCircle(P_Center.x, P_Center.y, dip2px(C_R_M), paint4);
        if (C_R_M == 0) {
            Path path = initPath();
            canvas.drawPath(path, paint4_1);
        }
    }


    //加载成功后第一阶段动画
    public void onAnim1() {
        if (animator == null) {
            animator = ValueAnimator.ofFloat(C_R_M, 0);
        } else if (animator.isRunning() || C_R_M == 0) {
            return;
        }
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                C_R_M = progress;
                postInvalidate();
            }
        });
        animator.start();
    }


    private void drawText(Canvas canvas) {
        String text = (int) (progress * 100) + "%";
        canvas.drawText(text, P_Center.x, getTextCenterY(P_Center.y, paint3), paint3);
        if (progress == 1) {
            onAnim1();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int WHSize = dip2px(C_R + P_W + M_W);//获取view宽高
        setMeasuredDimension(2 * WHSize, 2 * WHSize);
    }

    /**
     * 将dp转换成px
     *
     * @param dpValue
     * @return
     */
    public int dip2px(float dpValue) {
        final float scale = this.context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float getTextCenterY(float Ty, Paint textPaint) {
        Paint.FontMetricsInt fmi = textPaint.getFontMetricsInt();
        float mTextCenterY = Ty - fmi.top / 2 - fmi.bottom / 2;
        return mTextCenterY;
    }
}
