package com.mighter.videorecorder;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoManager {

    private static final String FILES_PATH = "/swivlVideos";
    private List<String> mFileNames;

    private String mFilesPath;

    public VideoManager() {
        mFilesPath = Environment.getExternalStorageDirectory() + FILES_PATH;
        File filesPath = new File(mFilesPath);
        filesPath.mkdir();
        mFileNames = new ArrayList<String>();
    }

    public String getNewFileName() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
        String fileName = String.format("/VIDEO_%s.mp4", dateString);
        return mFilesPath + "/" + fileName;
    }

    public List<String> getRecordedVideos() {
        File[] files = new File(mFilesPath).listFiles();
        mFileNames.clear();
        for (File file : files) {
            mFileNames.add(file.getName());
        }
        return mFileNames;
    }

}
