package com.mighter.videorecorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.database.SQLException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoManager {

    private static final String FILES_PATH = "/swivlVideos";
    private List<Video> mVideos;

    private SQLiteDatabase mDatabase;
    private MySQLiteHelper mDatabaseHelper;
    private String[] mAllColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_FILE, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_DESCRIPTION};

    private String mFilesPath;

    public VideoManager(Context context) {
        mDatabaseHelper = new MySQLiteHelper(context);
        mFilesPath = Environment.getExternalStorageDirectory() + FILES_PATH;
        File filesPath = new File(mFilesPath);
        filesPath.mkdir();
        mVideos = new ArrayList<Video>();
    }

    public void open() throws SQLException {
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        mDatabaseHelper.close();
    }

    /**
     *
     * @return full uri of new video file
     */
    public String createVideo() {
        ContentValues values = new ContentValues();

        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
        String fileName = String.format("VIDEO_%s.mp4", dateString);
        File videoFile = new File(mFilesPath + "/" + fileName);
        Video video = new Video(videoFile);
        mVideos.add(video);

        values.put(MySQLiteHelper.COLUMN_FILE, video.getFile().toString());
        values.put(MySQLiteHelper.COLUMN_NAME, video.getName());
        values.put(MySQLiteHelper.COLUMN_DESCRIPTION, video.getDescription());

        mDatabase.insert(MySQLiteHelper.TABLE_VIDEOS, null, values);

        return video.getFile().toString();
    }

    public List<Video> getAllVideos() {
        mVideos.clear();
        Cursor cursor = mDatabase.query(MySQLiteHelper.TABLE_VIDEOS, mAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Video video = cursorToVideo(cursor);
            mVideos.add(video);
            cursor.moveToNext();
        }
        cursor.close();
        return mVideos;
    }


    private Video cursorToVideo(Cursor cursor) {
        Video video = new Video(new File(cursor.getString(1)), cursor.getString(2), cursor.getString(3));
        return video;
    }

}
