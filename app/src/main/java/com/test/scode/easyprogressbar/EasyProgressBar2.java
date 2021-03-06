package com.test.scode.easyprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class EasyProgressBar2 extends View {
    private float C_R = 40;// 内圆半径大小
    private float P_W = 5;//进度条宽度
    private float P_M_W = 5;//Mark宽度
    private float M_W = 10;//圆距离view边界距离
    private float C_O_R = C_R + P_W / 2;//外圆的半径
    private int P_Bg_Color = R.color.colorGary;//进度条背景颜色
    private int P_Fg_Color = R.color.colorGreen;//进度条前景颜色
    private int C_Bg_Color = R.color.colorGreen;//进度完成后触发圆的背景颜色
    private int C_Fg_Color = R.color.colorWhite;//进度完成后触发圆的前景颜色
    private int TextColor = R.color.colorGreen;//字体颜色
    private int M_Color = R.color.colorWhite;//标志颜色
    private int Err_Color = R.color.colorRed;//错误时的颜色
    private int Succ_Color = R.color.colorGreen;//成功时的颜色

    private int FROM_ANGLE = 270;//开始角度
    private float progress = 0.1F;//当前进度  0-1
    private int TextSize = 24;//dp值

    Paint P_Bg_P;//进度条背景画笔
    Paint P_Fg_P;//进度条前进画笔
    Paint P_C_Bg;//触发圆的背景画笔
    Paint P_C_Fg;//触发圆的前景画笔
    Paint P_T;//字体画笔
    Paint P_M;//标志画笔
    boolean isAniming = false;//圆是否处于动画状态

    RectF Rect_C;//内圆Rect
    Point pointCenter;//圆心
    Context context;

    public static String STATUS_SUCCESS = "Success";//成功状态
    public static String STATUS_FAILED = "Failed";//失败状态
    public static String STATUS_UNKNOW = "known";//未知状态

    public static String statusNow = STATUS_UNKNOW;//当前状态


    ValueAnimator animator = ValueAnimator.ofFloat(C_O_R, 0);//第一阶段动画
    ValueAnimator animator2;//第二阶段动画 及Mark绘制阶段

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
        if (isExplicitStatus()) return;//已处于明确状态就不进行刷新进度
        this.progress = progress;
        if (this.progress >= 1) {
            this.progress = 1;
            statusNow = STATUS_SUCCESS;
        } else if (this.progress < 0) {
            this.progress = 0;
        }
        initData();
        postInvalidate();
    }

    //是否处于明确的状态
    private boolean isExplicitStatus() {
        if (statusNow.equals(STATUS_SUCCESS) || statusNow.equals(STATUS_FAILED)) {
            return true;
        }
        return false;
    }

    public void setStatus(String status) {
        statusNow = status;
        initData();
        if (statusNow.equals(STATUS_UNKNOW)) return;
        onProgressAnim();
    }

    //进度更新中断时开启进度条动画
    public void onProgressAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(progress, 1);
        animator.setDuration(250);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    public void initData() {
        if (C_O_R < P_W / 2) {
            C_O_R = C_R + P_W / 2;//初始化圆半径
        }
        M_Path = new Path();
        if (statusNow.equals(STATUS_FAILED)) {
            P_Fg_P.setColor(getResources().getColor(Err_Color));
            P_C_Bg.setColor(getResources().getColor(Err_Color));
        } else if (statusNow.equals(STATUS_SUCCESS)) {
            P_Fg_P.setColor(getResources().getColor(Succ_Color));
            P_C_Bg.setColor(getResources().getColor(Succ_Color));
        } else {
            P_Fg_P.setColor(getResources().getColor(P_Fg_Color));
            P_C_Bg.setColor(getResources().getColor(P_Fg_Color));
        }
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
        P_T.setTextSize(dip2px(TextSize));
        P_T.setTextAlign(Paint.Align.CENTER);
        P_T.setAntiAlias(true);
        P_T.setDither(true);

        //标志的画笔
        P_M = new Paint();
        P_M.setColor(context.getResources().getColor(M_Color));
        P_M.setAntiAlias(true);
        P_M.setDither(true);
        P_M.setStrokeWidth(dip2px(P_M_W));
        P_M.setStyle(Paint.Style.STROKE);
        P_M.setStrokeCap(Paint.Cap.ROUND);
        P_M.setStrokeJoin(Paint.Join.ROUND);

    }

    private void initRect() {
        //初始化内外圆Rect
        Rect_C = new RectF(pointCenter.x - dip2px(C_R), pointCenter.y - dip2px(C_R),
                pointCenter.x + dip2px(C_R), pointCenter.y + dip2px(C_R));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawCircle(canvas);
        drawText(canvas);
        drawMark(canvas);
    }

    //画进度条
    private void drawProgress(Canvas canvas) {
        canvas.drawArc(Rect_C, FROM_ANGLE, 360, false, P_Bg_P);//进度前景
        canvas.drawArc(Rect_C, FROM_ANGLE, progress * 360, false, P_Fg_P);//进度背景
    }

    //画圆
    private void drawCircle(Canvas canvas) {
        if (progress >= 1) {
            canvas.drawCircle(pointCenter.x, pointCenter.y, dip2px(C_R), P_C_Bg);
            canvas.drawCircle(pointCenter.x, pointCenter.y, dip2px(C_O_R - P_W / 2), P_C_Fg);
            onCircleAnim();
        }
    }

    Path M_Path = new Path();

    private void drawMark(Canvas canvas) {
        if (C_O_R - P_W / 2 <= 0) {
            canvas.drawPath(M_Path, P_M);
        }
    }

    private void startPathAnim(final Path path) {
        if (path == null || (animator2 != null && animator2.isRunning())) return;
        final PathMeasure measure = new PathMeasure(path, false);
        animator2 = ValueAnimator.ofFloat(0, measure.getLength());
        animator2.setDuration(250);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float length = (float) animation.getAnimatedValue();
                M_Path = new Path();
                measure.getSegment(0, length, M_Path, true);
                if (statusNow.equals(STATUS_FAILED) && measure.nextContour()) {
                    Path path1 = new Path();
                    measure.getSegment(0, length, path1, true);
                    M_Path.addPath(path1);
                    measure.setPath(path, false);
                }
                postInvalidate();
            }
        });
        animator2.start();
    }

    private void drawText(Canvas canvas) {
        if (progress >= 1) return;
        String text = (int) (progress * 100) + "%";
        canvas.drawText(text, pointCenter.x, getTextCenterY(pointCenter.y, P_T), P_T);
    }

    private Path initMarkPath() {
        if (statusNow.equals(STATUS_SUCCESS)) {
            return getSuccessPath();
        } else if (statusNow.equals(STATUS_FAILED)) {
            return getFailedPath();
        }
        return new Path();
    }

    private Path getSuccessPath() {
        Path path = new Path();
        float C_R_M = (float) (C_R * 1.3);//这里调整Mark的大小
        path.moveTo(pointCenter.x - dip2px((C_R_M / 3)), pointCenter.y);
        path.lineTo(pointCenter.x, pointCenter.y + dip2px((C_R_M / 3)));
        path.lineTo(pointCenter.x + dip2px((float) (((1 + Math.sqrt(2)) / 6 * C_R_M))), (float) (pointCenter.y - (Math.sqrt(2) * C_R_M) / 2));
        return path;
    }

    private Path getFailedPath() {
        Path path = new Path();
        float C_R_M = (float) (C_R * 1.3);//这里调整Mark的大小
        path.moveTo(pointCenter.x - dip2px(C_R_M / 3), pointCenter.y - dip2px(C_R_M / 3));
        path.lineTo(pointCenter.x + dip2px(C_R_M / 3), pointCenter.y + dip2px(C_R_M / 3));
        path.moveTo(pointCenter.x + dip2px(C_R_M / 3), pointCenter.y - dip2px(C_R_M / 3));
        path.lineTo(pointCenter.x - dip2px(C_R_M / 3), pointCenter.y + dip2px(C_R_M / 3));
        return path;
    }

    //加载成功后第一阶段动画
    public void onCircleAnim() {
        if (animator.isRunning() || C_O_R == 0) {
            return;
        }
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                C_O_R = progress;
                if (C_O_R <= 0) {
                    Path path = initMarkPath();
                    startPathAnim(path);
                }
                postInvalidate();
            }
        });
        animator.start();
    }

    /**
     * 将dp转换成px
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

    private float getTextCenterY(float Ty, Paint textPaint) {
        Paint.FontMetricsInt fmi = textPaint.getFontMetricsInt();
        float mTextCenterY = Ty - fmi.top / 2 - fmi.bottom / 2;
        return mTextCenterY;
    }
}
