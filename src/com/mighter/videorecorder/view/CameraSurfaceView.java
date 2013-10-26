package com.mighter.videorecorder.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CameraSurfaceView.class.getSimpleName();
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraSurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
		public void surfaceChanged(SurfaceHolder holder, int format, int weight, int height) {

			if (mHolder.getSurface() == null) {
				return;
			}

			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();

			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
            //Ignored
		}

    public Camera getCamera() {
        return mCamera;
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
    }
}