package com.example.standard.popularmoviestagetwoseggio.dataFromDatabase;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vince on 10.09.2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.standard.popularmoviestagetwoseggio";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public MovieContract() {
    }

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE);

        public final static String _ID = BaseColumns._ID;

        /**
         * Name of database table for movies
         */
        public final static String TABLE_NAME = "movies";

        /*
         * Title of the Movie.
         *
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_POSTER = "poster";
        public final static String COLUMN_MOVIE_TITLE = "title";
        public final static String COLUMN_MOVIE_STORY = "story";
        public final static String COLUMN_MOVIE_DATE = "date";
        public final static String COLUMN_MOVIE_RATING = "rating";
        public final static String COLUMN_MOVIE_ID = "id";

        /**
         * * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    }
}
