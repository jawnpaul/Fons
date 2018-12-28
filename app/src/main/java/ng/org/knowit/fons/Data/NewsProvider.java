package ng.org.knowit.fons.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class NewsProvider extends ContentProvider {

    private static final String TAG = NewsProvider.class.getSimpleName();

    private NewsDbHelper mNewsDbHelper;


    private static final int NEWS = 100;
    private static final int NEWS_WITH_ID = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(NewsContract.CONTENT_AUTHORITY,
                NewsContract.NewsEntry.TABLE_NAME,
                NEWS);

        uriMatcher.addURI(NewsContract.CONTENT_AUTHORITY,
                NewsContract.NewsEntry.TABLE_NAME + "/#",
                NEWS_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mNewsDbHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        final SQLiteDatabase db = mNewsDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match){
            case NEWS:
                retCursor = db.query(NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case NEWS_WITH_ID:
                String id  = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mNewsDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case NEWS:
                long id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);

                if(id > 0){
                    Log.d(TAG, "INSERTED");
                    returnUri = ContentUris.withAppendedId(NewsContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri );
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }


        getContext().getContentResolver().notifyChange(uri, null);

        return  returnUri;
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case NEWS:
                //Rows aren't counted with null selection
                selection = (selection == null) ? "1" : selection;
                break;
            case NEWS_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", NewsContract.NewsEntry._ID);
                selectionArgs = new String[]{String.valueOf(id)};

                break;
            default:

                throw new IllegalArgumentException("Illegal delete URI");

        }

        SQLiteDatabase db = mNewsDbHelper.getWritableDatabase();
        int count = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);

        if (count > 0) {
            //Notify observers of the change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mNewsDbHelper.getWritableDatabase();

        int rowsUpdated =0;

        int match = sUriMatcher.match(uri);
        switch (sUriMatcher.match(uri)){
            case NEWS:
                rowsUpdated = db.update(NewsContract.NewsEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            case NEWS_WITH_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(NewsContract.NewsEntry.TABLE_NAME,
                            values,
                            NewsContract.NewsEntry._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(NewsContract.NewsEntry.TABLE_NAME,
                            values,
                            NewsContract.NewsEntry._ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
