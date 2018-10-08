package ng.org.knowit.fons.Data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class CompanyContract {

    public static final class CompanyEntry implements BaseColumns{

        public static final String TABLE_NAME = "CompanyEntry";
        public static final String COLUMN_COMPANY_SYMBOL = "CompanySymbol";
        public static final String COLUMN_COMPANY_OPEN = "CompanyOpen";
        public static final String COLUMN_COMPANY_HIGH = "CompanyHigh";
        public static final String COLUMN_COMPANY_LOW = "CompanyLow";
        public static final String COLUMN_COMPANY_PRICE = "CompanyPrice";
        public static final String COLUMN_COMPANY_VOLUME = "CompanyVolume";
        public static final String COLUMN_COMPANY_LATEST_TRADING_DAY = "CompanyLatestTradingDay";
        public static final String COLUMN_COMPANY_PREVIOUS_CLOSE= "CompanyPreviousClose";
        public static final String COLUMN_COMPANY_CHANGE = "CompanyChange";
        public static final String COLUMN_COMPANY_CHANGE_PERCENT = "CompanyChangePercent";
        public static final String COLUMN_TIMESTAMP = "TimeStamp";
    }

    //Unique authority string for the content provider
    public static final String CONTENT_AUTHORITY = "ng.org.knowit.fons";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(CompanyEntry.TABLE_NAME).build();

    public static Uri buildDetailActivity(long id) {
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
