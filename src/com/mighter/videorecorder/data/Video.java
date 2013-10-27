package com.mighter.videorecorder.data;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;

import java.io.File;

public class Video {
    public static final String DOT = ".";
    private String uri;
    private String description;
    private String name;
    private final int id;


    public Video(String uri, String name) {
        this.name = name;
        this.id = name.hashCode();
        this.uri = uri;
    }

    private void createOutDir(String path) {
        File outputDir = new File(path);
        if(outputDir.exists()){
            return;
        }
        outputDir.mkdir();
    }


    public String getDescription() {
        return description;
    }

    public String getName(){
        return name;
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
