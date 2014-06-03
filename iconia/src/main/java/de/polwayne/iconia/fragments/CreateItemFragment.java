package de.polwayne.iconia.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import de.polwayne.iconia.R;
import de.polwayne.iconia.database.DatabaseTables;
import de.polwayne.iconia.database.IconiaProvider;
import de.polwayne.iconia.pojo.Item;

/**
 * Created by Paul on 30.04.14.
 */
public class CreateItemFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    private EditText et_title;
    private EditText et_story;
    private ImageView iv_image;
    private Item mItem = new Item();
    private RadioGroup rg_category;

    private static final int REQUEST_GALLERY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            mItem.title = et_title.getText().toString();
            mItem.details = et_story.getText().toString();
            insertItem();;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_item, container, false);
        et_title = (EditText) v.findViewById(R.id.et_title);
        et_story = (EditText) v.findViewById(R.id.et_story);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"IndieFlower.ttf");
        et_title.setBackground(null);
        et_story.setBackground(null);
        et_title.setTypeface(tf);
        et_story.setTypeface(tf);

        iv_image = (ImageView) v.findViewById(R.id.details_image);
        rg_category = (RadioGroup) v.findViewById(R.id.rdogrp);
        rg_category.setOnCheckedChangeListener(this);
        iv_image.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.details_image){
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i,REQUEST_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY){
            if(resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                if(selectedImagePath != null) {
                    iv_image.setPadding(0, 0, 0, 0);
                    iv_image.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                    mItem.data = selectedImagePath;
                }
            }
         }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.btn1:
                mItem.category = Item.CATEGORY_THOUGHT;
                break;
            case R.id.btn2:
                mItem.category = Item.CATEGORY_DREAM;
                break;
            case R.id.btn3:
                mItem.category = Item.CATEGORY_MEMORY;
                break;
        }
    }

    public String getPath(Uri contentUri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri,
                        proj,
                        null,
                        null,
                        null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void insertItem(){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseTables.ItemColumns.TIMESTAMP, System.currentTimeMillis());
        cv.put(DatabaseTables.ItemColumns.CATEGORY, mItem.category);
        cv.put(DatabaseTables.ItemColumns.DATA, mItem.data);
        cv.put(DatabaseTables.ItemColumns.TITLE, mItem.title);
        cv.put(DatabaseTables.ItemColumns.DETAILS, mItem.details);

        Uri result = getActivity().getContentResolver().insert(IconiaProvider.ITEMS_URI,cv);
        if(result != null)
            getActivity().finish();
    }
}
