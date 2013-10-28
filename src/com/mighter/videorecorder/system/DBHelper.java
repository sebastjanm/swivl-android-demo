package com.mighter.videorecorder.system;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.mighter.videorecorder.data.Video;

import java.util.*;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "recorded_videos";

  public static final String TABLE_NAME = "videos";
  public static final String URI = "uri";
  public static final String DESCRIPTION = "desc";
    private static final String NAME = "name";
    private static final String ID = "_id";
    private static final String[] COLUMNS = {ID, URI, NAME, DESCRIPTION};
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" + ID + " integer primary key autoincrement, "
      + URI + " text, " + NAME + " text, "+DESCRIPTION + " text);";
    private static DBHelper sDBHelper;

  private DBHelper(Context context) {
    super(context, DB_NAME, null,1);
  }

    public static void init(Context context){
        sDBHelper = new DBHelper(context);
    }

    public static DBHelper getInstance() {
        return sDBHelper;
    }

    public void release() {
        sDBHelper = null;
    }

    @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(CREATE_TABLE);
  }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Ignored
    }



  public void add(Video video){
    SQLiteDatabase db = getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(ID, video.getId());
    cv.put(URI, video.getUri());
    cv.put(NAME, video.getName());
    cv.put(DESCRIPTION, video.getDescription());
    db.insertOrThrow(TABLE_NAME, null, cv);
    db.close();
  }

    public void remove(Video video){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, String.valueOf(video.getId()), null);
        db.close();
    }

    public Video[] getAllVideos(){
        List<Video> videos = new ArrayList<Video>();
        Cursor cursor = getAllFields();
        cursor.moveToFirst();
        if(cursor.getCount()==0) return new Video[0];
        do{
            Video video = new Video(cursor.getString(1), cursor.getString(2));
            video.setDescription(cursor.getString(3));
            videos.add(video);
        }while (cursor.moveToNext());
        cursor.close();
        Video[] videosArray = new Video[videos.size()];
        videos.toArray(videosArray);
        return videosArray;
    }

    public Cursor getAllFields() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
    }


    public void updateVideos(List<Video> videos){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = getAllFields();
        for(Video video:videos){
            ContentValues cv = new ContentValues();
            cv.put(ID, video.getId());
            cv.put(URI, video.getUri());
            cv.put(NAME, video.getName());
            cv.put(DESCRIPTION, video.getDescription());
            db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        }
        if(cursor.getCount()>videos.size()){
            List<Video> allVideos = Arrays.asList(getAllVideos());
            for(Video video:allVideos){
                if(!videos.contains(video)){
                    remove(video);
                }
            }
        }
        db.close();
    }
}