package com.mighter.videorecorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_VIDEOS= "videos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FILE = "file";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";

    private static final String DATABASE_NAME = "videos.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_VIDEOS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FILE
            + " TEXT, " + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

}
