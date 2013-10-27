package com.mighter.videorecorder.system;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import com.mighter.videorecorder.R;
import com.mighter.videorecorder.data.Video;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoManager {
    public static final String DOT = ".";
    public static final String DEFAULT_EXTENSION = "mp4";
    private Context context;

    public VideoManager(Context context) {
        this.context = context;
    }

    public Video createNewVideo(String path, String extension, String name){
        return new Video(path+name+Video.DOT+extension, name);
    }

    public Video createNewVideo(){
        return createNewVideo(getDefaultPath(), DEFAULT_EXTENSION, getDefaultName());
    }

    public List<Video> getAllVideos(){
        return DBHelper.getInstance(context).getAllVideos();
    }

    private String getDefaultPath(){
        StringBuilder builder = new StringBuilder();
        builder.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        builder.append(File.separator);
        builder.append(context.getResources().getString(R.string.app_name));
        String path = builder.toString();
        createDirectory(path);
        return path;
    }

    private void createDirectory(String path) {
        File pathFile = new File(path);
        if(pathFile.exists()) return;
        pathFile.mkdir();
    }

    private String getDefaultName(){
        Time time = new Time();
        time.setToNow();
        Log.d(Video.class.getSimpleName(), time.format2445());
        return time.format2445();
    }

    public void updateVideos(){
        File videosDir = new File(getDefaultPath());
        File[] files = videosDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if(filename.lastIndexOf(DEFAULT_EXTENSION)<filename.length()-DEFAULT_EXTENSION.length()){
                    return false;
                }
                return true;
            }
        });
        List<Video> videos = new ArrayList<Video>();
        for(File f:files){
            String name = f.getName().replaceAll(DEFAULT_EXTENSION,"");
            Video video = new Video(f.getAbsolutePath(), name);
            videos.add(video);
        }
        DBHelper.getInstance(context).updateVideos(videos);
    }
}