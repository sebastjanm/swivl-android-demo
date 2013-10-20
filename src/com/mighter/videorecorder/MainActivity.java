package com.mighter.videorecorder;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "MainActivity";

	private static final String FILE_NAME = "/myvideo.mp4";
	
	private static final int MAX_FILE_SIZE = 5 * 1000 * 1000;
	private static final int MAX_DURATION = 60 * 1000;

	private Camera myCamera;
	private MyCameraSurfaceView myCameraSurfaceView;
	private MediaRecorder mediaRecorder;

	private String filePath;

	Button recordButton;
	SurfaceHolder surfaceHolder;
	boolean recording;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recording = false;
		filePath = Environment.getExternalStorageDirectory() + FILE_NAME;

		myCamera = getCameraInstance();

		initUI();
	}

	private void initUI() {
		myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
		FrameLayout myCameraPreview = (FrameLayout) findViewById(R.id.videoview);
		myCameraPreview.addView(myCameraSurfaceView);

		recordButton = (Button) findViewById(R.id.mybutton);
		recordButton.setOnClickListener(this);
	}

	private Camera getCameraInstance() {
		Camera camera = null;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return camera;
	}

	private void releaseCamera() {
		if (myCamera != null) {
			myCamera.release();
			myCamera = null;
		}
	}

	private boolean prepareMediaRecorder() {
		myCamera = getCameraInstance();
		mediaRecorder = new MediaRecorder();

		myCamera.unlock();
		mediaRecorder.setCamera(myCamera);

		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));

		mediaRecorder.setOutputFile(filePath);
		mediaRecorder.setMaxDuration(MAX_DURATION);
		mediaRecorder.setMaxFileSize(MAX_FILE_SIZE);

		mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			return false;
		}
		return true;

	}

	@Override
	protected void onPause() {
		super.onPause();

		releaseMediaRecorder();
		releaseCamera();
	}

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			myCamera.lock();
		}
	}

	public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

		private SurfaceHolder mHolder;
		private Camera mCamera;

		public MyCameraSurfaceView(Context context, Camera camera) {
			super(context);
			mCamera = camera;

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
		}
	}

	@Override
	public void onClick(View v) {
		if (recording) {
			stopRecordingVideo();
		} else {
			startRecordingVideo();
		}
	}

	private void startRecordingVideo() {
		releaseCamera();

		if (!prepareMediaRecorder()) {
			Toast.makeText(MainActivity.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
		}

		mediaRecorder.start();
		recording = true;
		recordButton.setText("STOP");
	}

	private void stopRecordingVideo() {
		mediaRecorder.stop();
		recording = false;
		recordButton.setText("REC");
		releaseMediaRecorder();
	}
}
