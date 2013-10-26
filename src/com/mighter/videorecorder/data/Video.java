package com.mighter.videorecorder.data;

import java.io.File;

public class Video {
    private File path;
    private String description;
    private String name;
    private String extension;

    public Video(File path, String extension, String name, String description) {
        this.path = path;
        this.name = name;
        this.extension = extension;
        this.description = description;
    }


    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName(){
        return name;
    }
}
