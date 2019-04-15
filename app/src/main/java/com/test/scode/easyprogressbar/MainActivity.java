package com.test.scode.easyprogressbar;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    EasyProgressBar2 progressView;
    ValueAnimator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = findViewById(R.id.progress);
    }

    public void onAnim(final View view) {
        progressView.setStatus(EasyProgressBar2.STATUS_UNKNOW);
        if (animator!=null&&animator.isRunning()){
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                progressView.setProgress(progress);
            }
        });
        animator.start();
    }

    public void onAnim1(View view) {
        progressView.setProgress(0.1F);
        progressView.setStatus(EasyProgressBar2.STATUS_SUCCESS);
    }

    public void onAnim2(View view) {
        progressView.setProgress(0.1F);
        progressView.setStatus(EasyProgressBar2.STATUS_FAILED);
    }
}
