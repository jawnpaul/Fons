package ng.org.knowit.fons.Data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {

    public static final class NewsEntry implements BaseColumns{

        public static final String TABLE_NAME = "NewsEntry";
        public static final String COLUMN_AUTHOR = "NewsAuthor";
        public static final String COLUMN_TITLE = "NewsTitle";
        public static final String COLUMN_DESCRIPTION = "NewsDescription";
        public static final String COLUMN_URL = "NewsUrl";
        public static final String COLUMN_URL_TO_IMAGE = "NewsImageUrl";
        public static final String COLUMN_PUBLISHED_AT = "NewsPublishedAt";
        public static final String COLUMN_CONTENT = "NewsContent";
    }

    //Unique authority string for the content provider
    public static final String CONTENT_AUTHORITY = "ng.org.knowit.fons";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(NewsEntry.TABLE_NAME).build();

    public static Uri buildSingleCompany(long id) {
        return CONTENT_URI.buildUpon()
                .appendPath(Long.toString(id))
                .build();
    }

    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}
