package de.polwayne.iconia.database;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import de.polwayne.iconia.pojo.Item;

/**
 * Created by Paul on 30.05.14.
 */
public class ItemDAO {

    private Context mContext;

    public ItemDAO(Context ctx){
        mContext = ctx;
    }

    public Item loadItem(long id){
        Uri itemUri = ContentUris.withAppendedId(IconiaProvider.ITEMS_URI, id);
        Cursor c = mContext.getContentResolver().query(itemUri,null,null,null,null);
        if(c.moveToFirst())
        {
            Item i = new Item();
            i.id = id;
            i.title = c.getString(c.getColumnIndex(DatabaseTables.ItemColumns.TITLE));
            i.details = c.getString(c.getColumnIndex(DatabaseTables.ItemColumns.DETAILS));
            i.data = c.getString(c.getColumnIndex(DatabaseTables.ItemColumns.DATA));
            i.category = c.getInt(c.getColumnIndex(DatabaseTables.ItemColumns.CATEGORY));
            i.time = c.getLong(c.getColumnIndex(DatabaseTables.ItemColumns.TIMESTAMP));
            c.close();
            return i;
        }
        return null;
    }

    public boolean deleteItem(long id){
        Uri itemUri = ContentUris.withAppendedId(IconiaProvider.ITEMS_URI, id);
        int count = mContext.getContentResolver().delete(itemUri,null,null);
        return count > 0;
    }

}
