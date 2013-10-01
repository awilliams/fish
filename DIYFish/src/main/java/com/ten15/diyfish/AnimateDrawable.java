package com.ten15.diyfish;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

public class AnimateDrawable {
    private Drawable mDrawable;
    private Animation mAnimation;
    private Transformation mTransformation = new Transformation();

    public AnimateDrawable(Drawable target, Animation animation) {
        mDrawable = target;
        mAnimation = animation;
    }

    public boolean hasEnded() {
        return mAnimation.hasEnded();
    }

    public float[] getPosition() {
        float[] matrix = new float[9];
        Transformation transformation = new Transformation();

        mAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), transformation);
        transformation.getMatrix().getValues(matrix);
        return matrix;
    }

    public void draw(Canvas canvas) {
        int sc = canvas.save();
        mAnimation.getTransformation(
                AnimationUtils.currentAnimationTimeMillis(),
                mTransformation);
        canvas.concat(mTransformation.getMatrix());
        mDrawable.draw(canvas);
        canvas.restoreToCount(sc);
    }
}
