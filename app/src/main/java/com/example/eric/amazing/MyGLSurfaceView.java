package com.example.eric.amazing;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Message;
import android.view.MotionEvent;

/**
 * Created by Eric on 5/3/2017.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private static MyGLRenderer mRenderer;
    MainActivity.LooperThread looper;

    public MyGLSurfaceView(Context context, MainActivity.LooperThread looper){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer(context, looper);
        this.looper = looper;

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        //Continuously re-render the frame
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);


    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }


                if(!mRenderer.hasWon()){
                    mRenderer.translateTriangleBasedOnAngle((dx + dy) * TOUCH_SCALE_FACTOR);

                    mRenderer.setAngle(
                            mRenderer.getAngle() +
                                    ((dx + dy) * TOUCH_SCALE_FACTOR));

                    Message message = new Message();
                    message.obj = false;
                    looper.mHandler.handleMessage(message);

                } else {
                    Message message = new Message();
                    message.obj = true;
                    looper.mHandler.handleMessage(message);
                }

                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    public static void restart(){
        mRenderer.restart();
    }
}
