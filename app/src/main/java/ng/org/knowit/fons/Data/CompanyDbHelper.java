package ng.org.knowit.fons.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CompanyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CompanyEntry.db";
    private static final int DATABASE_VERSION = 1;

    public CompanyDbHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_COMPANY_ENTRY_TABLE = "CREATE TABLE " +
                CompanyContract.CompanyEntry.TABLE_NAME + " (" +
                CompanyContract.CompanyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_SYMBOL + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_OPEN + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_HIGH + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_LOW + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_PRICE + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_VOLUME + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_LATEST_TRADING_DAY + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_PREVIOUS_CLOSE + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_CHANGE + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_COMPANY_CHANGE_PERCENT + " TEXT NOT NULL, " +
                CompanyContract.CompanyEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_COMPANY_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompanyContract.CompanyEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
