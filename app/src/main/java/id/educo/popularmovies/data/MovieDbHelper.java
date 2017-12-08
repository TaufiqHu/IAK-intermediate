package id.educo.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tito on 08/12/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movie.db";
    private static final short DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_ID + " INTEGER NOT NULL UNIQUE, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVEGARE + " FLOAT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER DEFAULT 0, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " FLOAT DEFAULT NULL" +
                ");";
        db.execSQL(SQL_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
