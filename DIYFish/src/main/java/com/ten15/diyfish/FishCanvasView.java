package com.ten15.diyfish;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;


public final class FishCanvasView extends View {
    final static int BACKGROUND_COLOR = Color.BLACK;
    final static String TOY_DIY_URL = "http://www.toydiy.tumblr.com";

    private ArrayList<FishDrawable> mAnimateDrawables = new ArrayList<FishDrawable>();
    private FragmentManager mFragmentManager;
    private WindowManager mWindowManager;
    private boolean popupDisplayed = false;

    public FishCanvasView(Context context, WindowManager windowManager) {
        super(context);
        mWindowManager = windowManager;
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTouch();
            }
        });

        addDrawables(context);
    }

    public FishCanvasView(Context context, WindowManager windowManager, FragmentManager fragmentManager) {
        this(context, windowManager);
        // this is called from the activity
        // allows use of a fragment
        mFragmentManager = fragmentManager;
    }

    private void onTouch() {
        if(mFragmentManager != null && !popupDisplayed) {
            PopupDialogFragment popup = new PopupDialogFragment();
            popup.show(mFragmentManager, "popup");
            popupDisplayed = true;
        }
        joltDrawables();
    }

    private void joltDrawables() {
        for (FishDrawable drawable : mAnimateDrawables) {
            drawable.jolt();
        }
    }

    private void addDrawables(Context context) {
        // get window dimensions
        Point size = new Point();
        mWindowManager.getDefaultDisplay().getSize(size);

        for (int drawableResource : new int[]{R.drawable.pez_1, R.drawable.pez_2, R.drawable.pez_3, R.drawable.pez_4}) {
            mAnimateDrawables.add(new FishDrawable(context.getResources().getDrawable(drawableResource), size));
            mAnimateDrawables.add(new FishDrawable(context.getResources().getDrawable(drawableResource), size));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);

        for (FishDrawable drawable : mAnimateDrawables) {
            drawable.draw(canvas);
        }
        invalidate();
    }

    private class PopupDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            Dialog dialog = new Dialog(getActivity(), R.style.CutomDialog);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.popup);
            View img = dialog.findViewById(R.id.toy_diy);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(TOY_DIY_URL));
                    startActivity(i);
                }
            });

            // Create the popup object and return it
            dialog.show();
            return dialog;
        }
    }
}
