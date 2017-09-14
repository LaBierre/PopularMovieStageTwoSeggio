package com.example.standard.popularmoviestagetwoseggio.dataFromDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vince on 10.09.2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_POSTER + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_STORY + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_DATE + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_RATING + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
