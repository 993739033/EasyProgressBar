package com.test.scode.easyprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class EasyProgressBar2 extends View {
    private float C_R = 80;// 内圆半径大小
    private float P_W = 10;//进度条宽度
    private float M_W = 10;//圆距离view边界距离
    private float C_O_R = C_R + P_W / 2;//外圆的半径
    private int P_Bg_Color = R.color.colorGary;//进度条背景颜色
    private int P_Fg_Color = R.color.colorGreen;//进度条前景颜色
    private int C_Bg_Color = R.color.colorGreen;//进度完成后触发圆的背景颜色
    private int C_Fg_Color = R.color.colorGary;//进度完成后触发圆的前景颜色
    private int TextColor = R.color.colorGreen;//字体颜色
    private int M_Color = R.color.colorWhite;//标志颜色

    private int FROM_ANGLE = 270;//开始角度
    private float progress = 0.5F;//当前进度  0-1
    private int TextSize = 24;//dp值

    Paint P_Bg_P;//进度条背景画笔
    Paint P_Fg_P;//进度条前进画笔
    Paint P_C_Bg;//触发圆的背景画笔
    Paint P_C_Fg;//触发圆的前景画笔
    Paint P_T;//字体画笔
    Paint P_M;//标志画笔

    RectF Rect_C;//内圆Rect
    RectF Rect_O_B_C;//外圆背景Rect
    RectF Rect_O_F_C;//外圆前景Rect
    Point pointCenter;//圆心
    Context context;

    ValueAnimator animator;//第一阶段动画


    public EasyProgressBar2(Context context) {
        super(context);
        initView(context);
    }

    public EasyProgressBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EasyProgressBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //设置进度
    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }


    private void initView(Context context) {
        this.context = context;
        pointCenter = new Point(dip2px(M_W + C_R), dip2px(M_W + C_R));

        initRect();

        //初始化进度条背景画笔
        P_Bg_P = new Paint();
        P_Bg_P.setColor(context.getResources().getColor(P_Bg_Color));
        P_Bg_P.setAntiAlias(true);
        P_Bg_P.setDither(true);
        P_Bg_P.setStrokeWidth(dip2px(P_W));
        P_Bg_P.setStyle(Paint.Style.STROKE);


        P_Fg_P = new Paint();
        P_Fg_P.setColor(context.getResources().getColor(P_Fg_Color));
        P_Fg_P.setAntiAlias(true);
        P_Fg_P.setDither(true);
        P_Fg_P.setStrokeWidth(dip2px(P_W));
        P_Fg_P.setStyle(Paint.Style.STROKE);
        P_Fg_P.setStrokeCap(Paint.Cap.ROUND);

        //初始化圆背景及前景画笔
        P_C_Bg = new Paint();
        P_C_Bg.setColor(context.getResources().getColor(C_Bg_Color));
        P_C_Bg.setAntiAlias(true);
        P_C_Bg.setDither(true);
        P_C_Bg.setStyle(Paint.Style.FILL);

        P_C_Fg = new Paint();
        P_C_Fg.setColor(context.getResources().getColor(C_Fg_Color));
        P_C_Fg.setAntiAlias(true);
        P_C_Fg.setDither(true);
        P_C_Fg.setStyle(Paint.Style.FILL);

        P_T = new Paint();
        P_T.setColor(context.getResources().getColor(TextColor));
        P_T.setTextAlign(Paint.Align.CENTER);
        P_T.setAntiAlias(true);
        P_T.setDither(true);

        //标志的画笔
        P_M = new Paint();
        P_M.setColor(context.getResources().getColor(M_Color));
        P_M.setAntiAlias(true);
        P_M.setDither(true);
        P_M.setStrokeWidth(dip2px(P_W));
        P_M.setStyle(Paint.Style.STROKE);
        P_M.setStrokeCap(Paint.Cap.ROUND);
        P_M.setStrokeJoin(Paint.Join.ROUND);

    }


    private void initRect() {
        //初始化内外圆Rect
        Rect_C = new RectF(pointCenter.x - dip2px(C_R), pointCenter.y - dip2px(C_R),
                pointCenter.x + dip2px(C_R), pointCenter.y + dip2px(C_R));

        //圆背景
        Rect_O_B_C = new RectF(pointCenter.x - dip2px(C_R + P_W / 2), pointCenter.y - dip2px(C_R + P_W / 2),
                pointCenter.x + dip2px(C_R + P_W / 2), pointCenter.x + dip2px(C_R + P_W / 2));

        //圆前景
        Rect_O_F_C = new RectF(pointCenter.x - dip2px(C_R - P_W / 2), pointCenter.y - dip2px(C_R - P_W / 2),
                pointCenter.x + dip2px(C_R - P_W / 2), pointCenter.x + dip2px(C_R - P_W / 2));
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int WHSize = dip2px(C_R + P_W / 2 + M_W);//获取view宽高
        setMeasuredDimension(2 * WHSize, 2 * WHSize);
    }

}
