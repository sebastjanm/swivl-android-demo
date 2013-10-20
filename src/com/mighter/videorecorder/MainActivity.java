package com.mighter.videorecorder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private static final int MAX_FILE_SIZE = 5 * 1000 * 1000;
	private static final int MAX_DURATION = 60 * 1000;

	private Camera mCamera;
	private CameraSurfaceView mCameraSurfaceView;
	private MediaRecorder mMediaRecorder;
    private VideoManager mVideoManager;

	private Button mRecordButton;
    private ListView mVideosListView;
    private ArrayAdapter<String> mAdapter;

    private boolean mIsRecording;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mIsRecording = false;

        mCamera = getCameraInstance();
        mVideoManager = new VideoManager();

		initUI();
	}

	private void initUI() {
        mCameraSurfaceView = new CameraSurfaceView(this, mCamera);
		FrameLayout myCameraPreview = (FrameLayout) findViewById(R.id.videoview);
		myCameraPreview.addView(mCameraSurfaceView);

        mRecordButton = (Button) findViewById(R.id.mybutton);
        mVideosListView = (ListView) findViewById(R.id.video_list);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mVideoManager.getRecordedVideos());
        mVideosListView.setAdapter(mAdapter);
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
		if (mCamera != null) {
            mCamera.release();
            mCamera = null;
		}
	}

	private boolean prepareMediaRecorder() {
        mCamera = getCameraInstance();
        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));

        mMediaRecorder.setOutputFile(mVideoManager.getNewFileName());
        mMediaRecorder.setMaxDuration(MAX_DURATION);
        mMediaRecorder.setMaxFileSize(MAX_FILE_SIZE);

        mMediaRecorder.setPreviewDisplay(mCameraSurfaceView.getHolder().getSurface());

		try {
            mMediaRecorder.prepare();
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
		if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
		}
	}

	public void onButtonClick(View v) {
		if (mIsRecording) {
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

        mMediaRecorder.start();
        mIsRecording = true;
        mRecordButton.setText("STOP");
	}

	private void stopRecordingVideo() {
        mMediaRecorder.stop();
        mIsRecording = false;
        mRecordButton.setText("REC");
		releaseMediaRecorder();
        mVideoManager.getRecordedVideos();
        mAdapter.notifyDataSetChanged();
	}
}
