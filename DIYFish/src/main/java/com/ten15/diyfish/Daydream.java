package com.ten15.diyfish;

import android.service.dreams.DreamService;

public class Daydream extends DreamService {
    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        setInteractive(true);
        setFullscreen(true);

        setContentView(new FishCanvasView(this, getWindowManager()));
    }
}
