package ng.org.knowit.fons.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NewsEntry.db";
    private static final int DATABASE_VERSION = 1;

    public NewsDbHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_NEWS_ENTRY_TABLE = "CREATE TABLE " +
                NewsContract.NewsEntry.TABLE_NAME + " (" +
                NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsContract.NewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_PUBLISHED_AT + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_URL + " TEXT NOT NULL, " +
                NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE + " TEXT NOT NULL " +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_ENTRY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
