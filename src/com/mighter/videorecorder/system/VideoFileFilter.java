package com.mighter.videorecorder.system;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created with IntelliJ IDEA.
 * User: valentin
 * Date: 27.10.13
 * Time: 4:48
 * To change this template use File | Settings | File Templates.
 */
public class VideoFileFilter implements FilenameFilter {
    private String regexp;

    public VideoFileFilter(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean accept(File dir, String filename) {
        return filename.matches(regexp);
    }
}
