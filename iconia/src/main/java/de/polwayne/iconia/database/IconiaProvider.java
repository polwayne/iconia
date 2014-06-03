package de.polwayne.iconia.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class IconiaProvider extends ContentProvider {

    private static final String CONTENT_AUTHORITY = "de.polwayne.iconia";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri ITEMS_URI = BASE_CONTENT_URI.buildUpon().appendPath("items").build();

    private static final int ITEMS = 1;
    private static final int ITEM_ID = 2;

    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, "items", ITEMS);
        uriMatcher.addURI(CONTENT_AUTHORITY, "items/*", ITEM_ID);
    }

    private DatabaseHelper mDbHelper;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String rowID = null;
        String selectedTable = null;

        final int match = uriMatcher.match(uri);
        switch(match){
            case ITEMS:
                selectedTable = DatabaseTables.ItemTbl.TABLE_NAME;
                break;
            case ITEM_ID:
                selectedTable = DatabaseTables.ItemTbl.TABLE_NAME;
                rowID = uri.getPathSegments().get(1);
                selection = selectedTable + "." + DatabaseTables.ItemTbl.ID + "=" + rowID +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : "");
        }

        if(selection == null)
            selection = "1";

        int updateCount = db.delete(selectedTable, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ITEMS:
                return "vnd.android.cursor.dir/vnd.item";
            case ITEM_ID:
                return "vnd.android.cursor.item/vnd.item";
            default:
                throw new IllegalArgumentException("Unsopported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = -1;
        Uri resultUri = null;

        final int match = uriMatcher.match(uri);
        switch(match){
            case ITEMS:
                id = db.insert(DatabaseTables.ItemTbl.TABLE_NAME, null, values);
                resultUri = ITEMS_URI;
                break;
        }

        Uri insertedId = null;
        if(id > -1){
            insertedId = ContentUris.withAppendedId(resultUri, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
        }
        return insertedId;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // Open the database
        SQLiteDatabase db;
        try{
            db = mDbHelper.getWritableDatabase();
        } catch(SQLiteException ex){
            db = mDbHelper.getReadableDatabase();
        }

        String groupBy = null;
        String having = null;
        String rowID = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)){
            case ITEMS:
                queryBuilder.setTables(DatabaseTables.ItemTbl.TABLE_NAME);
                break;
            case ITEM_ID:
                rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DatabaseTables.ItemTbl.TABLE_NAME + "." + DatabaseTables.ItemTbl.ID + "=" + rowID);
                queryBuilder.setTables(DatabaseTables.ItemTbl.TABLE_NAME);
                break;
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String rowID = null;
        String selectedTable = null;

        final int match = uriMatcher.match(uri);
        switch(match){
            case ITEMS:
                selectedTable = DatabaseTables.ItemTbl.TABLE_NAME;
                break;
            case ITEM_ID:
                selectedTable = DatabaseTables.ItemTbl.TABLE_NAME;
                rowID = uri.getPathSegments().get(1);
                selection = selectedTable + "." + DatabaseTables.ItemTbl.ID + "=" + rowID +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : "");
        }

        if(selection == null)
            selection = "1";

        int updateCount = db.update(selectedTable, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }
}
