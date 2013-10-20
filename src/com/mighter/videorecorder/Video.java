package com.mighter.videorecorder;

import java.io.File;

public class Video {

    public static final String DEFAULT_VIDEO_NAME = "untitled";
    public static final String DEFAULT_VIDEO_DESCRIPTION = "some description";

    private File mFile;
    private String mName;
    private String mDescription;

    public Video(File file, String name, String description) {
        mFile = file;
        mName = name;
        mDescription = description;
    }

    public Video(File file) {
        mFile = file;
        mName = DEFAULT_VIDEO_NAME;
        mDescription = DEFAULT_VIDEO_DESCRIPTION;
    }

    public File getFile() {
        return mFile;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public String toString() {
        return mFile.getName();
    }

}
