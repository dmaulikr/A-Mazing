package com.example.eric.amazing;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MainActivity.MyGLSurfaceView(this);

/*        Drawable drawable = getResources().getDrawable(R.drawable.you_win);
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //use the compression format of your need
        is = new ByteArrayInputStream(stream.toByteArray());*/


        setContentView(mGLView);
    }

    class MyGLSurfaceView extends GLSurfaceView {

        private final MyGLRenderer mRenderer;

        public MyGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new MyGLRenderer(context);

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
                    }

                    requestRender();
            }

            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
    }
}
