package com.ten15.diyfish;

import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import java.util.Random;

public class FishDrawable {
    private static long MIN_DURATION = 12000l, MAX_DURATION = 30000l;
    private final Drawable mDrawable;
    private final Point mPoint;
    private AnimateDrawable mAnimateDrawable;
    private boolean jolted = false;

    FishDrawable(Drawable drawable, Point windowSize) {
        mDrawable = drawable;
        mPoint = windowSize;
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());

        reset();
    }

    private void reset() {
        jolted = false;
        setRandomColor();
        Point startPoint = new Point(
                -(mDrawable.getIntrinsicWidth() + Entropy.random(0, mPoint.x / 2)), // start offscreen to the left
                Entropy.random(-mDrawable.getIntrinsicHeight(), mPoint.y + mDrawable.getIntrinsicHeight()) // start anywhere on y-axis
        );
        Point endPoint = new Point(
                mPoint.x, // end once offscreen right
                Entropy.random(-mDrawable.getIntrinsicHeight(), mPoint.y + mDrawable.getIntrinsicHeight()) // end randomly on y-axix
        );
        startAnimation(startPoint, endPoint, Entropy.random() ? new LinearInterpolator() : new AccelerateInterpolator(), Entropy.random(MIN_DURATION, MAX_DURATION));
    }

    public void jolt() {
        float[] position = mAnimateDrawable.getPosition();
        // do not jolt if already jolted, or not completely onscreen
        if(jolted || position[Matrix.MTRANS_X] < 0 || position[Matrix.MTRANS_X] > (mPoint.x - mDrawable.getIntrinsicWidth())) {
            return;
        }
        jolted = true;

        Point startPoint = new Point(
                (int) position[Matrix.MTRANS_X],
                (int) position[Matrix.MTRANS_Y]
        );
        Point endPoint = new Point(
                (int) position[Matrix.MTRANS_X],
                Entropy.random() ? -mDrawable.getIntrinsicHeight() : mPoint.y
        );
        startAnimation(startPoint, endPoint, new DecelerateInterpolator(), Entropy.random(MIN_DURATION, MAX_DURATION) / 2l);
    }

    public void draw(Canvas canvas) {
        if (mAnimateDrawable.hasEnded()) {
            reset();
        }
        mAnimateDrawable.draw(canvas);
    }

    private void setRandomColor() {
        mDrawable.mutate().setAlpha(Entropy.random(30, 255));
        if (Entropy.random()) {
            mDrawable.mutate().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x000000FF));
        } else {
            mDrawable.mutate().setColorFilter(null);
        }
    }

    private void startAnimation(Point start, Point end, Interpolator interpolator, long duration) {
        Animation an = new TranslateAnimation(start.x, end.x, start.y, end.y);

        an.setInterpolator(interpolator);
        an.setDuration(duration);
        an.setRepeatCount(0);
        an.initialize(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight(), mPoint.x, mPoint.x);

        mAnimateDrawable = new AnimateDrawable(mDrawable, an);
        an.start();
    }

    private static class Entropy {
        static final Random r = new Random();

        public static long random(long from, long to) {
            return from + ((long) (r.nextDouble() * (to - from)));
        }

        public static int random(int from, int to) {
            return from + ((int) (r.nextDouble() * (to - from)));
        }

        public static boolean random() {
            return r.nextBoolean();
        }
    }
}
