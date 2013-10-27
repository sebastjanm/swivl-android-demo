package com.mighter.videorecorder;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.mighter.videorecorder.data.Video;
import com.mighter.videorecorder.system.DBHelper;
import com.mighter.videorecorder.system.VideoManager;
import com.mighter.videorecorder.view.CameraSurfaceView;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int MAX_FILE_SIZE = 5 * 1000 * 1000;
	private static final int MAX_DURATION = 60 * 1000;

	private Camera camera;
	private CameraSurfaceView cameraSurfaceView;
	private MediaRecorder mediaRecorder;

	private Button btnRec;
	private SurfaceHolder surfaceHolder;
	boolean recording;
    private Video currentVideo;
    private VideoManager videoManager;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		videoManager = new VideoManager(this);
		camera = getCameraInstance();
		initUI();
        videoManager.updateVideos();
	}

	private void initUI() {
		cameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraPreview);
        cameraSurfaceView.setCamera(camera);
		btnRec = (Button) findViewById(R.id.btnRec);
		btnRec.setOnClickListener(this);
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
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

	private boolean prepareMediaRecorder() {
		camera = getCameraInstance();
		mediaRecorder = new MediaRecorder();

		camera.unlock();
		mediaRecorder.setCamera(camera);

		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
        VideoManager videoManager = new VideoManager(this);
        currentVideo = videoManager.createNewVideo();
        createOutputFile(currentVideo.getUri());
        Log.d("URI", String.valueOf(currentVideo.getUri()));
        mediaRecorder.setOutputFile(currentVideo.getUri());
		mediaRecorder.setMaxDuration(MAX_DURATION);
		mediaRecorder.setMaxFileSize(MAX_FILE_SIZE);
		mediaRecorder.setPreviewDisplay(cameraSurfaceView.getHolder().getSurface());
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
            Log.e(IllegalStateException.class.getSimpleName(), String.valueOf(e.getMessage()));
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), String.valueOf(e.getMessage()));
			releaseMediaRecorder();
			return false;
		}
        return true;

	}

    private void createOutputFile(String uri) {
        File output = new File(uri);
        if(!output.exists()){
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
			camera.lock();
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
            return;
		}
		mediaRecorder.start();
		recording = true;
		btnRec.setText("STOP");
	}

	private void stopRecordingVideo() {
		mediaRecorder.stop();
        recording = false;
        DBHelper.getInstance(this).add(currentVideo);
		btnRec.setText("REC");
		releaseMediaRecorder();
	}
}
