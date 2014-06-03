package de.polwayne.iconia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.polwayne.iconia.R;

/**
 * Created by Paul on 30.04.14.
 */

public class DrawerAdapter extends BaseAdapter {

    private String[] mTitles;
    private int[] mIcons;
    private Context mContext;

    public DrawerAdapter(Context c, String[] titles, int[] icons){
        mContext = c;
        mTitles = titles;
        mIcons = icons;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mTitles[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.drawer_item,parent,false);
        TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
        ImageView iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
        tv_title.setText(mTitles[position]);
        iv_icon.setImageResource(mIcons[position]);
        return v;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
