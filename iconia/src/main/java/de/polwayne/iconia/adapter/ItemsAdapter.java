package de.polwayne.iconia.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.polwayne.iconia.R;
import de.polwayne.iconia.database.DatabaseTables;
import de.polwayne.iconia.utils.Utils;

/**
 * Created by Paul on 30.05.14.
 */

public class ItemsAdapter extends SimpleCursorAdapter {

    private String[] mTitles;
    private int[] mIcons;
    private Context mContext;
    private int layout;
    private int idIndex = -1;
    private int dataIndex = -1;
    private int titleIndex = -1;
    private Typeface mTypeface;

    public ItemsAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, android.support.v4.widget.SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mContext = context;
        this.layout = layout;
        mTypeface = Typeface.createFromAsset(context.getAssets(),"IndieFlower.ttf");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.title = (TextView) v.findViewById(R.id.list_item_title);
        holder.title.setTypeface(mTypeface);
        holder.image = (ImageView) v.findViewById(R.id.list_item_image);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

        ViewHolder holder = (ViewHolder)v.getTag();

        if(idIndex == -1) initIndexes(c);

        int itemid = c.getInt(idIndex);
        String title = c.getString(titleIndex);
        String data = c.getString(dataIndex);

        holder.itemid = itemid;
        holder.title.setText(title);
        holder.data = data;

        if(holder.data != null)
            new ThumbnailTask(mContext, holder).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initIndexes(Cursor c){
        idIndex = c.getColumnIndex(DatabaseTables.ItemColumns.ID);
        titleIndex = c.getColumnIndex(DatabaseTables.ItemColumns.TITLE);
        dataIndex = c.getColumnIndex(DatabaseTables.ItemColumns.DATA);
    }

    public static class ViewHolder {
        int itemid;
        String data;
        TextView title;
        ImageView image;
    }

    private static class ThumbnailTask extends AsyncTask<Void,Integer,Bitmap> {
        private int mItemid;
        private ViewHolder mHolder;
        private Context mContext;

        public ThumbnailTask(Context context, ViewHolder holder) {
            mContext = context;
            mHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
                try {
                    File file = new File(mHolder.data);
                    Bitmap bmp = Utils.createScaledBitmapFromStream(new FileInputStream(file), 300, 300);
                    return bmp;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
               mHolder.image.setImageBitmap(bitmap);
            }
            else{
                mHolder.image.setImageResource(R.drawable.ic_launcher);
            }
        }

    }
}
